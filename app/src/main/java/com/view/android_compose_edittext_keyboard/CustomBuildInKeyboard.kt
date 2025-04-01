package com.view.android_compose_edittext_keyboard

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
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

private class KeyboardComponent(
    val itemTextSymbol: String? = null,
    val itemIconSymbol: ImageVector? = null,
    val itemValue: String,
    val isFullExpand: Boolean = false,
    val isEnable: Boolean = true
)

@Composable
fun CustomEditTextWithKeyboard(
    context: Context = LocalContext.current,
    defaultText: String = "",
    hintText: String = "Enter text",
    gravityPositionVertical: Arrangement.Vertical = Arrangement.Top,
    @ColorRes textColor: Int = R.color.black,
    @ColorRes hintColor: Int = R.color.black,
    @ColorRes bgColor: Int = R.color.gray,
    @ColorRes borderColor: Int = R.color.transparent,
    borderSize: Int = 1,
    textSize: Int = 16,
    cornerRadiusSize: Int = 8,
    height: Int = 70,
    width: Int = 200,
    keyboardType: KeyboardInputType = KeyboardInputType.TEXT,
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
                    showingCustomTextKeyboard(
                        context,
                        keyboardType,
                        text,
                        isKeyboardShow = isKeyboardShow,
                        isAllCaps = true,
                        maxLine = maxLine,
                        onTextValueChange = { masking, real ->
                            text = if (isAllCaps) real.uppercase() else if (isLowerText) real.lowercase() else real
                            maskingText = if (isAllCaps) masking.uppercase() else if (isLowerText) masking.lowercase() else masking

                            onTextValueChange(maskingText, text)
                        },
                        onDismiss = {
                            isKeyboardShow = it
                        })
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
    isKeyboardShow: Boolean,
    isAllCaps: Boolean,
    maxLine: Int,
    onTextValueChange: (String, String) -> Unit = { masking, real -> },
    onDismiss: (Boolean) -> Unit = {}
) {
    BackHandler(enabled = isKeyboardShow) {
        onDismiss(!isKeyboardShow)
    }

    var isAllCapsState by remember { mutableStateOf(isAllCaps) }
    var alphabetOrSpecialCharState by remember { mutableStateOf(KeyboardItemSymbol.ALPHABET) }
    var currentTypingResult by remember { mutableStateOf(initialValue) }

    val normalSymbol: ArrayList<ArrayList<KeyboardComponent>> = arrayListOf()
    val specialSymbol: ArrayList<ArrayList<KeyboardComponent>> = arrayListOf()

    // normal qwerty
    val qwertyRows1 = remember {
        arrayListOf(
            KeyboardComponent(
                itemTextSymbol = "Q",
                itemValue = "Q",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "W",
                itemValue = "W",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "E",
                itemValue = "E",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "R",
                itemValue = "R",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "T",
                itemValue = "T",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "Y",
                itemValue = "Y",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "U",
                itemValue = "U",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "I",
                itemValue = "I",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "O",
                itemValue = "O",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "P",
                itemValue = "P",
                isEnable = true
            )
        )
    }
    val qwertyRows2 = remember {
        arrayListOf(
            KeyboardComponent(
                itemTextSymbol = "A",
                itemValue = "A",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "S",
                itemValue = "S",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "D",
                itemValue = "D",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "F",
                itemValue = "F",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "G",
                itemValue = "G",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "H",
                itemValue = "H",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "J",
                itemValue = "J",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "K",
                itemValue = "K",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "L",
                itemValue = "L",
                isEnable = true
            )
        )
    }
    val qwertyRows3 = remember {
        arrayListOf(
            KeyboardComponent(
                itemIconSymbol = Icons.Filled.KeyboardArrowUp,
                itemValue = "_caps_",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "Z",
                itemValue = "Z",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "X",
                itemValue = "X",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "C",
                itemValue = "C",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "V",
                itemValue = "V",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "B",
                itemValue = "B",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "N",
                itemValue = "N",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "M",
                itemValue = "M",
                isEnable = true
            ),
            KeyboardComponent(
                itemIconSymbol = Icons.Filled.ArrowBack,
                itemValue = "_del_",
                isEnable = true
            )
        )
    }
    val qwertyRows4 = remember {
        arrayListOf(
            KeyboardComponent(
                itemTextSymbol = "?123",
                itemValue = "?123",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "@",
                itemValue = "@",
                isEnable = keyboardType == KeyboardInputType.EMAIL
            ),
            KeyboardComponent(
                itemTextSymbol = " ",
                itemValue = " ",
                isFullExpand = true,
                isEnable = keyboardType != KeyboardInputType.PASSWORD_TEXT
            ),
            KeyboardComponent(
                itemTextSymbol = ".",
                itemValue = ".",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "Enter",
                itemValue = "\n",
                isEnable = keyboardType != KeyboardInputType.PASSWORD_TEXT
            )
        )
    }

    normalSymbol.add(qwertyRows1)
    normalSymbol.add(qwertyRows2)
    normalSymbol.add(qwertyRows3)
    normalSymbol.add(qwertyRows4)

    // special char and number
    val specialRows1 = remember {
        arrayListOf(
            KeyboardComponent(
                itemTextSymbol = "1",
                itemValue = "1",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "2",
                itemValue = "2",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "3",
                itemValue = "3",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "4",
                itemValue = "4",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "5",
                itemValue = "5",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "6",
                itemValue = "6",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "7",
                itemValue = "7",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "8",
                itemValue = "8",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "9",
                itemValue = "9",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "0",
                itemValue = "0",
                isEnable = true
            )
        )
    }
    val specialRows2 = remember {
        arrayListOf(
            KeyboardComponent(
                itemTextSymbol = "`",
                itemValue = "`",
                isFullExpand = false
            ),
            KeyboardComponent(
                itemTextSymbol = "@",
                itemValue = "@",
                isEnable = keyboardType == KeyboardInputType.EMAIL,
                isFullExpand = false
            ),
            KeyboardComponent(
                itemTextSymbol = "!",
                itemValue = "!",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "?",
                itemValue = "?",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "#",
                itemValue = "#",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "$",
                itemValue = "$",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "%",
                itemValue = "%",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "^",
                itemValue = "^",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "&",
                itemValue = "&",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "*",
                itemValue = "*",
                isEnable = true
            )
        )
    }
    val specialRows3 = remember {
        arrayListOf(
            KeyboardComponent(
                itemTextSymbol = "(",
                itemValue = "(",
                isFullExpand = false
            ),
            KeyboardComponent(
                itemTextSymbol = ")",
                itemValue = ")",
                isFullExpand = false
            ),
            KeyboardComponent(
                itemTextSymbol = "_",
                itemValue = "_",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "-",
                itemValue = "-",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "+",
                itemValue = "+",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "=",
                itemValue = "=",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "/",
                itemValue = "/",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "|",
                itemValue = "|",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "~",
                itemValue = "~",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "'",
                itemValue = "'",
                isEnable = true
            )
        )
    }
    val specialRows4 = remember {
        arrayListOf(
            KeyboardComponent(
                itemTextSymbol = "{",
                itemValue = "{",
                isFullExpand = false
            ),
            KeyboardComponent(
                itemTextSymbol = "}",
                itemValue = "}",
                isFullExpand = false
            ),
            KeyboardComponent(
                itemTextSymbol = "\"",
                itemValue = "\"",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = ":",
                itemValue = ":",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = ";",
                itemValue = ";",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "?",
                itemValue = "?",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = ".",
                itemValue = ".",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = ",",
                itemValue = ",",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "<",
                itemValue = "<",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = ">",
                itemValue = ">",
                isEnable = true
            ),
            KeyboardComponent(
                itemTextSymbol = "\\",
                itemValue = "\\",
                isEnable = true
            )
        )
    }
    val specialRows5 = remember {
        arrayListOf(
            KeyboardComponent(
                itemTextSymbol = "abc",
                itemValue = "abc",
                isFullExpand = false
            ),
            KeyboardComponent(
                itemTextSymbol = "[",
                itemValue = "[",
                isFullExpand = false
            ),
            KeyboardComponent(
                itemTextSymbol = "]",
                itemValue = "]",
                isFullExpand = false
            ),
            KeyboardComponent(
                itemTextSymbol = " ",
                itemValue = " ",
                isFullExpand = true,
                isEnable = keyboardType != KeyboardInputType.PASSWORD_TEXT
            ),
            KeyboardComponent(
                itemIconSymbol = Icons.Filled.ArrowBack,
                itemValue = "_del_",
                isFullExpand = false
            ),
            KeyboardComponent(
                itemTextSymbol = "Enter",
                itemValue = "\n",
                isEnable = keyboardType != KeyboardInputType.PASSWORD_TEXT
            )
        )
    }

    specialSymbol.add(specialRows1)
    specialSymbol.add(specialRows2)
    specialSymbol.add(specialRows3)
    specialSymbol.add(specialRows4)
    specialSymbol.add(specialRows5)

    val keyboardBgColor = Color(ContextCompat.getColor(context, R.color.gray_c8c8c8))
    val keyboardTextColor = Color(ContextCompat.getColor(context, R.color.black))
    val keyboardFontSize = 16

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    onDismiss(!isKeyboardShow)
                })
            },
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) { detectTapGestures() }
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
                val symbolList = if (alphabetOrSpecialCharState == KeyboardItemSymbol.ALPHABET)
                    normalSymbol
                else
                    specialSymbol

                symbolList.forEachIndexed { index, oneRowItem ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = when (index) {
                            1 -> Arrangement.Center
                            else -> Arrangement.SpaceEvenly
                        }
                    ) {
                        oneRowItem.forEach { item ->
                            if (!item.itemTextSymbol.isNullOrEmpty()) {
                                val formattedDisplay = if (isAllCapsState) item.itemTextSymbol.uppercase() else item.itemTextSymbol.lowercase()

                                Text(
                                    text = formattedDisplay,
                                    fontSize = keyboardFontSize.sp,
                                    color = keyboardTextColor,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .background(keyboardBgColor, RoundedCornerShape(6.dp))
                                        .padding(11.dp)
                                        .then(
                                            if (item.isFullExpand) Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                            else Modifier.wrapContentWidth()
                                        )
                                        .clickable {
                                            if (!item.isEnable) return@clickable

                                            when (item.itemTextSymbol) {
                                                "?123" -> {
                                                    alphabetOrSpecialCharState =
                                                        KeyboardItemSymbol.SPECIAL
                                                }

                                                "abc" -> {
                                                    alphabetOrSpecialCharState =
                                                        KeyboardItemSymbol.ALPHABET
                                                }

                                                "Enter" -> {
                                                    val newlineCount =
                                                        currentTypingResult.count { it == '\n' }
                                                    if (newlineCount < maxLine) {
                                                        currentTypingResult += item.itemValue

                                                        val maskingText =
                                                            if (keyboardType == KeyboardInputType.PASSWORD_TEXT) {
                                                                "*".repeat(currentTypingResult.length)
                                                            } else {
                                                                currentTypingResult
                                                            }
                                                        onTextValueChange(
                                                            maskingText,
                                                            currentTypingResult
                                                        )
                                                    }
                                                }

                                                else -> {
                                                    currentTypingResult += item.itemValue

                                                    val maskingText =
                                                        if (keyboardType == KeyboardInputType.PASSWORD_TEXT) {
                                                            "*".repeat(currentTypingResult.length)
                                                        } else {
                                                            currentTypingResult
                                                        }
                                                    onTextValueChange(
                                                        maskingText,
                                                        currentTypingResult
                                                    )
                                                }
                                            }
                                        }
                                )
                            } else if (item.itemIconSymbol != null) {
                                Icon(
                                    imageVector = item.itemIconSymbol,
                                    contentDescription = null,
                                    tint = keyboardTextColor,
                                    modifier = Modifier
                                        .height(50.dp)
                                        .then(
                                            if (item.isFullExpand) Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                            else Modifier.wrapContentWidth()
                                        )
                                        .padding(1.dp)
                                        .background(keyboardBgColor, RoundedCornerShape(6.dp))
                                        .padding(11.dp)
                                        .size(14.dp)
                                        .clickable {
                                            if (!item.isEnable) return@clickable

                                            when (item.itemValue) {
                                                "_del_" -> {
                                                    currentTypingResult =
                                                        currentTypingResult.dropLast(1)

                                                    val maskingText =
                                                        if (keyboardType == KeyboardInputType.PASSWORD_TEXT) {
                                                            "*".repeat(currentTypingResult.length)
                                                        } else {
                                                            currentTypingResult
                                                        }
                                                    onTextValueChange(
                                                        maskingText,
                                                        currentTypingResult
                                                    )
                                                }

                                                "_caps_" -> {
                                                    isAllCapsState = !isAllCapsState

                                                    val maskingText =
                                                        if (keyboardType == KeyboardInputType.PASSWORD_TEXT) {
                                                            "*".repeat(currentTypingResult.length)
                                                        } else {
                                                            currentTypingResult
                                                        }
                                                    onTextValueChange(
                                                        maskingText,
                                                        currentTypingResult
                                                    )
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
    }
}

