package com.syahna.appdicodingevent

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.syahna.appdicodingevent.databinding.ActivityMainBinding
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupActionBar()
        initializeBinding()
        configureNavigation()

        if (!checkInternetConnection()) {
            showNoConnectionToast()
        }
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@MainActivity, R.color.blue)))
            title = "Main"
            Log.d("MainActivity", "Action bar setup complete")
        }
    }

    private fun initializeBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun configureNavigation() {
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_upcoming, R.id.navigation_finished, R.id.navigation_favorite, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfig)
        binding.navView.setupWithNavController(navController)
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityService = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityService.activeNetwork ?: return false
        val capabilities = connectivityService.getNetworkCapabilities(network) ?: return false
        return capabilities.run {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }

    private fun showNoConnectionToast() {
        Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
    }
}