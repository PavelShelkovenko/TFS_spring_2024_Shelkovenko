package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils

import android.graphics.Color

object TopicColorProvider {

    private var currentColor = TopicColor.YELLOW

    fun provideColor(): Int {
        return when(currentColor) {
            TopicColor.GREEN -> {
                currentColor = TopicColor.YELLOW
                Color.argb(255,0, 128, 128)
            }
            TopicColor.YELLOW -> {
                currentColor = TopicColor.GREEN
                Color.argb(255,235, 199, 68)
            }
        }
    }
}

enum class TopicColor {
    GREEN, YELLOW
}