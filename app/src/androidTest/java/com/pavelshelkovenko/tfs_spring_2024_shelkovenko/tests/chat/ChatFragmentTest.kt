package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.tests.chat

import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.mock.MockZulip
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.mock.MockZulip.Companion.zulip
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.MainActivity
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.ChatFragment
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.screens.chat.ChatScreen
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.AppTestRule
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.getFormattedDate
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
* Этот тест нужно запускать на "stagingDebug" buildVariant
*/

class ChatFragmentTest : TestCase() {

    @get:Rule
    val rule = AppTestRule<MainActivity>(
        Intent(
            ApplicationProvider.getApplicationContext(),
            MainActivity::class.java
        )
    ) {}

    @Test
    fun checkingVisibilityOfChatItems() = run {
        rule.wiremockRule.zulip { stubTestMessages() }
        val fragmentArgs = bundleOf(
            "streamName" to "test stream",
            "topicName" to "test topic",
            "streamId" to 1
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
            step("Проверяем, что были отправлены нужные запросы на сервер") {
                verify(1, WireMock.getRequestedFor(MockZulip.urlPatternForMessages))
            }
            step("Проверяем, что отображаются экран чата") {
                streamName.isDisplayed()
                topicName.isDisplayed()
                chatRecycler.isDisplayed()
            }
            step("Проверяем количество элементов") {
                Assert.assertEquals(7, chatRecycler.getSize())
            }
            step("Проверяем видимость элементов") {
                streamName.hasText("test stream")
                topicName.hasText("Topic: test topic")
                chatRecycler {
                    childAt<ChatScreen.KChatDate>(0) {
                        date.isDisplayed()
                        date.hasText(getFormattedDate(1715330849))
                    }
                    childAt<ChatScreen.KReceivedMessage>(1) {
                        message.isDisplayed()
                        message.hasText("/user_uploads/63414/oSw0X1ExHDbQpciG4OPJK3Wr/IMG_20240507_175907.jpg")
                        userName.isDisplayed()
                        userName.hasText("Алексей Решетников")
                        userAvatar.isDisplayed()
                    }
                    childAt<ChatScreen.KChatDate>(2) {
                        date.isDisplayed()
                        date.hasText(getFormattedDate(1715435094))
                    }
                    childAt<ChatScreen.KSendMessage>(3) {
                        message.isDisplayed()
                        message.hasText("goo")
                    }
                }
            }
            step("Проверяем корректное отображение реакций") {
                chatRecycler {
                    lastChild<ChatScreen.KSendMessage> {
                        flexBox.isDisplayed()
                        flexBox.hasChildrenCount(3)
                        flexBox.hasReaction("1f914", "2")
                    }
                }
            }

        }
    }
}