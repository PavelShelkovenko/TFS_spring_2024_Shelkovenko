package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_5

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.UserOnlineStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.throwRandomError
import kotlinx.coroutines.delay
import kotlin.random.Random

class GetStubLocalUserUseCase(context: Context) {

    private val localUserAvatar = ResourcesCompat.getDrawable(
        context.resources,
        R.drawable.ic_launcher_background,
        null
    )?.toBitmap() ?: throw IllegalArgumentException("Drawable not found")

    private val localUser = User(
        id = Random.nextInt(0, 1000000),
        avatar = localUserAvatar,
        name = "Pavel Shelkovenko",
        email = "pavel.shelkovenko@gmail.com",
        onlineStatus = UserOnlineStatus.ACTIVE,
        activityStatus = "In a meeting"
    )

    suspend operator fun invoke(): User {
        delay(1000)
        throwRandomError()
        return localUser
    }
}