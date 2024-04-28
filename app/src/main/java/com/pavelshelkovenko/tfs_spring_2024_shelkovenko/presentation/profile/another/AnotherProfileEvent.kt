package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.another

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User

sealed interface AnotherProfileEvent {
    sealed interface Ui : AnotherProfileEvent {
        data class Init(val userId: Int) : Ui

        data class ReloadData(val userId: Int) : Ui
    }

    sealed interface Internal : AnotherProfileEvent {

        data class DataLoaded(val user: User) : Internal

        data class Error(val throwable: Throwable) : Internal
    }
}