package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.UserRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User

class GetOwnProfileUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): User {
        return userRepository.getOwnProfile()
    }
}