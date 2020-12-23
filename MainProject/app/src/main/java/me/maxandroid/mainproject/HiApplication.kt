package me.maxandroid.mainproject

import com.alibaba.android.arouter.launcher.ARouter
import me.maxandroid.common.ui.compoment.HiBaseApplication

class HiApplication : HiBaseApplication() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }
}