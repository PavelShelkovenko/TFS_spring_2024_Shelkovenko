package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own.settings

sealed interface OwnSettingsEffect {

    data class MinorError(val errorMessageId: Int): OwnSettingsEffect
}