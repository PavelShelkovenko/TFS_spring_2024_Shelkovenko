package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.dto.MessageDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.dto.OperationDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.dto.ReactionDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.dto.StreamDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.dto.TopicDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.dto.UserDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.dto.UserOnlineStatusDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Message
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Reaction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Topic
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.UserOnlineStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.Operation
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.generateRandomColor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.generateRandomId

fun UserDto.toUser(): User = User(
    id = this.id,
    avatarUrl = this.avatarUrl,
    name = this.userName,
    email = this.email,
)


fun UserOnlineStatusDto.toUserStatus(): UserOnlineStatus = when (this) {
    UserOnlineStatusDto.ACTIVE -> UserOnlineStatus.ACTIVE
    UserOnlineStatusDto.IDLE -> UserOnlineStatus.IDLE
    UserOnlineStatusDto.OFFLINE -> UserOnlineStatus.OFFLINE
}

fun StreamDto.toStream(): Stream = Stream(id = this.id, name = this.streamName)

fun TopicDto.toTopic(): Topic = Topic(
    id = generateRandomId(),
    name = this.topicName,
    lastMessageId = this.lastMessageId,
    color = generateRandomColor()
)


fun MessageDto.toMessage(): Message = Message(
    id = this.id,
    avatarUrl = this.avatarUrl,
    message = this.message,
    userId = this.userId,
    userName = this.userName,
    dateInUTCSeconds = this.dateInUTCSeconds,
    reactions = this.reactions.toReaction(MyUserId.MY_USER_ID)
)


fun List<ReactionDto>.toReaction(myUserId: Int): List<Reaction> {
    return this.groupBy { it.emojiCode }
        .map { (emojiCode, list) ->
            val userId = list.findLast { it.userId == myUserId }?.userId ?: list.last().userId
            val emojiName = list.first().emojiName
            Reaction(
                emojiCode = emojiCode,
                emojiName = emojiName,
                userId = userId,
                count = list.size
            )
        }
}

fun OperationDto.toOperation(): Operation = when (this) {
    OperationDto.ADD -> Operation.ADD
    OperationDto.REMOVE -> Operation.REMOVE
}