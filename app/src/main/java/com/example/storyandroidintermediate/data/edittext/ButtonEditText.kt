package com.example.storyandroidintermediate.data.edittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.example.storyandroidintermediate.R
import com.google.android.material.button.MaterialButton

class ButtonEditText : MaterialButton {
    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColor: Int = 0
    private var txtColor2: Int = 0

constructor(context: Context) : super(context) {
    init()
}

constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    init()
}

constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    init()
}

override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    background = if(isEnabled) enabledBackground  else disabledBackground
    if(isEnabled) setTextColor(txtColor) else setTextColor(txtColor2)
    textSize = 20f
    gravity = Gravity.CENTER
    text = if(isEnabled) "Submit" else ""
}
private fun init() {
    txtColor = ContextCompat.getColor(context, android.R.color.white)
    txtColor2 = ContextCompat.getColor(context, android.R.color.black)
    enabledBackground = ContextCompat.getDrawable(context, R.drawable.background_button_rill) as Drawable
    disabledBackground = ContextCompat.getDrawable(context, R.drawable.background_story_button_disable) as Drawable
}
}