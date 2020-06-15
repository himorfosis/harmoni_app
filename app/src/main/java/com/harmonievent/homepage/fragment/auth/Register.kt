package com.harmonievent.homepage.fragment.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harmonievent.R
import com.harmonievent.dialog.DialogLoading
import com.harmonievent.entity.UserModel
import com.harmonievent.homepage.HomeAuthActivity
import com.harmonievent.network.config.AppNetwork
import com.harmonievent.network.service.UserService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_register.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast

class Register : Fragment() {

    lateinit var loadingDialog: DialogLoading
    val service = AppNetwork.buildService(UserService::class.java)
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        setDialog()

    }

    private fun initUI() {

        register_btn.onClick {
            checkRegister()
        }

    }

    private fun checkRegister() {

        val username = username_ed.text.toString()
        val password = password_ed.text.toString()
        val email = email_ed.text.toString()
        val phone = phone_ed.text.toString()
        val name = name_ed.text.toString()
        val companyName = company_name_ed.text.toString()
        val companyAddress = company_address_ed.text.toString()

        if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()
            && phone.isNotEmpty()
        ) {
            onPostLogin(UserModel(0, name, phone, email, password))
        } else {
            onNotComplete()
        }


    }

    private fun setDialog() {
        loadingDialog = DialogLoading(requireContext())
        loadingDialog.setCancelable(false)
    }

    private fun onPostLogin(it: UserModel) {
        isLog("start login")
        loadingDialog.show()

        service.register(it.nama_user, it.no_telp, it.email, it.username, it.password)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe({
                isLog("Success")
                onSuccessful()
            }, {
                isLog("Failed")
                onFailed()
            }).let {
                disposable.add(it)
            }
    }

    private fun onSuccessful() {
        loadingDialog.dismiss()
        toast(getString(R.string.successful))
        startActivity(
            intentFor<HomeAuthActivity>(
                "HOME" to HomeAuthActivity.LOGIN)
        )

    }

    private fun onFailed() {
        loadingDialog.dismiss()
        toast("Register Gagal")
    }

    private fun onWrong() {
        toast(getString(R.string.wrong_email_or_password))
    }

    private fun onNotComplete() {
        toast(getString(R.string.please_complete_data))
    }

    private fun isLog(msg: String) {
        Log.e("Register", msg)
    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }

}
