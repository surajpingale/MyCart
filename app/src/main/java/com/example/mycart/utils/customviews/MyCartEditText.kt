package com.example.mycart.utils.customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.mycart.R

class MyCartEditText(context: Context, attributes:AttributeSet)
    : AppCompatEditText(context, attributes) {

        init {
            applyFonts()
        }

    private fun applyFonts() {
        val typeface : Typeface? = ResourcesCompat.getFont(context, R.font.regular)
        setTypeface(typeface)
        background = ContextCompat.getDrawable(context,R.drawable.bg_edit_text_login)
    }

}