package com.example.testapp.di

object AppComponentInjector {

    private var component: AppComponent? = null

    fun initComponent(): AppComponent {
        return AppComponent().also { component = it }
    }

    fun getComponent(): AppComponent {
        return requireNotNull(component) { "AppComponent was not initialized" }
    }
}
