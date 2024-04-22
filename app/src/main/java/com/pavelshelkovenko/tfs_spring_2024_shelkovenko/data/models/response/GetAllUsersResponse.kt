package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.response

import com.google.gson.annotations.SerializedName
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.dto.UserDto
import java.io.Serializable

data class GetAllUsersResponse(
    @SerializedName("members")
    val members: List<UserDto>
): Serializable