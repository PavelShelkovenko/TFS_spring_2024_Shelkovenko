package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.ReactionDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.TopicDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.EmailVisibility
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.SubscriptionStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.UserOnlineStatus

class Converters {

    @TypeConverter
    fun toSubscriptionStatus(value: String): SubscriptionStatus = SubscriptionStatus.valueOf(value)

    @TypeConverter
    fun fromSubscriptionStatus(status: SubscriptionStatus): String = status.name

    @TypeConverter
    fun toUserOnlineStatus(value: String): UserOnlineStatus = UserOnlineStatus.valueOf(value)

    @TypeConverter
    fun toTopicList(topics: String): List<TopicDbo> {
        val listType = object : TypeToken<List<TopicDbo>>() {}.type
        return Gson().fromJson(topics, listType)
    }

    @TypeConverter
    fun fromTopicList(list: List<TopicDbo>): String = Gson().toJson(list)

    @TypeConverter
    fun fromUserOnlineStatus(status: UserOnlineStatus): String = status.name

    @TypeConverter
    fun toReactionList(reaction: String): List<ReactionDbo> {
        val listType = object : TypeToken<List<ReactionDbo>>() {}.type
        return Gson().fromJson(reaction, listType)
    }

    @TypeConverter
    fun fromReactionList(list: List<ReactionDbo>): String = Gson().toJson(list)

    @TypeConverter
    fun fromEmailVisibility(emailVisibility: EmailVisibility): String = emailVisibility.name

    @TypeConverter
    fun toEmailVisibility(value: String): EmailVisibility = EmailVisibility.valueOf(value)
}