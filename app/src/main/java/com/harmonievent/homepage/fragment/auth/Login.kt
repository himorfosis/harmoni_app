package com.harmonievent.homepage.fragment.auth

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harmonievent.R
import com.harmonievent.dialog.DialogLoading
import com.harmonievent.homepage.HomeActivity
import com.harmonievent.network.config.AppNetwork
import com.harmonievent.network.service.UserService
import com.harmonievent.utilities.preferences.AppPreferences
import com.harmonievent.utilities.preferences.HarmoniPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast

class Login : Fragment() {

    lateinit var loadingDialog: DialogLoading
    val service = AppNetwork.buildService(UserService::class.java)
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        setDialog()

    }

    private fun initUI() {
        login_btn.onClick {
            checkAuth()
        }

    }

    private fun checkAuth() {

        val username = username_ed.text.toString()
        val password = password_ed.text.toString()

        if (username.isNotEmpty() && password.isNotEmpty()) {
            checkLoginStatus(username, password)
        } else {
            onNotComplete()
        }

    }

    private fun checkLoginStatus(username: String, password: String) {

        service.fetchUser()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe({

                for (pos in 0 until  it.data.size) {

                    var data = it.data[pos]
                    if (data.username == username && data.password == password) {
                        // save data preferences
                        HarmoniPreferences.account.setString("name", username )
                        HarmoniPreferences.account.setString("password", password )
                        HarmoniPreferences.account.setString("email", data.email )
                        HarmoniPreferences.account.setString("phone", data.no_telp )
                        HarmoniPreferences.account.setString("id", data.id_user )

                        onSuccessful()
                        break
                    }

                }

            }, {
                onWrong()
            }).let {
                disposable.add(it)
            }
    }

    private fun setDialog() {
        loadingDialog = DialogLoading(requireContext())
        loadingDialog.setCancelable(false)
    }

    private fun onSuccessful() {

        loadingDialog.show()
        Handler().postDelayed(object : Thread() {
            override fun run() {
                loadingDialog.dismiss()
                toast(getString(R.string.successful))
                startActivity(intentFor<HomeActivity>())
            }
        }, 2000)

    }

    private fun onWrong() {
        toast(getString(R.string.wrong_email_or_password))
    }

    private fun onNotComplete() {
        toast(getString(R.string.please_complete_data))
    }

}
