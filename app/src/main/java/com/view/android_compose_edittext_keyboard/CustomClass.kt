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
    keyboardType: KeyboardInputType = KeyboardInputType.TEXT,
    isAllCaps: Boolean = false,
    isLowerCaps: Boolean = false,
    isFullWidth: Boolean = true,
    onTextValueChange: (String) -> Unit = {},
    otherEditTextModifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(defaultText) }
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
                text = if (text.isEmpty()) hintText else if (isAllCaps) text.uppercase() else if (isLowerCaps) text.lowercase() else text,
                color = Color(ContextCompat.getColor(context, if (text.isEmpty()) hintColor else textColor)),
                fontSize = textSize.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }

        if (isKeyboardShow) {
            when (keyboardType) {
                KeyboardInputType.TEXT, KeyboardInputType.EMAIL, KeyboardInputType.PASSWORD_TEXT -> {
                    showingCustomTextKeyboard(context, keyboardType, text, isAllCaps, isLowerCaps) {
                        text = if (isAllCaps) it.uppercase() else if (isLowerCaps) it.lowercase() else it
                        onTextValueChange(text)
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
    isLowerCaps: Boolean,
    onTextValueChange: (String) -> Unit = {},
) {
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
            Triple("", true, "space"),
            Triple(".", false, "."),
            Triple("Enter", false, "enter")
        )
    }

    // special char and number
    val numberAndSpecialRowsUpper = remember {
        listOf(
            Pair("1234567890", true),
            Pair("!?#$%^&*(", true),
            Pair(")_-+=/|~'!", true),
            Pair("\":;?.,<>", true)
        )
    }
    val numberAndSpecialRowsLower = remember {
        listOf(
            Pair("`", false),
            Pair(":", false),
            Pair(";", false),
            Pair("@", false),
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
            Triple("", true, "space"),
            Triple("Enter", false, "enter")
        )
    }

    if (keyboardType == KeyboardInputType.EMAIL) {
        otherRows.add(1, Triple("@", false, "@"))
    }

    val keyboardBgColor = Color(ContextCompat.getColor(context, R.color.gray_c8c8c8))
    val keyboardTextColor = Color(ContextCompat.getColor(context, R.color.black))
    val keyboardFontSize = 16

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 5.dp),
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
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp, bottom = 5.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Keyboard upper
                qwertyRowsUpper.forEach { qwertyRowUpper ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        qwertyRowUpper.first.forEach { key ->
                            Text(
                                text = key.toString(),
                                fontSize = keyboardFontSize.sp,
                                color = keyboardTextColor,
                                modifier = Modifier
                                    .padding(3.dp)
                                    .background(keyboardBgColor, RoundedCornerShape(4.dp))
                                    .padding(11.dp)
                                    .then(if (qwertyRowUpper.second) Modifier.weight(1f) else Modifier)
                            )
                        }
                    }
                }

                // Keyboard lower
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    qwertyRowsLower.forEach { key ->
                        when (val item = key.first) {
                            is String -> {
                                Text(
                                    text = item,
                                    fontSize = keyboardFontSize.sp,
                                    color = keyboardTextColor,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .background(keyboardBgColor, RoundedCornerShape(6.dp))
                                        .padding(11.dp)
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
                    otherRows.forEach { key ->
                        Text(
                            text = key.first,
                            fontSize = keyboardFontSize.sp,
                            color = keyboardTextColor,
                            modifier = Modifier
                                .then(if (key.second) Modifier.fillMaxWidth().weight(1f) else Modifier.wrapContentWidth())
                                .padding(3.dp)
                                .background(keyboardBgColor, RoundedCornerShape(4.dp))
                                .padding(11.dp)
                        )
                    }
                }
            }
        }
    }

}

