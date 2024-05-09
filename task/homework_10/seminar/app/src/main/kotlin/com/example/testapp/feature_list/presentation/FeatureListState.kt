package com.example.testapp.feature_list.presentation

import com.example.testapp.domain_list.model.Coffee
import com.example.testapp.feature_list.presentation.model.FeatureListStatus
import com.example.testapp.feature_list.presentation.model.FeatureListStatus.INITIAL

data class FeatureListState(
    val status: FeatureListStatus = INITIAL,
    val items: List<Coffee> = emptyList()
)
