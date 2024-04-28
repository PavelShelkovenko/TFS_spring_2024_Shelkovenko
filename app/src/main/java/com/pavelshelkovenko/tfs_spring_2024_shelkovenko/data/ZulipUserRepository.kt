package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.toUser
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.toUserStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.UserOnlineStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.UserRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.containsQuery
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class ZulipUserRepository @Inject constructor(
    private val zulipApi: ZulipApi
) : UserRepository {

    override suspend fun getAllUsers(): List<User> {
        val response = zulipApi.getAllUsers()
        val users = response.members.map { userDto ->
            userDto.toUser()
        }
        val allUsersOnlineStatus = zulipApi.getAllUsersPresence().presences
        val usersWithOnlineStatus = users.map { user ->
            try {
                val emailKey = allUsersOnlineStatus.keys.first { it == user.email }
                val lastSeenTime = allUsersOnlineStatus[emailKey]?.aggregated?.timestamp?.toLong() ?: 0L

                var statusValue =
                    allUsersOnlineStatus[emailKey]?.aggregated?.userOnlineStatusDto?.toUserStatus()
                        ?: UserOnlineStatus.OFFLINE

                if (isOffline(lastSeenTime)) {
                    statusValue = UserOnlineStatus.OFFLINE
                }
                user.copy(onlineStatus = statusValue)
            } catch (e: Exception) {
                user
            }
        }
        return usersWithOnlineStatus
    }

    override suspend fun getOwnProfile(): User {
        val response = zulipApi.getOwnProfile()
        return User(
            id = response.userId,
            avatarUrl = response.avatarUrl,
            name = response.userName,
            email = response.email,
            onlineStatus = UserOnlineStatus.ACTIVE
        )
    }

    override suspend fun getAnotherUser(userId: Int): User {
        val response = zulipApi.getUser(userId = userId)
        val userStatus =
            zulipApi.getUserPresence(userId.toString())
                .presence
                .aggregated
                .userOnlineStatusDto
                .toUserStatus()

        val user = response.user.toUser()
        return user.copy(onlineStatus = userStatus)
    }

    override suspend fun searchUsers(query: String): List<User> {
        return if (query.isBlank()) {
            getAllUsers()
        } else {
            getAllUsers().filter { it.name.containsQuery(query) }
        }
    }
}
private fun isOffline(timestampInSeconds: Long): Boolean {
    val timestamp = Instant.ofEpochSecond(timestampInSeconds)
    val now = Instant.now()
    val minutesDifference = ChronoUnit.MINUTES.between(timestamp, now)
    return minutesDifference > 7
}