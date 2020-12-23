package me.maxandroid.mainproject.route

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import java.lang.RuntimeException

@Interceptor(name = "biz_interceptor", priority = 0)
class BizInterceptor : IInterceptor {
    private lateinit var context: Context

    override fun init(context: Context) {
        this.context = context
    }

    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        val flag = postcard.extra
        if (flag and RouteFlag.FLAG_LOGIN != 0) {
            callback.onInterrupt(RuntimeException("need login"))
            showToast("请先登录")
        } else if (flag and RouteFlag.FLAG_AUTHENTICATION != 0) {
            callback.onInterrupt(RuntimeException("need_authentication"))
            showToast("请先实名认证")
        } else if (flag and RouteFlag.FLAG_VIP != 0) {
            callback.onInterrupt(RuntimeException("need_vip"))
            showToast("请先加入会员")
        } else {
            callback.onContinue(postcard)
        }
    }

    private fun showToast(s: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, s, Toast.LENGTH_LONG).show()
        }
    }

}