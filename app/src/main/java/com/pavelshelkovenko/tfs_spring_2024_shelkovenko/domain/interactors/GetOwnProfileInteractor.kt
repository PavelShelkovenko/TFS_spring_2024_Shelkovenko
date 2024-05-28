package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.interactors

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.UserRepository
import javax.inject.Inject

class GetOwnProfileInteractor @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun getOwnProfileFromCache(): User? {
        return userRepository.getOwnProfileFromCache()
    }

    suspend fun getOwnProfileFromNetwork(): User {
        return userRepository.getOwnProfileFromNetwork()
    }

}