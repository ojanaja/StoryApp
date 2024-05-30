package com.example.storyapp.customView

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class EditTextEmail : AppCompatEditText {

    private lateinit var emailIconDrawable: Drawable

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        emailIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_email) as Drawable
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        compoundDrawablePadding = 16

        setHint(R.string.prompt_email)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setAutofillHints(AUTOFILL_HINT_EMAIL_ADDRESS)
        }
        setDrawable(emailIconDrawable)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.EditTextEmail)
            val customFont = typedArray.getString(R.styleable.EditTextEmail_customFontEmail)
            customFont?.let { fontName ->
                setCustomFont(context, fontName)
            }
            typedArray.recycle()
        }

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Email validation
                if (!s.isNullOrEmpty() && !Patterns.EMAIL_ADDRESS.matcher(s).matches())
                    error = context.getString(R.string.error_email)
            }
        })
    }

    private fun setCustomFont(context: Context, fontName: String) {
        try {
            val typeface = Typeface.createFromAsset(context.assets, "fonts/$fontName")
            setTypeface(typeface)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setDrawable(
        start: Drawable? = null,
        top: Drawable? = null,
        end: Drawable? = null,
        bottom: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom)
    }
}
