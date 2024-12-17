package com.example.waterapp.application

import android.app.Application
import android.content.res.Configuration
import com.example.waterapp.utils.PreferenceManager
import dagger.hilt.android.HiltAndroidApp
import java.io.File

@HiltAndroidApp
class WaterApp: Application(){
    companion object {
        lateinit var encryptedPrefs: PreferenceManager
        lateinit var instance: WaterApp
    }

    override fun onCreate() {
        super.onCreate()
        encryptedPrefs = PreferenceManager(applicationContext)
        instance = this

        val dexOutputDir: File = codeCacheDir

        // Set the directory to be read-only
        dexOutputDir.setReadOnly()
    }

    fun isDarkThemeOn(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}