package com.harmonievent.event

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.harmonievent.R
import com.harmonievent.dialog.DialogLoading
import com.harmonievent.homepage.HomeActivity
import com.harmonievent.network.config.AppNetwork
import com.harmonievent.network.service.EventService
import com.harmonievent.network.service.UserService
import com.harmonievent.utilities.DateCore
import com.harmonievent.utilities.ImageCorePermission
import com.harmonievent.utilities.preferences.AppPreferences
import com.harmonievent.utilities.preferences.HarmoniPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_event_input.*
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.toolbar_detail.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import retrofit2.http.Field
import java.lang.Exception
import java.util.*

class EventInput : AppCompatActivity() {


    lateinit var loadingDialog: DialogLoading
    val service = AppNetwork.buildService(EventService::class.java)
    private val disposable = CompositeDisposable()

    val GALLERY = 1
    var BITMAP: Bitmap? = null
    var DATE_SELECTED = ""
    var DURATION_SELECTED = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_input)

        setToolbar()
        initUI()
        addDurationToSpinner()
        imagePermission()
        setLoadingDialog()

    }

    private fun initUI() {

        choose_image_tv.onClick {
            imagePermission()
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY)
        }

        send_event_btn.onClick {
            checkSubmitDataEvent()
        }

        date_event_tv.onClick {
            selectedDateEvent()
        }

        date_event_tv.text = DateCore.getDateTodaySpecific()
        DATE_SELECTED = DateCore.getDateToday()
        isLog("date selected : $DATE_SELECTED")

    }

    private fun selectedDateEvent() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val month = if(monthOfYear < 10) "0$monthOfYear" else monthOfYear.toString()
            date_event_tv.text =  "$dayOfMonth-$month-$year"
            DATE_SELECTED = "$year-$month-$dayOfMonth"
            isLog("date selected : $DATE_SELECTED")
        }, year, month, day)

        dpd.show()
    }

    private fun checkSubmitDataEvent() {

        loadingDialog.show()

        val title = title_event_ed.text.toString()
        val description = description_event_ed.text.toString()
        val location = location_event_ed.text.toString()
        val phone = phone_event_ed.text.toString()
        val email = email_ed.text.toString()

        if (title.isNotEmpty() && description.isNotEmpty() && location.isNotEmpty() && phone.isNotEmpty()
            && email.isNotEmpty()
        ) {

            val idUser = HarmoniPreferences.account.getString("id")

            if (BITMAP != null) {

                // set image
                val fileImage = ImageCorePermission.saveImageInStorage(BITMAP!!)
                val reqFile = RequestBody.create(MediaType.parse("image/*"), fileImage)
                val body = MultipartBody.Part.createFormData("gambar", fileImage!!.name, reqFile)

                // set data
                val map = HashMap<String, RequestBody>()
                map["id_admin"] = createPartFromString("")
                map["id_user"] = createPartFromString(idUser)
                map["judul"] = createPartFromString(title)
                map["deskripsi"] = createPartFromString(description)
                map["lokasi"] = createPartFromString(location)
                map["no_telp"] = createPartFromString(phone)
                map["tgl_mulai"] = createPartFromString("")
                map["tgl_selesai"] = createPartFromString("")
                map["tgl_post"] = createPartFromString(DateCore.getDateToday())
                map["email"] = createPartFromString(email)
                map["no_telp"] = createPartFromString(phone)
                map["status"] = createPartFromString("Menunggu Verifikasi")

                sendDataEvent(body, map)

            } else {

            //  push event without image
            service.createEventWithoutImage(
                "", idUser!!, title, description, location,
                DateCore.getDateToday(), DateCore.getDateToday(), DateCore.getDateToday(),
                phone, email, "Menunggu Verifikasi", ""
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe({
                    loadingDialog.dismiss()
                    isLog("Success")
                    toast(getString(R.string.successful))
                    startActivity(
                        intentFor<HomeActivity>(
                            "HOME" to HomeActivity.EVENT
                        )
                    )
                }, {
                    loadingDialog.dismiss()
                    isLog("Failed")
                    toast("Gagal Simpan Event")
                }).let {
                    disposable.add(it)
                }

            }

        } else {
            onNotComplete()
        }

    }

    private fun addDurationToSpinner() {

        isLog("addYearToSpinner")

        val listDay: MutableList<String> = ArrayList()
        for (position in 0 until 30) {
            val day = 1 + position
            listDay.add("$day Hari")
        }

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listDay)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        duration_event_spinner.adapter = arrayAdapter
        duration_event_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val data = parent!!.getItemAtPosition(position).toString()
                DURATION_SELECTED = data.replace(" Hari", "")
                isLog("DURATION_SELECTED $data")
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //ga usah ada aksi
            }
        }

    }

    private fun imagePermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            ImageCorePermission.isWriteStoragePermissionGranted(this@EventInput)
        } else {
            ImageCorePermission.requestStoragePermission(this@EventInput)
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {

                    val bitmapImage = MediaStore.Images.Media.getBitmap(contentResolver, contentURI)
                    BITMAP = bitmapImage
                    Glide.with(this).load(bitmapImage).thumbnail().into(upload_event_img)

                } catch (e: Exception) {
                    onWrong(e.toString())
                }

            }
        }

    }

    private fun sendDataEvent(imageBody: MultipartBody.Part, mapData: Map<String, RequestBody>) {
        isLog("image : $imageBody")
        isLog("data : $mapData")
        isLog("Send Data Image")
        loadingDialog.show()
        service.createEvent(imageBody, mapData)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe({
                loadingDialog.dismiss()
                isLog("Success")
                toast(getString(R.string.successful))
                startActivity(
                    intentFor<HomeActivity>(
                        "HOME" to HomeActivity.EVENT
                    )
                )
            }, {
                loadingDialog.dismiss()
                isLog("Failed")
                toast("Gagal Simpan Event")
            }).let {
                disposable.add(it)
            }

    }

    private fun createPartFromString(it: String?): RequestBody {
        return RequestBody.create(MultipartBody.FORM, it!!)
    }


    private fun setToolbar() {

        back_bar_btn.onClick {
            finish()
        }

        title_bar_tv.text = "Input Event"

    }

    private fun setLoadingDialog() {
        loadingDialog = DialogLoading(this)
        loadingDialog.setCancelable(false)
    }

    private fun onWrong(msg: String) {
        toast(msg)
    }

    private fun onNotComplete() {
        toast(getString(R.string.please_complete_data))
    }

    private fun isLog(msg: String) {
        Log.e("Event Input", msg)
    }


}
