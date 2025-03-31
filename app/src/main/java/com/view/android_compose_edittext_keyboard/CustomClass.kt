package com.view.android_compose_edittext_keyboard

import android.content.Context
import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat


enum class KeyboardInputType {
    NUMBER,
    TEXT,
    EMAIL,
    PASSWORD_TEXT,
    PASSWORD_NUMBER
}

enum class KeyboardItemSymbol {
    ALPHABET, // showing button alphanumeric
    SPECIAL // showing button special character
}

@Composable
fun CustomEditTextWithKeyboard(
    context: Context = LocalContext.current,
    defaultText: String = "",
    hintText: String = "Enter text",
    gravityPositionVertical: Arrangement.Vertical = Arrangement.Top,
    @ColorRes textColor: Int = R.color.black,
    @ColorRes hintColor: Int = R.color.gray,
    @ColorRes bgColor: Int = R.color.gray,
    @ColorRes borderColor: Int = R.color.transparent,
    borderSize: Int = 1,
    textSize: Int = 16,
    cornerRadiusSize: Int = 8,
    height: Int = 70,
    width: Int = 200,
    keyboardType: KeyboardInputType = KeyboardInputType.PASSWORD_TEXT,
    isAllCaps: Boolean = false,
    isLowerText: Boolean = false,
    isFullWidth: Boolean = true,
    maxLine: Int = 1,
    onTextValueChange: (String, String) -> Unit = { masking, real -> },
    otherEditTextModifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(defaultText) }
    var maskingText by remember { mutableStateOf(defaultText) }
    var isKeyboardShow by remember { mutableStateOf(false) } // Control visibility

    Column(
        verticalArrangement = gravityPositionVertical, // Centers content
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .then(if (isFullWidth) Modifier.fillMaxWidth() else Modifier.width(width.dp))
                .height(height.dp)
                .border(
                    borderSize.dp,
                    Color(ContextCompat.getColor(context, borderColor)),
                    shape = RoundedCornerShape(cornerRadiusSize.dp)
                )
                .background(
                    Color(ContextCompat.getColor(context, bgColor)),
                    shape = RoundedCornerShape(cornerRadiusSize.dp)
                )
                .clickable {
                    isKeyboardShow = !isKeyboardShow
                }
                .then(otherEditTextModifier),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = if (maskingText.isEmpty()) hintText else if (isAllCaps) maskingText.uppercase() else if (isLowerText) maskingText.lowercase() else maskingText,
                color = Color(ContextCompat.getColor(context, if (text.isEmpty()) hintColor else textColor)),
                fontSize = textSize.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }

        if (isKeyboardShow) {
            when (keyboardType) {
                KeyboardInputType.TEXT, KeyboardInputType.EMAIL, KeyboardInputType.PASSWORD_TEXT -> {
                    showingCustomTextKeyboard(context, keyboardType, text, isAllCaps = true, maxLine = maxLine) { masking, real ->
                        text = if (isAllCaps) real.uppercase() else if (isLowerText) real.lowercase() else real
                        maskingText = if (isAllCaps) masking.uppercase() else if (isLowerText) masking.lowercase() else masking

                        onTextValueChange(maskingText, text)
                    }
                }
                else -> {

                }
            }
        }
    }
}

@Composable
fun showingCustomTextKeyboard(
    context: Context,
    keyboardType: KeyboardInputType,
    initialValue: String,
    isAllCaps: Boolean,
    maxLine: Int,
    onTextValueChange: (String, String) -> Unit = { masking, real -> },
) {
    var isAllCapsState by remember { mutableStateOf(isAllCaps) }
    var alphabetOrSpecialCharState by remember { mutableStateOf(KeyboardItemSymbol.ALPHABET) }
    var currentTypingResult by remember { mutableStateOf(initialValue) }

    // normal qwerty
    val qwertyRowsUpper = remember {
        listOf(
            Pair("QWERTYUIOP", true),
            Pair("ASDFGHJKL", false)
        )
    }
    val qwertyRowsLower = remember {
        listOf(
            Pair(Icons.Filled.KeyboardArrowUp, true),
            Pair("Z", false),
            Pair("X", false),
            Pair("C", false),
            Pair("V", false),
            Pair("B", false),
            Pair("N", false),
            Pair("M", false),
            Pair(Icons.Filled.ArrowBack, true),
        )
    }
    val otherRows = remember {
        arrayListOf(
            Triple("?123", false, "?123"),
            Triple("", true, " "),
            Triple(".", false, "."),
            Triple("Enter", false, "\n")
        )
    }

    // special char and number
    val numberAndSpecialRowsUpper = remember {
        listOf(
            Pair("1234567890", true),
            Pair("!?#$%^&*", true),
            Pair("_-+=/|~'", true),
            Pair("\":;?.,<>\\", true)
        )
    }
    val numberAndSpecialRowsLower = remember {
        listOf(
            Pair("`", false),
            Pair("@", false),
            Pair("(", false),
            Pair(")", false),
            Pair("{", false),
            Pair("}", false),
            Pair("[", false),
            Pair("]", false),
            Pair(Icons.Filled.ArrowBack, true),
        )
    }
    val otherNumberAndSpecialRows = remember {
        arrayListOf(
            Triple("abc", false, "abc"),
            Triple("", true, " "),
            Triple("Enter", false, "\n")
        )
    }

    if (keyboardType == KeyboardInputType.EMAIL) {
        otherRows.add(1, Triple("@", false, "@"))
    }

    if (keyboardType == KeyboardInputType.PASSWORD_TEXT) {
        otherRows.remove(otherRows.find { it.first == "" })
        otherNumberAndSpecialRows.remove(otherNumberAndSpecialRows.find { it.first == "" })
    }

    val keyboardBgColor = Color(ContextCompat.getColor(context, R.color.gray_c8c8c8))
    val keyboardTextColor = Color(ContextCompat.getColor(context, R.color.black))
    val keyboardFontSize = 16

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(ContextCompat.getColor(context, R.color.gray)),
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                ),
            contentAlignment = Alignment.Center // Center text inside the Box
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 5.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Keyboard upper
                val upperRowItem = if (alphabetOrSpecialCharState == KeyboardItemSymbol.ALPHABET)
                    qwertyRowsUpper
                else
                    numberAndSpecialRowsUpper

                upperRowItem.forEach { qwertyRowUpper ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        qwertyRowUpper.first.forEach { key ->
                            val formattedKey = if (isAllCapsState) key.uppercase() else key.lowercase()

                            Text(
                                text = formattedKey,
                                fontSize = keyboardFontSize.sp,
                                color = keyboardTextColor,
                                modifier = Modifier
                                    .padding(3.dp)
                                    .background(keyboardBgColor, RoundedCornerShape(4.dp))
                                    .padding(11.dp)
                                    .then(if (qwertyRowUpper.second) Modifier.weight(1f) else Modifier)
                                    .clickable {
                                        currentTypingResult += formattedKey

                                        val maskingText = if (keyboardType == KeyboardInputType.PASSWORD_TEXT) {
                                            "*".repeat(currentTypingResult.length)
                                        } else {
                                            currentTypingResult
                                        }
                                        onTextValueChange(maskingText, currentTypingResult)
                                    }
                            )
                        }
                    }
                }

                // Keyboard lower
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val lowerRowItem = if (alphabetOrSpecialCharState == KeyboardItemSymbol.ALPHABET)
                        qwertyRowsLower
                    else
                        numberAndSpecialRowsLower

                    lowerRowItem.forEach { key ->
                        when (val item = key.first) {
                            is String -> {
                                val formattedKey = if (isAllCapsState) item.uppercase() else item.lowercase()

                                Text(
                                    text = formattedKey,
                                    fontSize = keyboardFontSize.sp,
                                    color = keyboardTextColor,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .background(keyboardBgColor, RoundedCornerShape(6.dp))
                                        .padding(11.dp)
                                        .clickable {
                                            currentTypingResult += formattedKey

                                            val maskingText = if (keyboardType == KeyboardInputType.PASSWORD_TEXT) {
                                                "*".repeat(currentTypingResult.length)
                                            } else {
                                                currentTypingResult
                                            }
                                            onTextValueChange(maskingText, currentTypingResult)
                                        }
                                )
                            }

                            is ImageVector -> {
                                Icon(
                                    imageVector = item,
                                    contentDescription = null,
                                    tint = keyboardTextColor,
                                    modifier = Modifier
                                        .height(50.dp)
                                        .weight(1f)
                                        .padding(1.dp)
                                        .background(keyboardBgColor, RoundedCornerShape(6.dp))
                                        .padding(11.dp)
                                        .size(14.dp)
                                        .clickable {
                                            when (item) {
                                                Icons.Filled.ArrowBack -> {
                                                    currentTypingResult = currentTypingResult.dropLast(1)

                                                    val maskingText = if (keyboardType == KeyboardInputType.PASSWORD_TEXT) {
                                                        "*".repeat(currentTypingResult.length)
                                                    } else {
                                                        currentTypingResult
                                                    }
                                                    onTextValueChange(maskingText, currentTypingResult)
                                                }

                                                Icons.Filled.KeyboardArrowUp -> {
                                                    isAllCapsState = !isAllCapsState

                                                    val maskingText = if (keyboardType == KeyboardInputType.PASSWORD_TEXT) {
                                                        "*".repeat(currentTypingResult.length)
                                                    } else {
                                                        currentTypingResult
                                                    }
                                                    onTextValueChange(maskingText, currentTypingResult)
                                                }
                                            }
                                        }
                                )
                            }
                        }
                    }
                }

                // Special Function Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val otherRowItem = if (alphabetOrSpecialCharState == KeyboardItemSymbol.ALPHABET)
                        otherRows
                    else
                        otherNumberAndSpecialRows

                    otherRowItem.forEach { key ->
                        Text(
                            text = key.first,
                            fontSize = keyboardFontSize.sp,
                            color = keyboardTextColor,
                            modifier = Modifier
                                .then(
                                    if (key.second) Modifier
                                        .fillMaxWidth()
                                        .weight(1f) else Modifier.wrapContentWidth()
                                )
                                .padding(3.dp)
                                .background(keyboardBgColor, RoundedCornerShape(4.dp))
                                .padding(11.dp)
                                .clickable {
                                    when (key.first) {
                                        "?123" -> {
                                            alphabetOrSpecialCharState = KeyboardItemSymbol.SPECIAL
                                        }
                                        "abc" -> {
                                            alphabetOrSpecialCharState = KeyboardItemSymbol.ALPHABET
                                        }
                                        "Enter" -> {
                                            val newlineCount = currentTypingResult.count { it == '\n' }
                                            if (newlineCount < maxLine) {
                                                currentTypingResult += key.third

                                                val maskingText = if (keyboardType == KeyboardInputType.PASSWORD_TEXT) {
                                                    "*".repeat(currentTypingResult.length)
                                                } else {
                                                    currentTypingResult
                                                }
                                                onTextValueChange(maskingText, currentTypingResult)
                                            }
                                        }
                                        else -> {
                                            currentTypingResult += key.third

                                            val maskingText = if (keyboardType == KeyboardInputType.PASSWORD_TEXT) {
                                                "*".repeat(currentTypingResult.length)
                                            } else {
                                                currentTypingResult
                                            }
                                            onTextValueChange(maskingText, currentTypingResult)
                                        }
                                    }
                                }
                        )
                    }
                }
            }
        }
    }

}

