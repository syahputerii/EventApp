package com.syahna.appdicodingevent.ui.settings

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val pref = SettingPreferences.getInstance(dataStore)

        GlobalScope.launch {
            val isDarkModeActive = runBlocking { pref.getThemeSetting().first() }
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}