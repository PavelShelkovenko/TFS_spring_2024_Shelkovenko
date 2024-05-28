package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.interactors

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.UserRepository
import javax.inject.Inject

class GetAnotherProfileInteractor @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun getAnotherUserFromCache(userId: Int): User? {
        return userRepository.getAnotherProfileFromCache(userId)
    }

    suspend fun getAnotherUserFromNetwork(userId: Int): User {
        return userRepository.getAnotherProfileFromNetwork(userId)
    }
}