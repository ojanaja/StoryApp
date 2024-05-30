package com.example.storyapp.customView

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class EditTextName : AppCompatEditText {

    private lateinit var personIconDrawable: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {
        // Load the icon drawable
        personIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_person)!!

        // Set input type and padding
        inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        compoundDrawablePadding = 16

        // Set hint and autofill hints
        setHint(R.string.prompt_username)
        setAutofillHints(AUTOFILL_HINT_NAME)

        // Set the drawable icon
        setDrawable(personIconDrawable)

        // Add text watcher for validation
        addTextChangedListener(UsernameTextWatcher())
    }

    private fun setDrawable(
        start: Drawable? = null,
        top: Drawable? = null,
        end: Drawable? = null,
        bottom: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom)
    }

    // Inner class for TextWatcher to handle text changes
    private inner class UsernameTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Username validation: must be at least 2 characters
            if (!s.isNullOrEmpty() && s.length < 2) {
                error = context.getString(R.string.invalid_username)
            }
        }
    }

    // Optional method to clear text
    fun clearText() {
        text?.clear()
    }
}
