package me.maxandroid.biz_login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import me.maxandroid.biz_login.api.AccountApi
import me.maxandroid.biz_login.databinding.ActivityLoginBinding
import me.maxandroid.biz_login.databinding.ActivityRegisterBinding
import me.maxandroid.common.http.ApiFactory
import me.maxandroid.common.ui.compoment.HiBaseActivity
import me.maxandroid.hilibrary.restful.HiCallback
import me.maxandroid.hilibrary.restful.HiResponse

@Route(path = "/account/registration")
class RegistrationActivity : HiBaseActivity() {

    private lateinit var mBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.actionBack.setOnClickListener { onBackPressed() }

        mBinding.actionSubmit.setOnClickListener { submit() }
    }

    private fun submit() {
        val orderId = mBinding.inputItemOrderId.getEditText().text.toString()
        val moocId = mBinding.inputItemMoocId.getEditText().text.toString()

        val username = mBinding.inputItemUsername.getEditText().text.toString()
        val pwd = mBinding.inputItemPwd.getEditText().text.toString()
        val pwdSec = mBinding.inputItemPwdCheck.getEditText().text.toString()

        if (TextUtils.isEmpty(orderId)
            || (TextUtils.isEmpty(moocId))
            || (TextUtils.isEmpty(username))
            || TextUtils.isEmpty(pwd)
            || (!TextUtils.equals(pwd, pwdSec))
        ) {
            return
        }


        ApiFactory.create(AccountApi::class.java).register(username, pwd, moocId, orderId)
            .enqueue(object : HiCallback<String> {
                override fun onSuccess(response: HiResponse<String>) {
                    if (response.code == HiResponse.SUCCESS) {
                        //注册成功
                        var intent = Intent()
                        intent.putExtra("username", username)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        showToast(response.msg)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showToast(throwable.message)
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
}