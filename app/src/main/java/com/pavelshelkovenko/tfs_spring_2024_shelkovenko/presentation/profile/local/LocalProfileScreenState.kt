package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.local

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.User

sealed interface LocalProfileScreenState {

    data object Initial : LocalProfileScreenState

    data object Loading : LocalProfileScreenState

    data class Error(val errorMessage: String) : LocalProfileScreenState

    data class Content(val localUser: User) : LocalProfileScreenState
}