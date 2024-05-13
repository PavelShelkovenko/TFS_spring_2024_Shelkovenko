package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response

import com.google.gson.annotations.SerializedName
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.UserDto
import java.io.Serializable

data class GetUserResponse(
    @SerializedName("user")
    val user: UserDto
): Serializable