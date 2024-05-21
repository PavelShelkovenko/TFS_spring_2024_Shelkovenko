package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.mappers

import android.graphics.Color
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.StreamDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.SubscriptionStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.TopicDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.StreamDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.TopicDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Topic

fun StreamDto.toStreamDomain(): Stream = Stream(id = this.id, name = this.streamName)

fun Stream.toStreamDbo(subscriptionStatus: SubscriptionStatus): StreamDbo = StreamDbo(
    id = id,
    streamName = this.name,
    subscriptionStatus = subscriptionStatus
)

fun StreamDbo.toStreamDomain(): Stream = Stream(
    id = this.id,
    name = this.streamName,
)

fun TopicDto.toTopicDbo(streamId: Int): TopicDbo = TopicDbo(
    streamId = streamId,
    lastMessageId = this.lastMessageId,
    name = this.topicName,
    color = TopicColorProvider.provideColor(),
)

fun Topic.toTopicDbo(streamId: Int): TopicDbo = TopicDbo(
    topicId = this.id,
    name = this.name,
    streamId = streamId,
    color = this.color,
    lastMessageId = this.lastMessageId
)

fun TopicDbo.toTopicDomain(): Topic = Topic(
    id = this.topicId,
    name = this.name,
    lastMessageId = this.lastMessageId,
    color = this.color
)

fun List<Topic>.toTopicDboList(streamId: Int): List<TopicDbo> {
    return this.map { it.toTopicDbo(streamId) }
}

fun List<TopicDbo>.toTopicDomainList(): List<Topic> {
    return this.map { it.toTopicDomain() }
}



object TopicColorProvider {

    private var currentColor = TopicColor.YELLOW

    fun provideColor(): Int {
        return when(currentColor) {
            TopicColor.GREEN -> {
                currentColor = TopicColor.YELLOW
                Color.argb(255,42, 157, 143)
            }
            TopicColor.YELLOW -> {
                currentColor = TopicColor.GREEN
                Color.argb(255,233, 196, 106,)
            }
        }
    }
}

enum class TopicColor {
    GREEN, YELLOW
}