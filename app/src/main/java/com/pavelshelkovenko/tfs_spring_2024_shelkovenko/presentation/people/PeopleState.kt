package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.people

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User

sealed interface PeopleState {
    data object Initial : PeopleState

    data object Loading : PeopleState

    data class Error(val errorMessage: String) : PeopleState

    data class Content(val userList: List<User>) : PeopleState
}

