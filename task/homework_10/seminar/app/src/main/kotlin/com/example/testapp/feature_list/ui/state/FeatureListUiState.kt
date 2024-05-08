package com.example.testapp.feature_list.ui.state

import com.example.testapp.feature_list.ui.recycler.CardItem

data class FeatureListUiState(
    val isLoading: Boolean,
    val isError: Boolean,
    val items: List<CardItem>
)
