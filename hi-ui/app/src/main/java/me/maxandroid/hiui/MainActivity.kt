package me.maxandroid.hiui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import me.maxandroid.hiui.demo.HiTabBottomDemoActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_hi_bottom).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_hi_bottom -> {
                startActivity(Intent(this, HiTabBottomDemoActivity::class.java))
            }
            else -> {
            }
        }
    }
}