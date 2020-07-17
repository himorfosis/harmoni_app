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
import com.harmonievent.dialog.DialogInfo
import com.harmonievent.dialog.DialogLoading
import com.harmonievent.homepage.HomeActivity
import com.harmonievent.model.EventModelResponse
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
    var DATE_FINISH_SELECTED = ""
    var DURATION_EVENT = "1"

    var listDataEvent: MutableList<EventModelResponse.Data> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_input)

        setToolbar()
        initUI()
        addDurationToSpinner()
        imagePermission()
        setLoadingDialog()
        fetchDataEvent()
    }

    private fun initUI() {

        DATE_SELECTED = intent.getStringExtra("date")

        val dateData1 = DATE_SELECTED.substring(7, 10)
        val dateData = DATE_SELECTED.substring(8, 10)
        isLog("dateData : $dateData")
        isLog("dateData 1 : $dateData1")

        choose_image_tv.onClick {
            imagePermission()
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY)
        }

        send_event_btn.onClick {
            checkSubmitDataEvent()
        }

        date_event_tv.text = DATE_SELECTED
        isLog("date selected : $DATE_SELECTED")

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

            if (checkEventDuration() == false) {
                isLog("Tanggal Tersedia")
            } else {
                loadingDialog.dismiss()
                isLog("Tanggal Sudah Di Pesan")
            }

            if (checkEventDuration() == false) {
                isLog("checkEventDuration false")
                if (BITMAP != null) {

                    // set image
                    val fileImage = ImageCorePermission.saveImageInStorage(BITMAP!!)
                    val reqFile = RequestBody.create(MediaType.parse("image/*"), fileImage)
                    val body =
                        MultipartBody.Part.createFormData("gambar", fileImage!!.name, reqFile)

                    // set data
                    val map = HashMap<String, RequestBody>()
                    map["id_user"] = createPartFromString(idUser)
                    map["judul"] = createPartFromString(title)
                    map["deskripsi"] = createPartFromString(description)
                    map["lokasi"] = createPartFromString(location)
                    map["no_telp"] = createPartFromString(phone)
                    map["tgl_mulai"] = createPartFromString(DATE_SELECTED)
                    map["tgl_selesai"] = createPartFromString(DATE_FINISH_SELECTED)
                    map["tgl_post"] = createPartFromString(DateCore.getDateToday())
                    map["email"] = createPartFromString(email)
                    map["no_telp"] = createPartFromString(phone)
                    map["status"] = createPartFromString("Menunggu Verifikasi")

                    sendDataEvent(body, map)

                } else {

                    //  push event without image
                    service.createEventWithoutImage(
                        idUser!!, title, description, location,
                        DATE_SELECTED, DATE_FINISH_SELECTED, DateCore.getDateToday(),
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

            }

        } else {
            loadingDialog.dismiss()
            onNotComplete()
        }

    }

    private fun checkEventDuration(): Boolean {

        if (listDataEvent.isEmpty() || DURATION_EVENT.toInt() == 1) {
            return false
        } else {

            // check available data date event

            var statusBookingEvent = false
            var listDurationEvent: MutableList<String> = ArrayList()

            val dateStartEvent = DATE_SELECTED.substring(8, 10)
            val monthEvent = DATE_SELECTED.substring(0, 8)

            isLog("start event : $dateStartEvent")
            isLog("month event : $monthEvent")

            if (DURATION_EVENT.toInt() > 1) {
                for (position in 0 until DURATION_EVENT.toInt()) {
                    var selectedDate = dateStartEvent.toInt() + position
                    val dateEvent: String =
                        if (selectedDate < 10) "${monthEvent}0${selectedDate}" else "$monthEvent$selectedDate"
                    isLog("date event finish : $dateEvent")
                    DATE_FINISH_SELECTED = dateEvent
                    listDurationEvent.add(dateEvent)
                }
                listDurationEvent.forEach {
                    isLog("duration event date : $it")
                }

            } else {
                isLog("date event default : $DATE_SELECTED")
                DATE_FINISH_SELECTED = DATE_SELECTED
            }


            for (i in 0 until listDataEvent.size) {

                val item = listDataEvent[i]
                val monthEvent = item.tgl_mulai.substring(0, 7)

                if (item.status != "Menunggu Verifikasi") {
                    isLog("=== Status Data : ${item.status} ===")
                    if (DURATION_EVENT.toInt() > 1) {

                        var dateEventDuration = DATE_SELECTED.substring(8, 10)
                        val dateEvent = dateEventDuration.toInt() + DURATION_EVENT.toInt()

                        isLog("dateEventDuration : $dateEventDuration")
                        isLog("sumDateEvent : $dateEvent")
                        val selectedDateEvent = if (dateEvent > 10) {
                            "$monthEvent-$dateEvent"
                        } else {
                            "$monthEvent-0$dateEvent"
                        }

                        isLog("selectedDateEvent : $selectedDateEvent")
                        listDurationEvent.forEach {

                            isLog("====== CHECK DURATION ====")
                            isLog("Date Selected  $DATE_SELECTED")
                            isLog("Date Finish  $DATE_FINISH_SELECTED")
                            isLog("date event selected = $it")

                            if (it == item.tgl_mulai || it == item.tgl_selesai) {
                                onFullBookingDialog(it)
                                statusBookingEvent = true
                            }

                        }

                        if (statusBookingEvent == true) {
                            break
                        }

                    }

                }

            }

            return statusBookingEvent
        }

    }

    private fun addDurationToSpinner() {

        isLog("addYearToSpinner")

        val listDay: MutableList<String> = ArrayList()
        for (position in 0 until 7) {
            val day = 1 + position
            listDay.add("$day Hari")
        }

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listDay)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        duration_event_spinner.adapter = arrayAdapter
        duration_event_spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    val data = parent!!.getItemAtPosition(position).toString()
                    DURATION_EVENT = data.replace(" Hari", "")
                    isLog("DURATION_SELECTED $data")
                    isLog("duration data $DURATION_EVENT")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //ga usah ada aksi
                }
            }

    }

    private fun fetchDataEvent() {
        service.fetchEvent()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe({
                if (it.data.isNotEmpty()) {
                    isLog("Event Available")
                    listDataEvent.addAll(it.data)
                } else {
                    isLog("Event Empty")
                }
            }, {
                isLog("Failed")
            }).let {
                disposable.add(it)
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

    private fun onFullBookingDialog(dateEvent: String) {
        val dialog = DialogInfo(
            this@EventInput,
            "Yah, sudah dipesan",
            "Event pada tanggal $dateEvent sudah dipesan, silahkan pilih tanggal lain"
        )
        dialog.setCancelable(false)
        dialog.show()

    }

    private fun onDialog(title: String, description: String) {
        val dialog = DialogInfo(
            this@EventInput,
            title,
            description
        )
        dialog.setCancelable(false)
        dialog.show()

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
