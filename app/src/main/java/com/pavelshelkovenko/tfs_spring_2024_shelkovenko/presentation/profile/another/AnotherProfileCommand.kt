package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.another



sealed interface AnotherProfileCommand {

    data class LoadData(val userId: Int) : AnotherProfileCommand
}