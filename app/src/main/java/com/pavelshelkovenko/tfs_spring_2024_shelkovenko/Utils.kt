package com.pavelshelkovenko.tfs_spring_2024_shelkovenko

import android.content.Context
import android.graphics.Bitmap
import android.util.TypedValue
import java.io.ByteArrayOutputStream

fun Float.toDp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
)

fun String.toEmoji(): String {
    return String(Character.toChars(this.toInt(16)))
}

fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}