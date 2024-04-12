package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_5

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.generateRandomEmail
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.generateRandomName
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.UserOnlineStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.throwRandomError
import kotlinx.coroutines.delay
import kotlin.random.Random

class GetStubUsersUseCase(context: Context) {

    private val stubUserAvatar = ResourcesCompat.getDrawable(
        context.resources,
        R.drawable.ic_launcher_background,
        null
    )?.toBitmap() ?: throw IllegalArgumentException("Drawable not found")

    private val stubUser: User
        get() = User(
            id = Random.nextInt(0, 1000000),
            avatar = stubUserAvatar,
            name = generateRandomName(),
            email = generateRandomEmail(),
            onlineStatus = UserOnlineStatus.entries.shuffled().first(),
            activityStatus = "In a meeting"
        )

    private val stubUserList = mutableListOf<User>()

    init {
        for (i in 1..30) {
            stubUserList.add(stubUser)
        }
    }

    suspend fun search(query: String): List<User> {
        delay(1000)
        throwRandomError()
        return if (query.isEmpty()) {
            stubUserList
        } else {
            stubUserList.filter { it.name.lowercase().contains(query) }
        }
    }

    suspend operator fun invoke(): List<User> {
        delay(1000)
        throwRandomError()
        return stubUserList
    }
}