package com.view.compose_keyboard_edittext.dropDownCurrency

import androidx.compose.runtime.Composable
import com.view.compose_keyboard_edittext.enums.CurrencySymbolList

interface DropDownCurrency {
    @Composable
    fun showDropdown(defaultSymbol: CurrencySymbolList, onFinished: (CurrencySymbolList) -> Unit)
}