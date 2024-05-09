package com.example.testapp.feature_list.presentation

import com.example.testapp.feature_list.presentation.FeatureListCommand.LoadList
import com.example.testapp.feature_list.presentation.data.FeatureListReducerTestData
import com.example.testapp.feature_list.presentation.model.FeatureListStatus.LOADING
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe

class FeatureListReducerTest : BehaviorSpec({
    FeatureListReducerTestData().apply {
        Given("FeatureListReducer") {
            val reducer = FeatureListReducer()
            When("reduce") {
                And("state is initial") {
                    And("event is OnButtonClick") {
                        val actual = reducer.reduce(onButtonClickEvent, initialState)
                        val expectedStatus = LOADING
                        val expectedCommand = LoadList(category)
                        Then("should return result with status is $expectedStatus and commands conains LoadList") {
                            actual.state.status shouldBe expectedStatus
                            actual.commands shouldContain expectedCommand
                        }
                    }
                    And("event is other") {
                        // test
                    }
                }
                And("state is other") {
                    // test
                }
            }
        }
    }
})
