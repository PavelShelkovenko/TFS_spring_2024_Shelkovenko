package com.example.testapp.feature_list.presentation

import com.example.testapp.feature_list.presentation.actor.FeatureListActor
import vivid.money.elmslie.core.store.ElmStore

typealias FeatureListStore = ElmStore<FeatureListEvent, FeatureListState, FeatureListEffect, FeatureListCommand>

fun FeatureListStore(
    reducer: FeatureListReducer,
    actor: FeatureListActor
) = FeatureListStore(
    initialState = FeatureListState(),
    reducer = reducer,
    actor = actor
)
