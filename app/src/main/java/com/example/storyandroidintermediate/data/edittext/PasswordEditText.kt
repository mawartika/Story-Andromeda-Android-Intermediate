package com.example.storyandroidintermediate.data.edittext

import androidx.appcompat.widget.AppCompatEditText
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet

class PasswordEditText : AppCompatEditText{
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    setError("The password must not be less than 8 characters", null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

}