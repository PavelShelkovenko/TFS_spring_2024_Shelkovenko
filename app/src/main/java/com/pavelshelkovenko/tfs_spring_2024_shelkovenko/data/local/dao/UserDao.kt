package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.UserDbo

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    suspend fun getAll(): List<UserDbo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserDbo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserDbo)

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): UserDbo?

}