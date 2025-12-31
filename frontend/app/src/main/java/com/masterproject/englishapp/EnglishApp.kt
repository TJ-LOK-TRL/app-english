package com.masterproject.englishapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EnglishApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
