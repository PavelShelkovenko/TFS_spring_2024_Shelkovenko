package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.another

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.User

sealed interface AnotherProfileScreenState {

    data object Initial : AnotherProfileScreenState

    data object Loading : AnotherProfileScreenState

    data class Error(val errorMessage: String) : AnotherProfileScreenState

    data class Content(val anotherUser: User) : AnotherProfileScreenState
}