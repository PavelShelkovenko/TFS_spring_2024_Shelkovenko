package com.example.testapp.feature_list.ui.state

import com.example.testapp.domain_list.model.Coffee
import com.example.testapp.feature_list.presentation.FeatureListState
import com.example.testapp.feature_list.presentation.model.FeatureListStatus.ERROR
import com.example.testapp.feature_list.presentation.model.FeatureListStatus.LOADING
import com.example.testapp.feature_list.ui.recycler.CardItem

interface FeatureListUiStateMapper : (FeatureListState) -> FeatureListUiState

class FeatureListUiStateMapperImpl : FeatureListUiStateMapper {

    override fun invoke(state: FeatureListState): FeatureListUiState {
        return FeatureListUiState(
            isLoading = state.status == LOADING,
            isError = state.status == ERROR,
            items = getItems(state.items)
        )
    }

    private fun getItems(items: List<Coffee>): List<CardItem> {
        return items.map { item ->
            CardItem(
                id = item.id,
                title = item.title,
                description = item.description
            )
        }
    }
}
