package com.view.compose_keyboard_edittext.ext

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun String.convertToStringWithoutSeparator(): String {
    if (this.isEmpty()) return this
    return replace(".", "")
}

fun String.convertToStringWithSeparator() : String {
    if (this.isBlank()) return this

    val normalized = this.replace(",", ".").trimEnd('.')

    val number = try {
        BigDecimal(normalized)
    } catch (e: NumberFormatException) {
        BigDecimal.ZERO
    }

    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }

    val formatter = DecimalFormat("#,##0.##", symbols)
    return formatter.format(number)
}