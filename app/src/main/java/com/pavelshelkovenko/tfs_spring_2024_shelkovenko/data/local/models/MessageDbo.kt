package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.AppDatabase

@Entity(tableName = AppDatabase.MESSAGES_TABLE_NAME)
data class MessageDbo(
    @PrimaryKey
    val id: Int,
    val avatarUrl: String,
    val message: String,
    val userId: Int,
    val userName: String,
    val dateInUTCSeconds: Int,
    val reactions: List<ReactionDbo>,
    val topicName: String,
    val streamName: String,
)