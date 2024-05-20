package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.screens.chat

import android.view.View
import androidx.core.view.children
import androidx.test.espresso.matcher.BoundedMatcher
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.custom_views.EmojiReactionView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.custom_views.FlexBoxLayout
import org.hamcrest.Description

class ReactionMatcher(
    private val emojiCode: String,
    private val reactionCount: String
): BoundedMatcher<View, FlexBoxLayout>(FlexBoxLayout::class.java) {
    override fun describeTo(description: Description) {
        description.appendText("Reaction code and count doesn't match.")
    }

    override fun matchesSafely(item: FlexBoxLayout): Boolean {
        item.children.forEach {
            try {
                val emojiView = (it as EmojiReactionView)
                if (emojiView.emojiCode == emojiCode && emojiView.reactionCount == reactionCount) {
                    return true
                }
            } catch (_: Exception) {}
        }
        return false
    }

}