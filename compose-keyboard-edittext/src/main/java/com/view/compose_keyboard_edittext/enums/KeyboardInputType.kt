package com.view.compose_keyboard_edittext.enums

import androidx.compose.ui.graphics.vector.ImageVector

enum class KeyboardInputType {
    NUMBER,
    PHONE,
    TEXT,
    EMAIL,
    PASSWORD_TEXT,
    PASSWORD_NUMBER,
    CURRENCY,
}

enum class KeyboardItemSymbol {
    ALPHABET_OR_NUMBER, // showing button alphanumeric
    SPECIAL // showing button special character
}

class KeyboardComponent(
    val itemTextSymbol: String? = null,
    val itemIconSymbol: Int? = null,
    val itemValue: String = "",
    val weightFullSection: Int? = null,
    val isEnable: Boolean = true
)

enum class CurrencySymbolList(val abbreviation: String, val symbol: String) {
    US("USD", "$"),
    ID("IDR", "Rp"),
    JP("JPY", "¥"),
    EUR("EUR", "€"),
}