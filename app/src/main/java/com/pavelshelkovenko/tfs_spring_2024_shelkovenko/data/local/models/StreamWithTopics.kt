package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models

import androidx.room.Embedded
import androidx.room.Relation

data class StreamWithTopics(
    @Embedded val stream: StreamDbo,
    @Relation(
        parentColumn = "id",
        entityColumn = "streamId"
    )
    val topics: List<TopicDbo>
)