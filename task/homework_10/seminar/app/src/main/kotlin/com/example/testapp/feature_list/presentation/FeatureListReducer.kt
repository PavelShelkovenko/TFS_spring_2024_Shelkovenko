package com.example.testapp.feature_list.presentation

import com.example.testapp.feature_list.presentation.FeatureListCommand.LoadList
import com.example.testapp.feature_list.presentation.FeatureListEffect.OpenDetails
import com.example.testapp.feature_list.presentation.FeatureListEvent.FeatureListInternalEvent
import com.example.testapp.feature_list.presentation.FeatureListEvent.FeatureListInternalEvent.OnListLoadFailed
import com.example.testapp.feature_list.presentation.FeatureListEvent.FeatureListInternalEvent.OnListLoaded
import com.example.testapp.feature_list.presentation.FeatureListEvent.FeatureListUiEvent
import com.example.testapp.feature_list.presentation.FeatureListEvent.FeatureListUiEvent.OnButtonClick
import com.example.testapp.feature_list.presentation.FeatureListEvent.FeatureListUiEvent.OnItemClick
import com.example.testapp.feature_list.presentation.model.FeatureListStatus.CONTENT
import com.example.testapp.feature_list.presentation.model.FeatureListStatus.ERROR
import com.example.testapp.feature_list.presentation.model.FeatureListStatus.LOADING
import vivid.money.elmslie.core.store.dsl.DslReducer

class FeatureListReducer : DslReducer<FeatureListEvent, FeatureListState, FeatureListEffect, FeatureListCommand>() {

    override fun Result.reduce(event: FeatureListEvent) {
        when (event) {
            is FeatureListUiEvent -> reduceUiEvent(event)
            is FeatureListInternalEvent -> reduceInternalEvent(event)
        }
    }

    private fun Result.reduceUiEvent(event: FeatureListUiEvent) {
        when (event) {
            is OnButtonClick -> {
                state { copy(status = LOADING) }
                commands { +LoadList(category = event.category) }
            }
            is OnItemClick -> {
                effects { +OpenDetails(title = event.title) }
            }
        }
    }

    private fun Result.reduceInternalEvent(event: FeatureListInternalEvent) {
        when (event) {
            OnListLoadFailed -> {
                state { copy(status = ERROR) }
            }
            is OnListLoaded -> {
                state { copy(status = CONTENT, items = event.items) }
            }
        }
    }
}
