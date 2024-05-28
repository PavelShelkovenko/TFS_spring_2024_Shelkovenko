package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.people

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User

sealed interface PeopleEvent {

    sealed interface Ui: PeopleEvent {

        data object StartProcess : Ui

        data class QueryChanged(val newQuery: String): Ui

        data class ReloadData(val currentQuery: String) : Ui
    }

    sealed interface Internal: PeopleEvent {

        data class DataLoadedFromNetwork(val users: List<User>) : Internal

        data class DataLoadedFromCache(val users: List<User>) : Internal

        data class Error(val errorMessageId: Int) : Internal

        data class MinorError(val errorMessageId: Int) : Internal

        data object ErrorLoadingFromCache : Internal

        data class SearchError(val errorMessageId: Int) : Internal
    }
}