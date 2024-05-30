package com.example.storyapp.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class EditTextPassword : AppCompatEditText {

    private var passwordIconDrawable: Drawable? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        transformationMethod = PasswordTransformationMethod.getInstance()
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        passwordIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_password)
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        compoundDrawablePadding = 16

        hint = context.getString(R.string.prompt_password)
        setAutofillHints(AUTOFILL_HINT_PASSWORD)

        setCompoundDrawablesWithIntrinsicBounds(passwordIconDrawable, null, null, null)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.EditTextPassword)
            val customFont = typedArray.getString(R.styleable.EditTextPassword_customFontPassword)
            customFont?.let { fontName ->
                setCustomFont(context, fontName)
            }
            typedArray.recycle()
        }

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length < 8) {
                    error = context.getString(R.string.invalid_password)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
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
}
