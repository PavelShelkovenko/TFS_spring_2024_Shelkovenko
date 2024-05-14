package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.repository

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.UserDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.ZulipApi
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.UserDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.toUserDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.toUserDomain
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.toUserOnlineStatusDomain
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.UserOnlineStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.UserRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.MyUserId
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.containsQuery
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val zulipApi: ZulipApi,
    private val userDao: UserDao,
) : UserRepository {

    override suspend fun getAllUsersFromNetwork(): List<User> {
        val usersDto = zulipApi.getAllUsers().members
        val usersWithOnlineStatus = getUsersWithOnlineStatus(usersDto)
        val usersForCaching = usersWithOnlineStatus.map { userDomain -> userDomain.toUserDbo() }
        userDao.insertAll(users = usersForCaching)
        return usersWithOnlineStatus.sortedBy { it.id }
    }

    override suspend fun getAllUsersFromCache(): List<User> {
        val cachedUsers = userDao.getAll()
        return cachedUsers.map { userDbo -> userDbo.toUserDomain() }
    }


    override suspend fun getOwnProfileFromCache(): User? {
        val ownProfileFromCache = userDao.getOwnUser()
        return ownProfileFromCache?.toUserDomain() ?: return null
    }

    override suspend fun getOwnProfileFromNetwork(): User {
        val userOnlineStatus =
            zulipApi.getUserPresence(MyUserId.MY_USER_ID.toString())
                .presence
                .aggregated
                .userOnlineStatusDto
                .toUserOnlineStatusDomain()
        val ownUserResponse = zulipApi.getOwnProfile()
        val ownUser = User(
            id = ownUserResponse.userId,
            name = ownUserResponse.userName,
            email = ownUserResponse.email,
            onlineStatus = userOnlineStatus,
            avatarUrl = ownUserResponse.avatarUrl
        )
        userDao.insert(ownUser.toUserDbo())
        return ownUser
    }

    override suspend fun getAnotherUserFromNetwork(userId: Int): User {
        val userStatus =
            zulipApi.getUserPresence(userId.toString())
                .presence
                .aggregated
                .userOnlineStatusDto
                .toUserOnlineStatusDomain()
        val anotherUser = zulipApi.getUser(userId).user.toUserDomain(userStatus)
        userDao.insert(anotherUser.toUserDbo())
        return anotherUser
    }

    override suspend fun getAnotherUserFromCache(userId: Int): User? {
        val anotherUserFromCache = userDao.getAnotherUser(userId = userId)
        return anotherUserFromCache?.toUserDomain() ?: return null
    }

    override suspend fun searchUsers(query: String): List<User> =
        searchUsersInCache(query).ifEmpty { searchUsersInNetwork(query) }


    private suspend fun searchUsersInNetwork(query: String): List<User> {
        return if (query.isBlank()) {
            getAllUsersFromNetwork()
        } else {
            getAllUsersFromNetwork().filter { it.name.containsQuery(query) }
        }
    }

    private suspend fun searchUsersInCache(query: String): List<User> {
        val usersFromCache = getAllUsersFromCache()
        return if (query.isBlank()) {
            usersFromCache
        } else {
            usersFromCache.filter { it.name.containsQuery(query) }
        }
    }

    private suspend fun getUsersWithOnlineStatus(users: List<UserDto>): List<User> {
        val allUsersOnlineStatus = zulipApi.getAllUsersPresence().presences
        val usersWithOnlineStatus = users.map { user ->
            try {
                val emailKey = allUsersOnlineStatus.keys.first { it == user.zulipEmail }
                val lastSeenTime =
                    allUsersOnlineStatus[emailKey]?.aggregated?.timestamp?.toLong() ?: 0L

                var statusValue =
                    allUsersOnlineStatus[emailKey]?.aggregated?.userOnlineStatusDto?.toUserOnlineStatusDomain()
                        ?: UserOnlineStatus.OFFLINE

                if (checkIfUserIsOffline(lastSeenTime = lastSeenTime)) {
                    statusValue = UserOnlineStatus.OFFLINE
                }
                user.toUserDomain(statusValue)
            } catch (e: Exception) {
                user.toUserDomain()
            }
        }
        return usersWithOnlineStatus
    }

    private fun checkIfUserIsOffline(lastSeenTime: Long): Boolean {
        val timestamp = Instant.ofEpochSecond(lastSeenTime)
        val now = Instant.now()
        val minutesDifference = ChronoUnit.MINUTES.between(timestamp, now)
        return minutesDifference > 5
    }

}