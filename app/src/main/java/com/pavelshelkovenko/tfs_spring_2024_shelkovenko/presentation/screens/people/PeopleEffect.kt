package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.people

sealed interface PeopleEffect {

    data class MinorError(val errorMessageId: Int): PeopleEffect
}