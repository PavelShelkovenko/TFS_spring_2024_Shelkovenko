package com.pavelshelkovenko.tfs_spring_2024_shelkovenko

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.TypedValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.ByteArrayOutputStream
import java.util.concurrent.CancellationException
import kotlin.random.Random

fun Float.toDp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
)

fun Float.toSp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics
)

fun String.toEmoji(): String {
    return String(Character.toChars(this.toInt(16)))
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

suspend fun <T1, T2, R> asyncAwait(
    s1: suspend CoroutineScope.() -> T1,
    s2: suspend CoroutineScope.() -> T2,
    transform: suspend (T1, T2) -> R
): R {
    return coroutineScope {
        val result1 = async(block = s1)
        val result2 = async(block = s2)

        transform(
            result1.await(),
            result2.await()
        )
    }
}

fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}


fun generateRandomName(): String {
    val firstNameList = listOf(
        "Emma",
        "Noah",
        "Olivia",
        "Liam",
        "Ava",
        "William",
        "Sophia",
        "Mason",
        "Isabella",
        "James"
    )
    val lastNameList = listOf(
        "Smith",
        "Johnson",
        "Williams",
        "Brown",
        "Jones",
        "Miller",
        "Davis",
        "Taylor",
        "Martin",
        "Anderson"
    )

    val firstName = firstNameList[Random.nextInt(firstNameList.size)]
    val lastName = lastNameList[Random.nextInt(lastNameList.size)]

    return "$firstName $lastName"
}

fun generateRandomEmail(): String {
    val firstNameList = listOf(
        "Emma",
        "Noah",
        "Olivia",
        "Liam",
        "Ava",
        "William",
        "Sophia",
        "Mason",
        "Isabella",
        "James"
    )
    val lastNameList = listOf(
        "Smith",
        "Johnson",
        "Williams",
        "Brown",
        "Jones",
        "Miller",
        "Davis",
        "Taylor",
        "Martin",
        "Anderson"
    )
    val emailDomains = listOf("@gmail.com", "@yahoo.com", "@hotmail.com", "@outlook.com")

    val firstName = firstNameList[Random.nextInt(firstNameList.size)]
    val lastName = lastNameList[Random.nextInt(lastNameList.size)]
    val emailDomain = emailDomains[Random.nextInt(emailDomains.size)]

    return "$firstName.$lastName${Random.nextInt(1000)}$emailDomain"
}

fun generateRandomColor(): Int {
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)
    return Color.argb(255, red, green, blue)
}


fun throwRandomError() {
    val randInt = Random.nextInt(from = 0, until = 10)
    if (randInt <= 2) throw IllegalStateException("Stub exception")
}