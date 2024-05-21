package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.AppDatabase

@Entity(
    tableName = AppDatabase.TOPICS_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = StreamDbo::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("streamId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TopicDbo(
    @PrimaryKey(autoGenerate = true)
    val topicId: Int = 0,
    val streamId: Int,
    val name: String,
    val lastMessageId: Int,
    val color: Int,
    val createdAt: Long = System.currentTimeMillis()
)