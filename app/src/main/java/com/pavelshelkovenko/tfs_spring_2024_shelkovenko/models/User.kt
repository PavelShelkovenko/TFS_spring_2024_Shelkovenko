package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models

import android.graphics.Bitmap

data class User(
    val id: Int,
    val avatar: Bitmap,
    val name: String
)