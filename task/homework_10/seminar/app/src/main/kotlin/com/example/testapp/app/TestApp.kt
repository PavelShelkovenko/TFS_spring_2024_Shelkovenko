package com.example.testapp.app

import android.app.Application
import com.example.testapp.di.AppComponentInjector
import com.example.testapp.feature_list.di.FeatureListComponentInjector

class TestApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initComponents()
    }

    private fun initComponents() {
        AppComponentInjector.initComponent().also { appComponent ->
            FeatureListComponentInjector.initComponent(appComponent)
        }
    }
}
