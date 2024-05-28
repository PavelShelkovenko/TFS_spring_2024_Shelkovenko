package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base

import android.content.Context
import android.os.Parcelable
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.Locale


sealed class Text : Parcelable {

    abstract fun resolve(context: Context): CharSequence

    fun resolveAsString(context: Context): String = resolve(context).toString()

    @Parcelize
    data class ResText(@StringRes val resId: Int, private val formatArgs: @RawValue List<Any>? = null) : Text() {
        override fun resolve(context: Context): CharSequence =
            if (formatArgs == null) context.resources.getString(resId)
            else {
                val resolvedArgs = formatArgs.map { arg -> if (arg is Text) arg.resolveAsString(context) else arg }
                context.resources.getString(resId, *resolvedArgs.toTypedArray())
            }
    }

    @Parcelize
    data class ResPluralText(
        @PluralsRes private val resId: Int,
        private val quantity: Int,
        private val formatArgs: @RawValue List<Any>? = null
    ) : Text() {
        override fun resolve(context: Context): CharSequence =
            if (formatArgs == null) context.resources.getQuantityString(resId, quantity, quantity)
            else context.resources.getQuantityString(resId, quantity, quantity, *formatArgs.toTypedArray())
    }

    @Parcelize
    data class PlainText(val value: String) : Text() {
        override fun resolve(context: Context): CharSequence = value

        companion object {
            val EMPTY = PlainText("")
        }
    }

    @Parcelize
    data class LocalDateText(
        val temporal: @RawValue TemporalAccessor,
        val patternResId: Int,
    ) : Text() {

        override fun resolve(context: Context): CharSequence {
            val pattern = context.resources.getString(patternResId)
            return DateTimeFormatter.ofPattern(pattern, Locale.getDefault()).format(temporal)
        }
    }
}

/**
 * Convenience method to convert String to Text
 *
 * @return text - [Text.PlainText] wrapping the String
 */
fun String.toText() = Text.PlainText(this)