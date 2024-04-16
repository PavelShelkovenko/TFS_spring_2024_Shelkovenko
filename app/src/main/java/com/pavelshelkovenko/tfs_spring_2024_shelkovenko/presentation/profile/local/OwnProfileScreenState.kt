package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.local

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User

sealed interface OwnProfileScreenState {

    data object Initial : OwnProfileScreenState

    data object Loading : OwnProfileScreenState

    data class Error(val errorMessage: String) : OwnProfileScreenState

    data class Content(val ownUser: User) : OwnProfileScreenState
}