package com.alex.styleandattrsample

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class StyleViewParent @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : LinearLayout(context, attributeSet, defaultStyleAttribute) {

    init {
        context.obtainStyledAttributes(attributeSet, R.styleable.StyleViewParent, defaultStyleAttribute, 0).apply {
            recycle()
        }
        val styleView = StyleViewChild(context, null, attributeSet, defaultStyleAttribute)
        addView(styleView)
    }

}