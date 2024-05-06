package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class PresenceDto(
    @SerializedName("aggregated")
    val aggregated: Aggregated,
): Serializable


data class Aggregated(
    @SerializedName("status")
    val userOnlineStatusDto: UserOnlineStatusDto,
    @SerializedName("timestamp")
    val timestamp: Int,
): Serializable


enum class UserOnlineStatusDto {
    @SerializedName("active")
    ACTIVE,
    @SerializedName("idle")
    IDLE,
    @SerializedName("offline")
    OFFLINE,
}