package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.another



sealed interface AnotherProfileCommand {

    data class LoadDataFromNetwork(val userId: Int) : AnotherProfileCommand

    data class LoadDataFromCache(val userId: Int) : AnotherProfileCommand
}