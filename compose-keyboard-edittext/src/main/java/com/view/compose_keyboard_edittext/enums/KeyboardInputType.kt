package com.view.compose_keyboard_edittext.enums

import androidx.compose.ui.graphics.vector.ImageVector

enum class KeyboardInputType {
    NUMBER,
    PHONE,
    TEXT,
    EMAIL,
    PASSWORD_TEXT,
    PASSWORD_NUMBER
}

enum class KeyboardItemSymbol {
    ALPHABET, // showing button alphanumeric
    SPECIAL // showing button special character
}

class KeyboardComponent(
    val itemTextSymbol: String? = null,
    val itemTextDesc: String? = null,
    val itemIconSymbol: ImageVector? = null,
    val itemValue: String = "",
    val isFullExpand: Boolean = false,
    val isEnable: Boolean = true
)