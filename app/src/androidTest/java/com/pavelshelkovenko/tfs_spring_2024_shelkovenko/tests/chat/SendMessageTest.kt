package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.tests.chat

import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.MainActivity
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.ChatFragment
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.screens.chat.ChatScreen
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.AppTestRule
import org.junit.Rule
import org.junit.Test

/**
  * Этот тест нужно запускать на (developmentDebug) BuildVariant, так как для этого теста требуется
  * реализация LongPolling'а
 */
class SendMessageTest: TestCase() {

    @get:Rule
    val rule = AppTestRule<MainActivity>(
        Intent(
            ApplicationProvider.getApplicationContext(),
            MainActivity::class.java
        )
    ) {}


    @Test
    fun testSendingMessage() = run {
        val fragmentArgs = bundleOf(
            "streamName" to "another channel",
            "topicName" to "myTopic",
            "streamId" to 1,
        )
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        launchFragmentInContainer<ChatFragment>(fragmentArgs = fragmentArgs, instantiate = {
            ChatFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        navController.setGraph(R.navigation.app_navigation)
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        })
        ChatScreen {
            step("Печатаем сообщение для отправки") {
                messageField.typeText("Hello")
            }
            step("Нажимаем кнопку отправить") {
                sendMessageButton.click()
            }
            step("Проверяем что сообщение отображается в чате") {
                chatRecycler {
                    lastChild<ChatScreen.KSendMessage> {
                        message.isDisplayed()
                        message.hasText("Hello")
                    }
                }
            }

        }
    }
}