package me.maxandroid.hilibrary

import android.app.Application
import com.google.gson.Gson
import me.maxandroid.hilibrary.log.HiConsolePrinter
import me.maxandroid.hilibrary.log.HiLogConfig
import me.maxandroid.hilibrary.log.HiLogManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        HiLogManager.init(object : HiLogConfig() {
            override fun injectParser(): JsonParser {
                return JsonParser { src ->
                    Gson().toJson(src)
                }
            }

            override fun getGlobalTag(): String {
                return this@MyApplication.javaClass.simpleName
            }

            override fun enable(): Boolean {
                return true
            }
        }, HiConsolePrinter())
    }
}