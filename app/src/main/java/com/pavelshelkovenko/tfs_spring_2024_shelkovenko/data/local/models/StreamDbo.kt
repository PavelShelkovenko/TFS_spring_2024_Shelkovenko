package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.AppDatabase

@Entity(tableName = AppDatabase.STREAMS_TABLE_NAME)
data class StreamDbo(
    @PrimaryKey
    val id: Int,
    val streamName: String,
    val subscriptionStatus: SubscriptionStatus,
)

enum class SubscriptionStatus {
    SUBSCRIBED, UNSUBSCRIBED
}