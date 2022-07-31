package com.example.mycart.utils.customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.mycart.R

class MyCartButton(context: Context, attributes:AttributeSet)
    : AppCompatButton(context, attributes) {

        init {
            applyFonts()
        }

    private fun applyFonts() {
        val typeface : Typeface? = ResourcesCompat.getFont(context, R.font.semibold)
        setTypeface(typeface)
        background = ContextCompat.getDrawable(context, R.drawable.bg_button)
    }

}