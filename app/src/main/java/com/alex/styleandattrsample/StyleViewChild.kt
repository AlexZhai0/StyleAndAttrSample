package com.alex.styleandattrsample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class StyleViewChild @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    attributeSetParent: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
): View(context, attributeSet, defaultStyleAttribute) {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.TRANSPARENT
        strokeWidth = 10f
    }

    init {
        context.obtainStyledAttributes(
            attributeSetParent, R.styleable.StyleViewParent, defaultStyleAttribute, 0).apply {
            setColor(getColor(R.styleable.StyleViewParent_lineColor, Color.TRANSPARENT))
            recycle()
        }
    }

    private fun setColor(color: Int) {
        paint.color = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawLine(0f, 0f, 100f, 100f, paint)
    }
}