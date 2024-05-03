package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.people

sealed interface PeopleCommand {

    data object LoadData: PeopleCommand

    data class ProcessSearch(val query: String): PeopleCommand
}