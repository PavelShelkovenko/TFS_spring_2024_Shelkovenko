package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.screens.chat

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.custom_views.FlexBoxLayout
import io.github.kakaocup.kakao.common.actions.BaseActions
import org.hamcrest.Description
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher

interface FlexBoxActions: BaseActions {

    fun clickByPosition(childPosition: Int) {
        view.perform(object : ViewAction {
            override fun getDescription() = "performing click by position: $childPosition"

            override fun getConstraints() =
                Matchers.allOf(
                    ViewMatchers.isAssignableFrom(View::class.java),
                    object : TypeSafeMatcher<View>() {
                        override fun describeTo(description: Description) {
                            description.appendText("is assignable from class: " + FlexBoxLayout::class.java)
                        }

                        override fun matchesSafely(view: View) =
                            FlexBoxLayout::class.java.isAssignableFrom(view.javaClass)
                    })

            override fun perform(uiController: UiController, view: View) {
                (view as ViewGroup).getChildAt(childPosition).performClick()
            }
        })
    }
}