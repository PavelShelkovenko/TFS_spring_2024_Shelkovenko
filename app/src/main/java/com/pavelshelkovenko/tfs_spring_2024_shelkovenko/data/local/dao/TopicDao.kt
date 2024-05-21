package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.TopicDbo

@Dao
interface TopicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(topics: List<TopicDbo>)

    @Query("SELECT * FROM topics WHERE streamId = :streamId")
    suspend fun getTopicsForStream(streamId: Int): List<TopicDbo>

    @Query("DELETE FROM topics WHERE createdAt < :timeThreshold")
    suspend fun deleteOldTopics(timeThreshold: Long)
}