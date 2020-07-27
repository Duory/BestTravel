package makov.besttravel.global.tools

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.addOnTextChangedListener(listener: (CharSequence) -> Unit): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            listener(s)
        }
    }
    addTextChangedListener(textWatcher)
    return textWatcher
}