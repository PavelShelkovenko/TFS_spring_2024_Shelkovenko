package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.UserRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User

class GetAnotherProfileUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(userId: Int): User {
        return userRepository.getAnotherUser(userId = userId)
    }
}