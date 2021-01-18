package me.maxandroid.biz_login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import me.maxandroid.biz_login.api.AccountApi
import me.maxandroid.biz_login.databinding.ActivityLoginBinding
import me.maxandroid.common.http.ApiFactory
import me.maxandroid.common.ui.compoment.HiBaseActivity
import me.maxandroid.common.utils.SPUtil
import me.maxandroid.hilibrary.restful.HiCallback
import me.maxandroid.hilibrary.restful.HiResponse

@Route(path = "/account/login")
class LoginActivity : HiBaseActivity() {

    companion object {
        private const val REQUEST_CODE_REGISTRATION = 1000
    }

    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.actionBack.setOnClickListener {
            onBackPressed()
        }

        mBinding.actionRegister.setOnClickListener {
            goRegistration()
        }

        mBinding.actionLogin.setOnClickListener {
            goLogin()
        }
    }

    private fun goLogin() {
        val name = mBinding.inputItemUsername.getEditText().text
        val password = mBinding.inputItemPassword.getEditText().text

        if (TextUtils.isEmpty(name) || (TextUtils.isEmpty(password))) {
            return
        }

        ApiFactory.create(AccountApi::class.java).login(name.toString(), password.toString())
            .enqueue(object : HiCallback<String> {
                override fun onSuccess(response: HiResponse<String>) {
                    if (response.code == HiResponse.SUCCESS) {
                        showToast(getString(R.string.login_success))

                        val data = response.data

                        SPUtil.putString("boarding-pass", data!!)
                        setResult(Activity.RESULT_OK, Intent())
                        finish()
                    } else {
                        showToast(getString(R.string.login_failed) + response.msg)
                    }

                }

                override fun onFailed(throwable: Throwable) {
                    showToast(getString(R.string.login_failed) + throwable.message)
                }
            })

    }

    fun showToast(message: String?) {
        if (TextUtils.isEmpty(message)) return
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((resultCode == Activity.RESULT_OK) and (data != null) and (requestCode == REQUEST_CODE_REGISTRATION)) {
            val username = data!!.getStringExtra("username")
            if (!TextUtils.isEmpty(username)) {
                mBinding.inputItemUsername.getEditText().setText(username)
            }
        }
    }

    private fun goRegistration() {
        startActivityForResult(
            Intent(this, RegistrationActivity::class.java),
            REQUEST_CODE_REGISTRATION
        )
    }
}