package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models

data class Stream(
    val id: Int,
    val name: String,
    val isExpanded: Boolean = false,
    val topicsList: List<Topic> = emptyList(),
    val subscriptionStatus: SubscriptionStatus = SubscriptionStatus.UNSUBSCRIBED,
)

enum class StreamDestination {
    ALL, SUBSCRIBED
}

enum class SubscriptionStatus {
    SUBSCRIBED, UNSUBSCRIBED
}