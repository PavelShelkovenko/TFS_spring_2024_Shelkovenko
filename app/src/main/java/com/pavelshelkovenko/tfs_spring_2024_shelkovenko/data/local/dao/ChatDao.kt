package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.MessageDbo

@Dao
interface ChatDao {
    @Query("SELECT * FROM messages WHERE streamName = :streamName AND topicName = :topicName")
    suspend fun getAll(streamName: String, topicName: String): List<MessageDbo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<MessageDbo>)

    @Query("DELETE FROM messages WHERE streamName = :streamName AND topicName = :topicName AND id NOT IN (SELECT id FROM messages  WHERE streamName = :streamName AND topicName = :topicName ORDER BY id DESC LIMIT 50)")
    suspend fun deleteOldMessages(streamName: String, topicName: String)

    @Query("UPDATE messages SET message = :newMessageContent WHERE id = :messageId")
    suspend fun updateMessage(messageId: Int, newMessageContent: String)

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteMessageById(id: Int)

    @Transaction
    suspend fun insertNewAndDeleteOldMessages(
        streamName: String,
        topicName: String,
        messages: List<MessageDbo>
    ) {
        insertAll(messages)
        deleteOldMessages(streamName, topicName)
    }
}