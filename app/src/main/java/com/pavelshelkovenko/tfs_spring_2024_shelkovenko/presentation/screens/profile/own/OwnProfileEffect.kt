package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own

sealed interface OwnProfileEffect {

    data class MinorError(val errorMessageId: Int): OwnProfileEffect
}