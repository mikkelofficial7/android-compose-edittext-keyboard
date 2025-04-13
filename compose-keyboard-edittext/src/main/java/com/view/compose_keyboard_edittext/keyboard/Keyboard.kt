package com.view.compose_keyboard_edittext.keyboard

import android.content.Context
import androidx.compose.runtime.Composable
import com.view.compose_keyboard_edittext.enums.KeyboardInputType

interface Keyboard {
    @Composable
    fun showKeyboardNumber(
        context: Context,
        keyboardType: KeyboardInputType,
        initialValue: String,
        isKeyboardShow: Boolean,
        isShowCurrencySelection: Boolean,
        onTextValueChange: (String, String) -> Unit,
        onDismiss: (Boolean) -> Unit
    )

    @Composable
    fun showKeyboardText(
        context: Context,
        keyboardType: KeyboardInputType,
        initialValue: String,
        isKeyboardShow: Boolean,
        isAllCaps: Boolean,
        maxLine: Int,
        onTextValueChange: (String, String) -> Unit,
        onDismiss: (Boolean) -> Unit
    )
}