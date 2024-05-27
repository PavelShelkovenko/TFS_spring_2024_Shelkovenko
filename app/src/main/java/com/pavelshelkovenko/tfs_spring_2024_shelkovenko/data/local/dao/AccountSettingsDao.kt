package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.AppDatabase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.AccountSettingsDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.EmailVisibility

@Dao
interface AccountSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(accountSettingsDbo: AccountSettingsDbo)

    @Query("SELECT * FROM ${AppDatabase.ACCOUNT_SETTINGS_TABLE_NAME} LIMIT 1")
    suspend fun getAccountSettings(): AccountSettingsDbo?

    @Query("UPDATE ${AppDatabase.ACCOUNT_SETTINGS_TABLE_NAME} SET isInvisibleMode = :newInvisibleModeState WHERE userId = :userId")
    suspend fun updateInvisibleModeState(userId: Int, newInvisibleModeState: Boolean)

    @Query("UPDATE ${AppDatabase.ACCOUNT_SETTINGS_TABLE_NAME} SET userName = :newUserName WHERE userId = :userId")
    suspend fun updateUserName(userId: Int, newUserName: String)

    @Query("UPDATE ${AppDatabase.ACCOUNT_SETTINGS_TABLE_NAME} SET emailVisibility = :newEmailVisibility WHERE userId = :userId")
    suspend fun updateEmailVisibility(userId: Int, newEmailVisibility: EmailVisibility)


}