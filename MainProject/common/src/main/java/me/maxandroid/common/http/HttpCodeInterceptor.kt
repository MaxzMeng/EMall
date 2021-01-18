package me.maxandroid.common.http

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import me.maxandroid.hilibrary.restful.HiInterceptor
import me.maxandroid.hilibrary.restful.HiResponse

/**
 * 根据response 的 code 自动路由到相关页面
 */
class HttpCodeInterceptor : HiInterceptor {
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        val response = chain.response();
        if (!chain.isRequestPeriod && response != null) {
            when (response.code) {
                HiResponse.RC_NEED_LOGIN -> {
                    ARouter.getInstance().build("/account/login").navigation()
                }
                //token过期
                //a | b
                HiResponse.RC_AUTH_TOKEN_EXPIRED, (HiResponse.RC_AUTH_TOKEN_INVALID), (HiResponse.RC_USER_FORBID) -> {
                    var helpUrl: String? = null
                    if (response.errorData != null) {
                        helpUrl = response.errorData!!["helpUrl"]
                    }

                    ARouter.getInstance().build("/degrade/global/activity")
                        .withString("degrade_title", "非法访问")
                        .withString("degrade_desc", response.msg)
                        .withString("degrade_action", helpUrl)
                        .navigation()
                }
            }
        }
        return false
    }

}