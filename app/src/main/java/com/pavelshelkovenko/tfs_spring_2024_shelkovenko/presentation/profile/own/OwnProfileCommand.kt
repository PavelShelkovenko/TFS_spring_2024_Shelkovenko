package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.own

sealed interface OwnProfileCommand {

    data object LoadData : OwnProfileCommand
}