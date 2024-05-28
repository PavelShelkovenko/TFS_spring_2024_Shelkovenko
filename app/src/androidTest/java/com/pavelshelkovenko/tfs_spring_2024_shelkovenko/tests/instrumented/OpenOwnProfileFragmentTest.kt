package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.tests.instrumented

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.github.tomakehurst.wiremock.client.WireMock
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.mock.MockZulip
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.mock.MockZulip.Companion.zulip
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.MainActivity
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.screens.MainActivityScreen
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.screens.profile.own.OwnProfileScreen
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.AppTestRule
import org.junit.Rule
import org.junit.Test
/**
 * Этот тест нужно запускать на "stagingDebug" buildVariant
 */
class OpenOwnProfileFragmentTest: TestCase() {

    @get:Rule
    val rule = AppTestRule<MainActivity>(
        Intent(
            ApplicationProvider.getApplicationContext(),
            MainActivity::class.java
        )
    ) {}

    @Test
    fun openOwnProfileFragment() = run {
        rule.wiremockRule.zulip {
            stubOwnProfile()
        }
        MainActivityScreen {
            step("Проверяем, что BottomNavigation видна") {
                bottomNavigationView.isDisplayed()
            }
            step("Переходим на OwnProfileFragment") {
                bottomNavigationView.setSelectedItem(R.id.ownProfileFragment)
            }
            step("Проверяем, что были отправлены нужные запросы на сервер") {
                WireMock.verify(1, WireMock.getRequestedFor(MockZulip.urlPatterForOwnProfile))
                WireMock.verify(1, WireMock.getRequestedFor(MockZulip.urlPatterForUserOnlineStatus))
                WireMock.verify(2, WireMock.getRequestedFor(MockZulip.urlPatternForSubscribedStreams))
            }
            step("Проверяем, что полученные данные видны на экране") {
                OwnProfileScreen {
                    userName.isDisplayed()
                    avatar.isDisplayed()
                    userOnlineStatus.isDisplayed()
                }
            }
            step("Проверяем, что полученные данные отображаются на экране корректно") {
                OwnProfileScreen {
                    userName.hasText("Iago")
                    userOnlineStatus.hasText("Active")
                    userOnlineStatus.hasTextColor(R.color.green)
                }
            }
        }
    }
}