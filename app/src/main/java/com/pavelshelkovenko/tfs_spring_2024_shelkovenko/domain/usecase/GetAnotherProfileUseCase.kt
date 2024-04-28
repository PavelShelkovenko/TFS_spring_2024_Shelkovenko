package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.UserRepository
import javax.inject.Inject

class GetAnotherProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(userId: Int): User {
        return userRepository.getAnotherUser(userId = userId)
    }
}