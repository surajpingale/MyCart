package com.example.mycart.utils.customviews

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mycart.R
import com.example.mycart.databinding.EditTextWithTextBinding

class EditTextWithTextView(context: Context, attributes: AttributeSet) :
    ConstraintLayout(context, attributes) {

    private var binding : EditTextWithTextBinding
    private var attrs : TypedArray
    var headerText : TextView
    var errorText : TextView
    var editText : TextView


        init {
            val view = inflate(context, R.layout.edit_text_with_text, this)
            binding = EditTextWithTextBinding.bind(view)
            attrs = context.obtainStyledAttributes(attributes, R.styleable.EditTextWithTextView)
            headerText = binding.tvHeader
            errorText = binding.tvError
            editText = binding.etEmail

            // Will get all strings
            val headerText = attrs.getString(R.styleable.EditTextWithTextView_headerText) ?: ""
            val errorText = attrs.getString(R.styleable.EditTextWithTextView_errorText) ?: ""
            val editTextHint = attrs.getString(R.styleable.EditTextWithTextView_editTextHint) ?: ""

//            Visibility.VISIBLE
            setHeaderText(headerText)
            setErrorText(errorText)
            setEditTextHint(editTextHint)
        }


    private fun setHeaderText(text : String)
    {
        headerText.text = text
    }

    private fun setEditTextHint(hint : String)
    {
        editText.hint = hint
    }

    private fun setErrorText(text: String)
    {
        errorText.text = text
    }

    enum class Visibility(i: Int) {
        VISIBLE(1),
        INVISIBLE(2),
        GONE(0)
    }

}