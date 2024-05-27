package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.AppDatabase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.StreamDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.StreamWithTopics
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.SubscriptionStatus

@Dao
interface StreamDao {

    @Query("SELECT * FROM ${AppDatabase.STREAMS_TABLE_NAME}")
    suspend fun getAllStreams(): List<StreamDbo>

    @Query("SELECT * FROM ${AppDatabase.STREAMS_TABLE_NAME} WHERE subscriptionStatus = :status")
    suspend fun getSubscribedStreams(status: String): List<StreamDbo>

    @Query("UPDATE ${AppDatabase.STREAMS_TABLE_NAME} SET subscriptionStatus = :status WHERE id = :id")
    suspend fun updateSubscriptionStatus(id: Int, status: SubscriptionStatus)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(streams: List<StreamDbo>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(stream: StreamDbo): Long

    @Transaction
    @Query("SELECT * FROM ${AppDatabase.STREAMS_TABLE_NAME} WHERE id = :streamId")
    suspend fun getStreamWithTopics(streamId: Int): StreamWithTopics
}