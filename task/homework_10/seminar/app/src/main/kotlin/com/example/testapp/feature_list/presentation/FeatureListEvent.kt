package com.example.testapp.feature_list.presentation

import com.example.testapp.domain_list.model.Coffee

sealed class FeatureListEvent {

    sealed class FeatureListUiEvent : FeatureListEvent() {

        data class OnButtonClick(val category: String) : FeatureListUiEvent()
        data class OnItemClick(val title: String) : FeatureListUiEvent()
    }

    sealed class FeatureListInternalEvent : FeatureListEvent() {

        data class OnListLoaded(val items: List<Coffee>) : FeatureListInternalEvent()
        data object OnListLoadFailed : FeatureListInternalEvent()
    }
}
