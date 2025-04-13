package com.view.compose_keyboard_edittext.ext

import com.view.compose_keyboard_edittext.dropDownCurrency.DropDownCurrencyImpl
import com.view.compose_keyboard_edittext.keyboard.KeyboardImpl

object ObjectImpl {
    fun generateKeyboard(): KeyboardImpl {
        return KeyboardImpl()
    }

    fun generateDropdown(): DropDownCurrencyImpl {
        return DropDownCurrencyImpl()
    }
}