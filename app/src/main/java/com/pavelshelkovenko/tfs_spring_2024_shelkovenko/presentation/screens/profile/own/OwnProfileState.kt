package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User

sealed interface OwnProfileState {

    data object Initial : OwnProfileState

    data object Loading : OwnProfileState

    data class Error(val errorMessage: String) : OwnProfileState

    data class Content(val ownUser: User) : OwnProfileState
}
