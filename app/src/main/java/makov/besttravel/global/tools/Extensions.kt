package makov.besttravel.global.tools

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

fun EditText.addOnTextChangedListener(listener: (String) -> Unit): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            listener(s.toString())
        }
    }
    addTextChangedListener(textWatcher)
    return textWatcher
}

fun Fragment.openKeyboard(view: View) {
    activity?.openKeyboard(view)
}

fun Activity.openKeyboard(view: View) {
    if (view.requestFocus()) {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
            view,
            InputMethodManager.SHOW_IMPLICIT
        )
    }
}