package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User

interface UserRepository {

    suspend fun getAllUsersFromNetwork(): List<User>

    suspend fun getAllUsersFromCache(): List<User>

    suspend fun getOwnProfileFromNetwork(): User

    suspend fun getOwnProfileFromCache(): User?

    suspend fun getAnotherProfileFromNetwork(userId: Int): User

    suspend fun getAnotherProfileFromCache(userId: Int): User?

    suspend fun searchUsers(query: String): List<User>
}