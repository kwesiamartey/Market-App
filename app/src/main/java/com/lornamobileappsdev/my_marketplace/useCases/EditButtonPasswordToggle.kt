package com.lornamobileappsdev.my_marketplace.useCases

import android.text.InputType
import android.widget.EditText

fun EditText.tooglePassWord() {
    this.tag = !((this.tag ?: false) as Boolean)
    this.inputType = if (this.tag as Boolean)
        InputType.TYPE_TEXT_VARIATION_PASSWORD
    else
        (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)

    this.setSelection(this.length()) }