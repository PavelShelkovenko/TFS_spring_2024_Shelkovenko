package com.example.testapp.feature_list.di

import com.example.testapp.domain_list.di.DomainListModule
import com.example.testapp.feature_list.presentation.FeatureListStore
import com.example.testapp.feature_list.ui.state.FeatureListUiStateMapper

interface FeatureListComponent {

    val featureListStore: FeatureListStore
    val featureListUiStateMapper: FeatureListUiStateMapper
}

fun FeatureListComponent(featureListDependencies: FeatureListDependencies): FeatureListComponent {
    val domainListModule = DomainListModule(featureListDependencies)
    return object : FeatureListComponent,
        DomainListModule by domainListModule,
        FeatureListModule by FeatureListModule(domainListModule) {}
}
