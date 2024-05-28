package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.tests.instrumented

import androidx.test.ext.junit.rules.activityScenarioRule
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.mock.MockZulip.Companion.zulip
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.MainActivity
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.screens.chat.ChatScreen
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.screens.streams.StreamInfoScreen
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
/**
 * Этот тест нужно запускать на "stagingDebug" buildVariant
 */
class OpenChatFragmentTest: TestCase() {

    @get:Rule
    val wireMockRule = WireMockRule(wireMockConfig(), false).apply {
        zulip { stubSubscribedStreams() }
    }
    @get:Rule
    val activityRule = activityScenarioRule<MainActivity>()

    @Test
    fun openChatFragment() = run {
        StreamInfoScreen {
            step("Проверяем список загруженных тестовых стримов") {
                streamsRecycler.isDisplayed()
                streamsRecycler {
                    childAt<StreamInfoScreen.KStream>(0) {
                        streamName.hasText("#Denmark")
                    }
                    childAt<StreamInfoScreen.KStream>(1) {
                        streamName.hasText("#Scotland")
                    }
                }
                Assert.assertEquals(2, streamsRecycler.getSize())
            }
            step("Открываем список стримов") {
                streamsRecycler {
                    childAt<StreamInfoScreen.KStream>(0) {
                        openTopicsButton.isDisplayed()
                        openTopicsButton.click()
                    }
                }
            }
            step("Проверяем видимость списка стримов") {
                streamsRecycler.isDisplayed()
            }
            step("Проверяем количество топиков") {
                Assert.assertEquals(5, streamsRecycler.getSize())
            }
            step("Открываем экран чата и проверяем, что правильно передались аргументы") {
                streamsRecycler {
                    childAt<StreamInfoScreen.KTopic>(1) {
                        click()
                    }
                }
                ChatScreen {
                    streamName.isDisplayed()
                    topicName.isDisplayed()
                    streamName.hasText("Denmark")
                    topicName.hasText("Topic: Denmark3")
                }
            }
        }

    }
}