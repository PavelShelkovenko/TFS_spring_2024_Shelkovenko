package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.repository

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.AccountInfo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.AccountSettingsDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.mappers.toAccountSetting
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.ZulipApi
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.AccountSettings
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.EmailVisibility
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.OwnSettingsRepository
import javax.inject.Inject

class OwnSettingsRepositoryImpl @Inject constructor(
    private val zulipApi: ZulipApi,
    private val accountSettingsDao: AccountSettingsDao,
    private val accountInfo: AccountInfo,
) : OwnSettingsRepository {

    override suspend fun getAccountSetting(): AccountSettings {
        val accountSettingsDbo = accountSettingsDao.getAccountSettings()
            ?: throw IllegalStateException("AccountSetting cache is empty")
        return accountSettingsDbo.toAccountSetting()
    }

    override suspend fun changeName(newName: String) {
        zulipApi.updateOwnUserName(newName)
        accountSettingsDao.updateUserName(
            accountInfo.userId,
            newUserName = newName
        )
    }

    override suspend fun changeInvisibleMode(newInvisibleModeState: Boolean) {
        zulipApi.updateInvisibleMode(newInvisibleModeState)
        accountSettingsDao.updateInvisibleModeState(
            accountInfo.userId,
            newInvisibleModeState = !newInvisibleModeState
        )
    }

    override suspend fun changeEmailVisibility(newEmailVisibility: EmailVisibility) {
        val emailVisibilityRequest = when(newEmailVisibility) {
            EmailVisibility.EVERYONE -> 1
            EmailVisibility.MEMBERS -> 2
            EmailVisibility.ADMINS -> 3
            EmailVisibility.NOBODY -> 4
        }
        zulipApi.updateEmailVisibility(emailVisibilityRequest)
        accountSettingsDao.updateEmailVisibility(
            accountInfo.userId,
            newEmailVisibility = newEmailVisibility
        )
    }
}