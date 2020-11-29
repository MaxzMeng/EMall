package me.maxandroid.mainproject

import android.os.Bundle
import me.maxandroid.common.ui.compoment.HiBaseActivity

class MainActivity : HiBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}