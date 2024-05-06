package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.StreamDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.SubscriptionStatus

@Dao
interface StreamDao {

    @Query("SELECT * FROM streams")
    suspend fun getAllStreams(): List<StreamDbo>

    @Query("SELECT * FROM streams WHERE subscriptionStatus = :status")
    suspend fun getSubscribedStreams(status: String): List<StreamDbo>

    @Query("UPDATE streams SET subscriptionStatus = :status WHERE id = :id")
    suspend fun updateSubscriptionStatus(id: Int, status: SubscriptionStatus)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(streams: List<StreamDbo>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(stream: StreamDbo): Long
}