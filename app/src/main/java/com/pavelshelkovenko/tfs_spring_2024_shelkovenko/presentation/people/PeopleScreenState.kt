package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.people

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.User

sealed interface PeopleScreenState {

    data object Initial : PeopleScreenState

    data object Loading : PeopleScreenState

    data class Error(val errorMessage: String) : PeopleScreenState

    data class Content(val userList: List<User>) : PeopleScreenState
}