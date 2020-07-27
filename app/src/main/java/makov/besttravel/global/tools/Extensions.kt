package makov.besttravel.global.tools

import android.text.Editable
import android.text.TextWatcher
import android.widget.AutoCompleteTextView

fun AutoCompleteTextView.addOnTextChangedListener(listener: (String) -> Unit): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (!this@addOnTextChangedListener.isPerformingCompletion) {
                listener(s.toString())
            }
        }
    }
    addTextChangedListener(textWatcher)
    return textWatcher
}