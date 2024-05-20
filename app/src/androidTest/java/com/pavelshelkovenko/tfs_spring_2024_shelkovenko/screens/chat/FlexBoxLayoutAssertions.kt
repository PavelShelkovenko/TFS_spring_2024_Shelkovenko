package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.screens.chat

import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import io.github.kakaocup.kakao.common.assertions.BaseAssertions

interface FlexBoxLayoutAssertions : BaseAssertions {

    fun hasChildrenCount(count: Int) {
        view.check(
            ViewAssertions.matches(
                ViewMatchers.hasChildCount(count)
            )
        )
    }

    fun hasReaction(
        emojiCode: String,
        reactionCount: String,
    ) {
        view.check(
            ViewAssertions.matches(
                ReactionMatcher(
                    emojiCode = emojiCode,
                    reactionCount = reactionCount
                )
            )
        )
    }
}