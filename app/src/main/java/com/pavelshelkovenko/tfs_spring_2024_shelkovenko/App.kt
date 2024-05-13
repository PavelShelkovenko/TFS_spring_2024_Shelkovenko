package com.pavelshelkovenko.tfs_spring_2024_shelkovenko

import android.app.Application
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.components.DaggerApplicationComponent

class App: Application() {

    val appComponent by lazy {
        DaggerApplicationComponent.factory().create(this@App)
    }
}