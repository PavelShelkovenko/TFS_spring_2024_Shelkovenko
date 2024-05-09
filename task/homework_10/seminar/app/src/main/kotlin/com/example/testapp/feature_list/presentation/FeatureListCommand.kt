package com.example.testapp.feature_list.presentation

sealed class FeatureListCommand {

    data class LoadList(val category: String) : FeatureListCommand()
}
