package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.another

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User

sealed interface AnotherProfileState {

    data object Initial : AnotherProfileState

    data object Loading : AnotherProfileState

    data class Error(val errorMessage: String) : AnotherProfileState

    data class Content(val anotherUser: User) : AnotherProfileState
}