package com.example.mycart.utils.customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.example.mycart.R

class MyCartTextView(context: Context, attributes:AttributeSet)
    : AppCompatTextView(context, attributes) {

        init {
            applyFonts()
        }

    private fun applyFonts() {
        val typeface : Typeface? = ResourcesCompat.getFont(context, R.font.regular)
        setTypeface(typeface)
    }


}