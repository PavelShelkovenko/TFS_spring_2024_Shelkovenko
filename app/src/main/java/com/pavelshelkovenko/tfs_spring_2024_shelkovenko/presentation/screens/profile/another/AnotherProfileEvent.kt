package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.another

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User

sealed interface AnotherProfileEvent {
    sealed interface Ui : AnotherProfileEvent {

        data class StartProcess(val userId: Int) : Ui

        data class ReloadData(val userId: Int) : Ui
    }

    sealed interface Internal : AnotherProfileEvent {

        data class DataLoadedFromNetwork(val user: User) : Internal

        data class DataLoadedFromCache(val user: User?, val userId: Int) : Internal

        data class Error(val errorMessageId: Int) : Internal

        data class ErrorLoadingFromCache(val userId: Int) : Internal

    }
}