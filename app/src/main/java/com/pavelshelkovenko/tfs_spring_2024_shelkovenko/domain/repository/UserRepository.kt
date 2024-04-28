package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User

interface UserRepository {

    suspend fun getAllUsers(): List<User>

    suspend fun getOwnProfile(): User

    suspend fun getAnotherUser(userId: Int): User

    suspend fun searchUsers(query: String): List<User>
}