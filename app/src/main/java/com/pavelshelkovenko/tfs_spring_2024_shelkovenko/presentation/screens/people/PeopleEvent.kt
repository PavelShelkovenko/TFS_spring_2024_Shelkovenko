package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.people

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User

sealed interface PeopleEvent {

    sealed interface Ui: PeopleEvent {

        data object StartProcess : Ui

        data class QueryChanged(val newQuery: String): Ui

        data object ReloadData : Ui
    }

    sealed interface Internal: PeopleEvent {

        data class DataLoaded(val users: List<User>) : Internal

        data class Error(val throwable: Throwable) : Internal
    }
}