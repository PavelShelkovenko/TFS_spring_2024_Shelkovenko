package com.pavelshelkovenko.tfs_spring_2024_shelkovenko

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.UserOnlineStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.adapter.StreamDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.delegate_adapter.DelegateItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.CancellationException
import kotlin.random.Random

fun Float.toDp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
)

fun Float.toSp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics
)

fun String.toEmoji(): String {
    return this.split("-")
        .map { String(Character.toChars(it.toInt(16))) }
        .joinToString(separator = "")
}

fun String.containsQuery(query: String): Boolean {
    return this.lowercase().contains(query.trim().lowercase())
}

inline fun <T> runCatchingNonCancellation(block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}

fun generateRandomColor(): Int {
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)
    return Color.argb(255, red, green, blue)
}

fun getFormattedDate(dateOfMessageInSeconds: Int): String {
    val formatter = SimpleDateFormat("dd MMMM")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = dateOfMessageInSeconds * 1000L
    return formatter.format(calendar.time)
}

fun generateRandomId(): Int {
    return Random.nextInt()
}

fun View.setColoredBackgroundStatus(status: UserOnlineStatus) {
    when (status) {
        UserOnlineStatus.ACTIVE -> {
            this.setBackgroundResource(R.drawable.green_circle_background)
        }
        UserOnlineStatus.IDLE -> {
            this.setBackgroundResource(R.drawable.orange_circle_background)
        }
        UserOnlineStatus.OFFLINE -> {
            this.setBackgroundResource(R.drawable.gray_circle_background)
        }
    }
}

fun TextView.setColoredTextStatus(status: UserOnlineStatus) {
    val resources = this.resources
    val context = this.context
    when (status) {
        UserOnlineStatus.ACTIVE -> {
            this.setTextColor(
                resources.getColor(
                    R.color.green,
                    context?.theme
                )
            )
            this.text = resources.getString(R.string.status_active)
        }
        UserOnlineStatus.IDLE -> {
            this.setTextColor(
                resources.getColor(
                    R.color.orange,
                    context?.theme
                )
            )
            this.text = resources.getString(R.string.status_idle)
        }
        UserOnlineStatus.OFFLINE -> {
            this.setTextColor(
                resources.getColor(
                    R.color.light_gray,
                    context?.theme
                )
            )
            this.text = resources.getString(R.string.status_offline)
        }
    }
}


fun List<Stream>.toDelegateList(): List<DelegateItem> {
    return this.map {
        StreamDelegateItem(
            it.id,
            value = it
        )
    }
}