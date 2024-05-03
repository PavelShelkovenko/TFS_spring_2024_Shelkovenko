package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User

sealed interface OwnProfileEvent {

    sealed interface Ui : OwnProfileEvent {

        data object StartProcess : Ui

        data object ReloadData : Ui
    }

    sealed interface Internal : OwnProfileEvent {

        data class DataLoaded(val user: User) : Internal

        data class Error(val throwable: Throwable) : Internal
    }

}
