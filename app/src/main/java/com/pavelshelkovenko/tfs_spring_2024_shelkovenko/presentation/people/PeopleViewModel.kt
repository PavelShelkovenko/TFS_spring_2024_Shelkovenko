package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.people

import android.app.Application
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.generateRandomEmail
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.generateRandomName
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.UserOnlineStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class PeopleViewModel(
    application: Application
) : AndroidViewModel(application) {


    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList = _userList.asStateFlow()

    private val testUserAvatar = ResourcesCompat.getDrawable(
        application.resources,
        R.drawable.ic_launcher_background,
        null
    )?.toBitmap() ?: throw IllegalArgumentException("Drawable not found")

    private val testUser: User
    get() = User(
        id = Random.nextInt(0, 1000000),
        avatar = testUserAvatar,
        name = generateRandomName(),
        email = generateRandomEmail(),
        status = UserOnlineStatus.entries.shuffled().first()
    )

    init {
        setupStubData()
    }


    private fun setupStubData() {
        val newList = mutableListOf<User>()
        for (i in 1..30) {
            newList.add(testUser)
        }
        _userList.value = newList
    }

}