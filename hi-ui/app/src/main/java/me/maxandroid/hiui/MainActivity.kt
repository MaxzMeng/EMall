package me.maxandroid.hiui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import me.maxandroid.hilibrary.log.HiConsolePrinter
import me.maxandroid.hilibrary.log.HiLog
import me.maxandroid.hilibrary.log.HiLogConfig
import me.maxandroid.hilibrary.log.HiLogManager
import me.maxandroid.hiui.demo.HiRefreshDemoActivity
import me.maxandroid.hiui.demo.HiTabBottomDemoActivity
import me.maxandroid.hiui.demo.HiTabTopDemoActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        HiLogManager.init(
            object : HiLogConfig() {
                override fun injectParser(): JsonParser {
                    return JsonParser { src -> Gson().toJson(src) }
                }

                override fun getGlobalTag(): String {
                    return "MApplication"
                }

                override fun enable(): Boolean {
                    return true
                }

                override fun includeThread(): Boolean {
                    return true
                }

                override fun stackTraceDepth(): Int {
                    return 5
                }
            },
            HiConsolePrinter()
        )

        findViewById<TextView>(R.id.tv_hi_bottom).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_hi_top).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_hi_refresh).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_hi_bottom -> {
                startActivity(Intent(this, HiTabBottomDemoActivity::class.java))
            }
            R.id.tv_hi_top -> {
                startActivity(Intent(this, HiTabTopDemoActivity::class.java))
            }
            R.id.tv_hi_refresh -> {
                startActivity(Intent(this, HiRefreshDemoActivity::class.java))
            }
            else -> {
            }
        }
    }
}