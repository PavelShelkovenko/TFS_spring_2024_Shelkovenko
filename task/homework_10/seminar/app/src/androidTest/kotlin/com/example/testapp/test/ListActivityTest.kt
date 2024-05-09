package com.example.testapp.test

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testapp.di.ListFeatureTestDependencies
import com.example.testapp.feature_details.DetailsActivity
import com.example.testapp.feature_list.di.FeatureListComponentInjector
import com.example.testapp.feature_list.ui.ListActivity
import com.example.testapp.mock.MockCoffee
import com.example.testapp.mock.MockCoffee.Companion.coffee
import com.example.testapp.screen.ListActivityScreen
import com.example.testapp.screen.ListActivityScreen.KCardItem
import com.example.testapp.util.AppTestRule
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.kakao.intent.KIntent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListActivityTest : TestCase() {

    @get:Rule
    val rule = AppTestRule<ListActivity>(getIntent()) {
        FeatureListComponentInjector.initComponent(featureListDependencies = ListFeatureTestDependencies())
    }

    @Test
    fun exampleTest() = run {
        rule.wiremockRule.coffee { withSingleCoffee() }
        val detailsIntent = KIntent { hasComponent(DetailsActivity::class.java.name) }
        detailsIntent.intending { withCode(RESULT_OK) }

        ListActivityScreen {
            step("Проверяем, что отображается кнопка") {
                button.click()
            }
            step("Проверяем, что вызвался метод /cofee") {
                verify(WireMock.getRequestedFor(MockCoffee.urlPattern))
            }
            flakySafely {
                recycler.childAt<KCardItem>(0) {
                    step("Проверяем отображение карточки") {
                        title.hasText("Black Coffee")
                        description.containsText("Svart kaffe")
                    }
                    step("Нажимаем на карточку") {
                        click()
                    }
                }
            }
        }
        step("Проверяем, что отправился интент на открытие экрана деталей") {
            detailsIntent.intended()
        }
    }
}

private fun getIntent(): Intent {
    return Intent(ApplicationProvider.getApplicationContext(), ListActivity::class.java)
}
