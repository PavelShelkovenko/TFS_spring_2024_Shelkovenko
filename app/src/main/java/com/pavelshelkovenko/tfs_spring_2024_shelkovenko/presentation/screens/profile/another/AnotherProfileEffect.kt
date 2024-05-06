package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.another

sealed interface AnotherProfileEffect {

    data class MinorError(val errorMessageId: Int): AnotherProfileEffect
}