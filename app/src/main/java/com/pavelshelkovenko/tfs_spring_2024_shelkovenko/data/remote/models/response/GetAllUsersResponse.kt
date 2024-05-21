package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response

import com.google.gson.annotations.SerializedName
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.UserDto

data class GetAllUsersResponse(
    @SerializedName("members")
    val members: List<UserDto>
)