package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecases


import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Topic
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.StreamRepository
import javax.inject.Inject

class GetTopicsForStreamByIdUseCase @Inject constructor(
    private val streamRepository: StreamRepository
) {

    suspend operator fun invoke(streamId: Int): List<Topic> {
       return streamRepository.getTopicsForStreamById(streamId)
    }
}