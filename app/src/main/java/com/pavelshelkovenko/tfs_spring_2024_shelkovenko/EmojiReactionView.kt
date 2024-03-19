package com.pavelshelkovenko.tfs_spring_2024_shelkovenko

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.min


class EmojiReactionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    @ColorInt
    private var textColor: Int = 0

    @ColorInt
    private var selectedBackgroundColor: Int = 0

    @ColorInt
    private var unselectedBackgroundColor: Int = 0

    @ColorInt
    private var borderColor: Int = 0

    private var textSize = 0f
    private var borderWidth = 0f

    var isSelectedReaction = false

    var emojiCode = ""
        private set
    var reactionCount = ""
        private set

    private val emojiStringHelper = "123" // Эта строка для рассчёта ширины эмоджи (экспериментально получено что ее длина равна 3)

    init {
        initAttributes(attrs, defStyleAttr, defStyleRes)
    }

    private var borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = borderColor
        style = Paint.Style.STROKE
        strokeWidth = borderWidth
    }

    private var backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = unselectedBackgroundColor
    }

    private var textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = textColor
        style = Paint.Style.FILL_AND_STROKE
        textSize = this@EmojiReactionView.textSize
    }

    private var reactionCountBounds = Rect()
    private var emojiBounds = Rect()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        textPaint.getTextBounds(reactionCount, 0, reactionCount.length, reactionCountBounds)
        textPaint.getTextBounds(emojiStringHelper, 0, emojiStringHelper.length, emojiBounds)

        val textHeight = reactionCountBounds.height()

        // Предполагаемая ширина View
        val desiredWidth = (reactionCountBounds.width() + emojiBounds.width() + 20f + paddingRight + paddingLeft).toInt()
        // Предполагаемая высота View
        val desiredHeight = textHeight * 2 + paddingTop + paddingBottom

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize // Задан конкретный размер для ширины
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize) // Размер не должен превышать заданный размер
            else -> desiredWidth // Задать предпочтительный размер, если точного или максимального размера не задано
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desiredHeight, heightSize)
            else -> desiredHeight
        }

        setMeasuredDimension(width, height) // Устанавливаем фактический размер View
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val widthCenter = measuredWidth / 2f
        val viewToDrawWidth = reactionCountBounds.width() + emojiBounds.width()
        val topOffset = height / 2 - reactionCountBounds.exactCenterY()

        canvas.drawRoundRect(
            DEFAULT_PADDING + paddingLeft,
            DEFAULT_PADDING + paddingTop,
            measuredWidth - DEFAULT_PADDING - paddingRight,
            measuredHeight - DEFAULT_PADDING - paddingBottom,
            DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS, borderPaint
        )
        if (isSelectedReaction) {
            backgroundPaint.color = selectedBackgroundColor
        } else {
            backgroundPaint.color = unselectedBackgroundColor
        }
        canvas.drawRoundRect(
            DEFAULT_PADDING + 1f + paddingLeft,
            DEFAULT_PADDING + 1f + paddingTop,
            measuredWidth - (DEFAULT_PADDING + 1f) - paddingRight,
            measuredHeight - (DEFAULT_PADDING + 1f) - paddingBottom,
            DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS, backgroundPaint
        )
        canvas.drawText(
            emojiCode.toEmoji(),
            widthCenter - (viewToDrawWidth / 2f),
            topOffset,
            textPaint
        )
        canvas.drawText(
            reactionCount,
            widthCenter - (viewToDrawWidth / 2f) + emojiBounds.width() - 2 * DEFAULT_PADDING,
            topOffset,
            textPaint
        )
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putString(REACTION_COUNT_KEY, reactionCount)
        bundle.putString(EMOJI_CODE_KEY, emojiCode)
        bundle.putParcelable(INSTANCESTATE_KEY, super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        reactionCount = bundle.getString(REACTION_COUNT_KEY).toString()
        emojiCode = bundle.getString(EMOJI_CODE_KEY).toString()
        super.onRestoreInstanceState(bundle.getParcelable(INSTANCESTATE_KEY))
        invalidate()
    }

    fun setReactionCount(count: String) {
        reactionCount = count
        requestLayout()
    }

    fun setEmojiCode(newEmojiCode: String) {
        emojiCode = newEmojiCode
        invalidate()
    }

    fun setTextColor(color: Int) {
        textColor = color
        textPaint.color = textColor
        invalidate()
    }

    fun setBorderColor(color: Int) {
        borderColor = color
        borderPaint.color = borderColor
        invalidate()
    }

    fun setBorderWidth(width: Float) {
        borderWidth = width
        borderPaint.strokeWidth = borderWidth
        invalidate()
    }

    fun setSelectedBackgroundColor(color: Int) {
        selectedBackgroundColor = color
        invalidate()
    }

    fun setUnselectedBackgroundColor(color: Int) {
        unselectedBackgroundColor = color
        invalidate()
    }

    fun setReactionSelected(isSelectedReaction: Boolean) {
        this.isSelectedReaction = isSelectedReaction
        invalidate()
    }


    fun setTextSize(size: Float) {
        textSize = size
        textPaint.textSize = textSize
        requestLayout()
    }

    private fun initAttributes(attrSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(
            attrSet,
            R.styleable.EmojiReactionView,
            defStyleAttr,
            defStyleRes
        )
        try {
            textColor =
                typedArray.getColor(R.styleable.EmojiReactionView_textColor, DEFAULT_TEXT_COLOR)
            selectedBackgroundColor = typedArray.getColor(
                R.styleable.EmojiReactionView_selectedBackgroundColor,
                DEFAULT_SELECTED_BACKGROUND_COLOR
            )
            unselectedBackgroundColor = typedArray.getColor(
                R.styleable.EmojiReactionView_unselectedBackgroundColor,
                DEFAULT_UNSELECTED_BACKGROUND_COLOR
            )
            borderColor =
                typedArray.getColor(R.styleable.EmojiReactionView_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth = typedArray.getDimension(
                R.styleable.EmojiReactionView_borderWidth,
                DEFAULT_BORDER_WIDTH
            )
            textSize =
                typedArray.getDimension(R.styleable.EmojiReactionView_textSize, DEFAULT_TEXT_SIZE)
            isSelectedReaction = typedArray.getBoolean(
                R.styleable.EmojiReactionView_isSelectedReaction,
                DEFAULT_SELECTION
            )
            reactionCount = typedArray.getInt(
                R.styleable.EmojiReactionView_reactionCount,
                DEFAULT_REACTION_COUNT
            ).toString()
            emojiCode =
                typedArray.getString(R.styleable.EmojiReactionView_emojiCode) ?: DEFAULT_EMOJI_CODE
        } finally {
            typedArray.recycle()
        }

    }
    companion object {
        const val DEFAULT_UNSELECTED_BACKGROUND_COLOR = Color.WHITE
        const val DEFAULT_TEXT_COLOR = Color.BLACK
        const val DEFAULT_SELECTED_BACKGROUND_COLOR = Color.CYAN
        const val DEFAULT_BORDER_COLOR = Color.GRAY
        const val DEFAULT_REACTION_COUNT = 0
        const val DEFAULT_CORNER_RADIUS = 10f
        const val DEFAULT_BORDER_WIDTH = 5f
        const val DEFAULT_TEXT_SIZE = 55f
        const val DEFAULT_PADDING = 5f
        const val DEFAULT_SELECTION = false
        const val DEFAULT_EMOJI_CODE = "2764"
        const val REACTION_COUNT_KEY = "count"
        const val EMOJI_CODE_KEY = "code"
        const val INSTANCESTATE_KEY = "instanceState"
    }
}