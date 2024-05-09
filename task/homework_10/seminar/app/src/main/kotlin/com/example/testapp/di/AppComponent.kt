package com.example.testapp.di

import com.example.testapp.feature_list.di.FeatureListDependencies

interface AppComponent : NetworkModule, FeatureListDependencies

fun AppComponent(): AppComponent {
    return object : AppComponent,
        NetworkModule by NetworkModule() {}
}
