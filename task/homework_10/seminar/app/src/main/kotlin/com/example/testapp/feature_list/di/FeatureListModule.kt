package com.example.testapp.feature_list.di

import com.example.testapp.domain_list.di.DomainListModule
import com.example.testapp.feature_list.presentation.FeatureListReducer
import com.example.testapp.feature_list.presentation.FeatureListStore
import com.example.testapp.feature_list.presentation.actor.FeatureListActor
import com.example.testapp.feature_list.presentation.actor.LoadListActorDelegate
import com.example.testapp.feature_list.ui.state.FeatureListUiStateMapper
import com.example.testapp.feature_list.ui.state.FeatureListUiStateMapperImpl

interface FeatureListModule {

    val featureListStore: FeatureListStore
    val featureListUiStateMapper: FeatureListUiStateMapper
}

fun FeatureListModule(domainListModule: DomainListModule): FeatureListModule {
    return object : FeatureListModule {

        private val reducer: FeatureListReducer
            get() = FeatureListReducer()

        private val actor: FeatureListActor
            get() = FeatureListActor(
                loadListActorDelegate = LoadListActorDelegate(domainListModule.coffeeRepository)
            )

        override val featureListStore: FeatureListStore
            get() = FeatureListStore(
                reducer = reducer,
                actor = actor
            )

        override val featureListUiStateMapper: FeatureListUiStateMapper
            get() = FeatureListUiStateMapperImpl()
    }
}
