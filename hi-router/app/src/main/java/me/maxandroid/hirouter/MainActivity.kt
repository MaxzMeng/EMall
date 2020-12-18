package me.maxandroid.hirouter

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import me.maxandroid.hirouter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController: NavController = findNavController(R.id.nav_host_fragment_activity_main)
        val hostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)

        NavUtil.buildNavGraph(this, hostFragment?.childFragmentManager, navController, R.id.nav_host_fragment_activity_main)
        NavUtil.builderBottomBar(navView)

        navView.setOnNavigationItemSelectedListener { item ->
            navController.navigate(item.itemId)
            true
        }

        navView.setupWithNavController(navController)
    }
}