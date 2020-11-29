package me.maxandroid.hilibrary.demo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import me.maxandroid.hilibrary.R
import me.maxandroid.hilibrary.log.*

class HiLogDemoActivity : AppCompatActivity() {

    private val viewPrinter by lazy { HiViewPrinter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_log_demo)

        HiLogManager.getInstance().addPrinter(viewPrinter)
        findViewById<Button>(R.id.btn_log).setOnClickListener {
            printLog()
        }

        viewPrinter.viewProvider.showFloatingView()
    }

    private fun printLog() {
        HiLog.log(object : HiLogConfig() {
            override fun includeThread() = true

            override fun stackTraceDepth() = 0
        }, HiLogType.E, "----", "5566")

        HiLog.a("test")
    }
}