package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.AccountSettings
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.EmailVisibility

interface OwnSettingsRepository {

    suspend fun getAccountSetting(): AccountSettings

    suspend fun changeName(newName: String)

    suspend fun changeInvisibleMode(newInvisibleModeState: Boolean)

    suspend fun changeEmailVisibility(newEmailVisibility: EmailVisibility)
}