package com.finapp.finapp

import android.app.Application
import com.finapp.finapp.di.AppComponent
import com.finapp.finapp.di.DaggerAppComponent

class FinappApplication: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().context(this).application(this).build()
        appComponent.inject(this)
    }
}