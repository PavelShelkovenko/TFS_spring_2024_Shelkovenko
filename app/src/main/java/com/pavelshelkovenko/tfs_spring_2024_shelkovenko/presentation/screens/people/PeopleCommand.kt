package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.people

sealed interface PeopleCommand {

    data object LoadDataFromNetwork: PeopleCommand

    data object LoadDataFromCache: PeopleCommand

    data class ProcessSearch(val query: String): PeopleCommand
}