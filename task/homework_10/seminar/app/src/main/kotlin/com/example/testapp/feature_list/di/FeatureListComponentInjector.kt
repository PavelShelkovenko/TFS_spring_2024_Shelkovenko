package com.example.testapp.feature_list.di

object FeatureListComponentInjector {

    private var component: FeatureListComponent? = null

    fun initComponent(featureListDependencies: FeatureListDependencies): FeatureListComponent {
        return FeatureListComponent(featureListDependencies).also { component = it }
    }

    fun getComponent(): FeatureListComponent {
        return requireNotNull(component) { "FeatureComponent was not initialized" }
    }
}
