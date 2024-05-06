package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.UserRepository
import javax.inject.Inject

class GetAnotherProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun getAnotherUserFromCache(userId: Int): User? {
        return userRepository.getAnotherUserFromCache(userId)
    }

    suspend fun getAnotherUserFromNetwork(userId: Int): User {
        return userRepository.getAnotherUserFromNetwork(userId)
    }
}