package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.MessageDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.ReactionDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.StreamDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.SubscriptionStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.TopicDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.UserDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.MessageDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.OperationDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.ReactionDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.StreamDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.TopicDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.UserDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.UserOnlineStatusDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Message
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Reaction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Topic
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.UserOnlineStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.Operation
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.MyUserId
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.generateRandomId

fun UserDto.toUserDomain(
    onlineStatus: UserOnlineStatus = UserOnlineStatus.OFFLINE
): User = User(
    id = this.id,
    avatarUrl = this.avatarUrl,
    name = this.userName,
    email = this.email ?: "User hide his email",
    onlineStatus = onlineStatus
)

fun UserDto.toUserDbo(
    onlineStatus: UserOnlineStatus = UserOnlineStatus.OFFLINE
): UserDbo = UserDbo(
    id = this.id,
    avatarUrl = this.avatarUrl,
    name = this.userName,
    email = this.email ?: "User hide his email",
    onlineStatus = onlineStatus
)

fun UserDbo.toUserDomain(): User = User(
    id = this.id,
    avatarUrl = this.avatarUrl,
    name = this.name,
    email = this.email,
    onlineStatus = this.onlineStatus
)

fun User.toUserDbo(): UserDbo = UserDbo(
    id = this.id,
    avatarUrl = this.avatarUrl,
    name = this.name,
    email = this.email,
    onlineStatus = this.onlineStatus
)

fun UserOnlineStatusDto.toUserOnlineStatusDomain(): UserOnlineStatus = when (this) {
    UserOnlineStatusDto.ACTIVE -> UserOnlineStatus.ACTIVE
    UserOnlineStatusDto.IDLE -> UserOnlineStatus.IDLE
    UserOnlineStatusDto.OFFLINE -> UserOnlineStatus.OFFLINE
}

fun MessageDto.toMessageDomain(): Message = Message(
    id = this.id,
    avatarUrl = this.avatarUrl,
    message = this.message,
    userId = this.userId,
    userName = this.userName,
    dateInUTCSeconds = this.dateInUTCSeconds,
    reactions = this.reactions.mapFromReactionDtoToReactionDomain()
)

fun MessageDbo.toMessageDomain(): Message = Message(
    id = this.id,
    avatarUrl = this.avatarUrl,
    message = this.message,
    userId = this.userId,
    userName = this.userName,
    dateInUTCSeconds = this.dateInUTCSeconds,
    reactions = this.reactions.mapFromReactionDboToReactionDomain()
)

fun Message.toMessageDbo(streamName: String, topicName: String): MessageDbo = MessageDbo(
    id = this.id,
    avatarUrl = this.avatarUrl,
    message = this.message,
    userId = this.userId,
    userName = this.userName,
    dateInUTCSeconds = this.dateInUTCSeconds,
    reactions = this.reactions.mapFromReactionDomainToReactionDbo(),
    streamName = streamName,
    topicName = topicName
)


fun List<ReactionDto>.mapFromReactionDtoToReactionDomain(): List<Reaction> {
    return this.groupBy { it.emojiCode }
        .map { (emojiCode, list) ->
            val userId =
                list.findLast { it.userId == MyUserId.MY_USER_ID }?.userId ?: list.last().userId
            val emojiName = list.first().emojiName
            Reaction(
                emojiCode = emojiCode,
                emojiName = emojiName,
                userId = userId,
                count = list.size
            )
        }
}

fun List<ReactionDbo>.mapFromReactionDboToReactionDomain(): List<Reaction> =
    this.map { it.toReactionDomain() }

fun List<Reaction>.mapFromReactionDomainToReactionDbo(): List<ReactionDbo> =
    this.map { it.toReactionDbo() }

fun ReactionDbo.toReactionDomain(): Reaction = Reaction(
    userId = this.userId,
    emojiCode = this.emojiCode,
    emojiName = this.emojiName,
    count = this.count
)

fun Reaction.toReactionDbo(): ReactionDbo = ReactionDbo(
    userId = this.userId,
    emojiCode = this.emojiCode,
    emojiName = this.emojiName,
    count = this.count
)

fun OperationDto.toOperation(): Operation = when (this) {
    OperationDto.ADD -> Operation.ADD
    OperationDto.REMOVE -> Operation.REMOVE
}

fun StreamDto.toStreamDomain(): Stream = Stream(id = this.id, name = this.streamName)

fun Stream.toStreamDbo(subscriptionStatus: SubscriptionStatus): StreamDbo = StreamDbo(
    id = id,
    streamName = this.name,
    topics = this.topicsList.toTopicDboList(),
    subscriptionStatus = subscriptionStatus
)

fun StreamDbo.toStreamDomain(): Stream = Stream(
    id = this.id,
    name = this.streamName,
    topicsList = this.topics.toTopicDomainList()
)

fun TopicDto.toTopicDomain(): Topic = Topic(
    id = generateRandomId(),
    name = this.topicName,
    lastMessageId = this.lastMessageId,
    color = TopicColorProvider.provideColor()
)

fun Topic.toTopicDbo(): TopicDbo = TopicDbo(
    id = this.id,
    name = this.name,
    lastMessageId = this.lastMessageId
)

fun List<Topic>.toTopicDboList(): List<TopicDbo> {
    return this.map { it.toTopicDbo() }
}

fun List<TopicDbo>.toTopicDomainList(): List<Topic> {
    return this.map { it.toTopicDomain() }
}

fun TopicDbo.toTopicDomain(): Topic = Topic(
    id = this.id,
    name = this.name,
    lastMessageId = this.lastMessageId,
    color = TopicColorProvider.provideColor()
)

