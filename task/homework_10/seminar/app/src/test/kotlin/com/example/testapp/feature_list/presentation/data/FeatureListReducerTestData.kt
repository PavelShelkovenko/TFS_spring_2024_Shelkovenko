package com.example.testapp.feature_list.presentation.data

import com.example.testapp.feature_list.presentation.FeatureListEvent.FeatureListUiEvent.OnButtonClick
import com.example.testapp.feature_list.presentation.FeatureListState

class FeatureListReducerTestData {

    val category = "category"

    val initialState = FeatureListState()

    val onButtonClickEvent = OnButtonClick(category)
}
