package com.example.testapp.feature_list.presentation

sealed class FeatureListEffect {

    data class OpenDetails(val title: String) : FeatureListEffect()
}
