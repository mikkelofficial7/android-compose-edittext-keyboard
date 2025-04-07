package com.view.compose_keyboard_edittext.ext

import android.util.Log
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun String.convertToStringWithoutSeparator(): String {
    Log.d("TAG", "without: $this")
    if (this.isEmpty()) return this

    var mainCurrency = this
    var decimalCurrency = ""

    if (this.contains(",")) {
        val arrCurrency = this.split(",")
        mainCurrency = arrCurrency[0]
        decimalCurrency = if (arrCurrency.size > 1) {
            if (arrCurrency[1].length >= 2) {
                ",${arrCurrency[1].substring(0, 2)}"
            } else {
                ",${arrCurrency[1]}"
            }
        } else {
            ""
        }
    }

    return "${mainCurrency.replace(".", "")}$decimalCurrency"
}

fun String.convertToStringWithSeparator() : String {
    Log.d("TAG", "with: $this")
    if (this.isBlank()) return this

    var mainCurrency = this
    var decimalCurrency = ""

    if (this.contains(",")) {
        val arrCurrency = this.split(",")
        mainCurrency = arrCurrency[0]
        decimalCurrency = if (arrCurrency.size > 1) {
            if (arrCurrency[1].length >= 2) {
                ",${arrCurrency[1].substring(0, 2)}"
            } else {
                ",${arrCurrency[1]}"
            }
        } else {
            ""
        }
    }

    val normalized = mainCurrency.replace(",", ".").trimEnd('.')

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
    return "${formatter.format(number)}$decimalCurrency"
}