package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.AppDatabase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.TopicDbo

@Dao
interface TopicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(topics: List<TopicDbo>)

    @Query("SELECT * FROM ${AppDatabase.TOPICS_TABLE_NAME} WHERE streamId = :streamId")
    suspend fun getTopicsForStream(streamId: Int): List<TopicDbo>

    @Query("DELETE FROM ${AppDatabase.TOPICS_TABLE_NAME} WHERE createdAt < :timeThreshold")
    suspend fun deleteOldTopics(timeThreshold: Long)
}