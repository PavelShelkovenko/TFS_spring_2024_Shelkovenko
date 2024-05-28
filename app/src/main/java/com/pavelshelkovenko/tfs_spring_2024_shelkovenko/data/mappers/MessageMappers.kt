package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.mappers

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.AccountInfo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.MessageDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.ReactionDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.MessageDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.OperationDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.ReactionDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Message
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Reaction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.Operation

fun MessageDto.toMessageDomain(accountInfo: AccountInfo): Message = Message(
    id = this.id,
    avatarUrl = this.avatarUrl,
    message = this.message,
    userId = this.userId,
    userName = this.userName,
    dateInUTCSeconds = this.dateInUTCSeconds,
    reactions = this.reactions.mapReactionDtoToReactionDomain(accountInfo)
)

fun MessageDbo.toMessageDomain(): Message = Message(
    id = this.id,
    avatarUrl = this.avatarUrl,
    message = this.message,
    userId = this.userId,
    userName = this.userName,
    dateInUTCSeconds = this.dateInUTCSeconds,
    reactions = this.reactions.mapReactionDboToReactionDomain()
)

fun Message.toMessageDbo(streamName: String, topicName: String): MessageDbo = MessageDbo(
    id = this.id,
    avatarUrl = this.avatarUrl,
    message = this.message,
    userId = this.userId,
    userName = this.userName,
    dateInUTCSeconds = this.dateInUTCSeconds,
    reactions = this.reactions.mapReactionDomainToReactionDbo(),
    streamName = streamName,
    topicName = topicName
)


fun List<ReactionDto>.mapReactionDtoToReactionDomain(accountInfo: AccountInfo): List<Reaction> {
    return this.groupBy { it.emojiCode }
        .map { (emojiCode, list) ->
            val userId =
                list.findLast { it.userId == accountInfo.userId }?.userId ?: list.last().userId
            val emojiName = list.first().emojiName
            Reaction(
                emojiCode = emojiCode,
                emojiName = emojiName,
                userId = userId,
                count = list.size
            )
        }
}

fun List<ReactionDbo>.mapReactionDboToReactionDomain(): List<Reaction> =
    this.map { it.toReactionDomain() }

fun List<Reaction>.mapReactionDomainToReactionDbo(): List<ReactionDbo> =
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