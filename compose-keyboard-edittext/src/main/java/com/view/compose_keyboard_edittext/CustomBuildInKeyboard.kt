package com.view.compose_keyboard_edittext

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.view.compose_keyboard_edittext.enums.KeyboardComponent
import com.view.compose_keyboard_edittext.enums.KeyboardInputType
import com.view.compose_keyboard_edittext.enums.KeyboardItemSymbol
import kotlinx.coroutines.delay

@Composable
fun customEditTextWithKeyboard(
    context: Context = LocalContext.current,
    defaultText: String = "",
    hintText: String = "Enter text",
    @ColorRes textColor: Int = R.color.black,
    @ColorRes hintColor: Int = R.color.gray_7a7a7a,
    @ColorRes bgColor: Int = R.color.gray,
    @ColorRes borderColor: Int = R.color.transparent,
    borderSize: Int = 1,
    textSize: Int = 16,
    cornerRadiusSize: Int = 8,
    height: Int = 70,
    width: Int = 200,
    keyboardType: KeyboardInputType = KeyboardInputType.NUMBER,
    isAllCaps: Boolean = false,
    isLowerText: Boolean = false,
    isFullWidth: Boolean = true,
    maxLine: Int = 1,
    onTextValueChange: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var cursorVisible by remember { mutableStateOf(true) }
    var text by remember { mutableStateOf(defaultText) }
    var maskingText by remember { mutableStateOf(defaultText) }
    var isKeyboardShow by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        while (true) {
            delay(1000)
            cursorVisible = !cursorVisible // Toggle cursor visibility
        }
    }

    Box(
        modifier = Modifier
    ) {
        val scrollTextBoxInsideState = rememberScrollState()

        Box(
            modifier = Modifier
                .padding(8.dp)
                .then(if (isFullWidth) Modifier.fillMaxWidth() else Modifier.width(width.dp))
                .height(height.dp)
                .heightIn(max = height.dp)
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
                .then(modifier),
            contentAlignment = Alignment.CenterStart
        ) {
            val formattedText = buildAnnotatedString {
                if (maskingText.isEmpty() && cursorVisible && isKeyboardShow) {
                    pushStyle(SpanStyle(color =  Color(ContextCompat.getColor(context, R.color.black))))
                    append("|")
                    pop()
                }

                append(if (maskingText.isEmpty()) "" else if (isAllCaps) maskingText.uppercase() else if (isLowerText) maskingText.lowercase() else maskingText)

                if (maskingText.isNotEmpty() && cursorVisible && isKeyboardShow) {
                    pushStyle(SpanStyle(color =  Color(ContextCompat.getColor(context, R.color.black))))
                    append("|")
                    pop()
                }
            }

            Box(
                Modifier.verticalScroll(scrollTextBoxInsideState)
            ) {
                if (maskingText.isEmpty()) {
                    Text(
                        text = hintText,
                        color = Color(ContextCompat.getColor(context, hintColor)),
                        fontSize = textSize.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
                Text(
                    text = formattedText,
                    color = Color(ContextCompat.getColor(context, textColor)),
                    fontSize = textSize.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }

        if (isKeyboardShow) {
            when (keyboardType) {
                KeyboardInputType.TEXT, KeyboardInputType.EMAIL, KeyboardInputType.PASSWORD_TEXT -> {
                    showingCustomTextKeyboard(
                        context,
                        keyboardType,
                        text,
                        isKeyboardShow = isKeyboardShow,
                        isAllCaps = isAllCaps,
                        maxLine = maxLine,
                        onTextValueChange = { masking, real ->
                            text = real
                            maskingText = masking

                            onTextValueChange(maskingText, text)
                        },
                        onDismiss = {
                            isKeyboardShow = it
                        })
                }
                KeyboardInputType.NUMBER, KeyboardInputType.PHONE, KeyboardInputType.PASSWORD_NUMBER -> {
                    showingCustomNumberKeyboard(
                        context,
                        keyboardType,
                        text,
                        isKeyboardShow = isKeyboardShow,
                        onTextValueChange = { masking, real ->
                            text = real
                            maskingText = masking

                            onTextValueChange(maskingText, text)
                        },
                        onDismiss = {
                            isKeyboardShow = it
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun showingCustomTextKeyboard(
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
    var alphabetOrSpecialCharState by remember { mutableStateOf(KeyboardItemSymbol.ALPHABET_OR_NUMBER) }
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
                itemIconSymbol = R.drawable.ic_caps,
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
                itemIconSymbol = R.drawable.ic_backspace,
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
                weightFullSection = 2,
                isEnable = !(keyboardType == KeyboardInputType.PASSWORD_TEXT || keyboardType == KeyboardInputType.EMAIL)
            ),
            KeyboardComponent(
                itemTextSymbol = ".",
                itemValue = ".",
                isEnable = true
            ),
            KeyboardComponent(
                itemIconSymbol = R.drawable.ic_enter,
                itemValue = "\n",
                weightFullSection = 1,
                isEnable = !(keyboardType == KeyboardInputType.PASSWORD_TEXT || keyboardType == KeyboardInputType.EMAIL)
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
                itemValue = "`"
            ),
            KeyboardComponent(
                itemTextSymbol = "@",
                itemValue = "@",
                isEnable = keyboardType == KeyboardInputType.EMAIL
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
            ),
            KeyboardComponent(
                itemTextSymbol = ")",
                itemValue = ")"
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
                itemValue = "{"
            ),
            KeyboardComponent(
                itemTextSymbol = "}",
                itemValue = "}"
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
                itemValue = "abc"
            ),
            KeyboardComponent(
                itemTextSymbol = "[",
                itemValue = "["
            ),
            KeyboardComponent(
                itemTextSymbol = "]",
                itemValue = "]"
            ),
            KeyboardComponent(
                itemTextSymbol = " ",
                itemValue = " ",
                weightFullSection = 2,
                isEnable = !(keyboardType == KeyboardInputType.PASSWORD_TEXT || keyboardType == KeyboardInputType.EMAIL)
            ),
            KeyboardComponent(
                itemIconSymbol = R.drawable.ic_backspace,
                itemValue = "_del_"
            ),
            KeyboardComponent(
                itemIconSymbol = R.drawable.ic_enter,
                itemValue = "\n",
                weightFullSection = 1,
                isEnable = !(keyboardType == KeyboardInputType.PASSWORD_TEXT || keyboardType == KeyboardInputType.EMAIL)
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

    Dialog(
        onDismissRequest = { onDismiss(!isKeyboardShow) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(ContextCompat.getColor(context, R.color.transparent)).copy(0.1f))
                .pointerInput(Unit) {
                    detectTapGestures { onDismiss(!isKeyboardShow) }
                },
            contentAlignment = Alignment.BottomCenter,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(ContextCompat.getColor(context, R.color.gray)),
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
                    .pointerInput(Unit) { detectTapGestures { } }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 5.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    val symbolList = if (alphabetOrSpecialCharState == KeyboardItemSymbol.ALPHABET_OR_NUMBER)
                        normalSymbol
                    else
                        specialSymbol

                    Row {
                        val scrollTextBoxInsideState = rememberScrollState()

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                .heightIn(max = 100.dp)
                                .padding(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(scrollTextBoxInsideState)
                            ) {
                                Text(
                                    text = if (keyboardType != KeyboardInputType.PASSWORD_TEXT) currentTypingResult
                                    else "*".repeat(currentTypingResult.length),
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                    }
                    symbolList.forEachIndexed { index, oneRowItem ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = when (index) {
                                1 -> Arrangement.Center
                                else -> Arrangement.SpaceEvenly
                            }
                        ) {
                            oneRowItem.forEach { item ->
                                val itemValue = if (isAllCapsState) item.itemValue.uppercase() else item.itemValue.lowercase()

                                if (!item.itemTextSymbol.isNullOrEmpty()) {
                                    val formattedDisplay = if (isAllCapsState) item.itemTextSymbol.uppercase() else item.itemTextSymbol.lowercase()

                                    Text(
                                        text = formattedDisplay,
                                        fontSize = keyboardFontSize.sp,
                                        color = if (item.isEnable) keyboardTextColor else keyboardTextColor.copy(
                                            0.3f
                                        ),
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .background(
                                                if (item.isEnable) keyboardBgColor else keyboardBgColor.copy(
                                                    0.3f
                                                ), RoundedCornerShape(6.dp)
                                            )
                                            .padding(11.dp)
                                            .then(
                                                if (item.weightFullSection == null)
                                                    Modifier.wrapContentWidth()
                                                else Modifier
                                                    .fillMaxWidth()
                                                    .weight(item.weightFullSection.toFloat())
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
                                                            KeyboardItemSymbol.ALPHABET_OR_NUMBER
                                                    }

                                                    else -> {
                                                        currentTypingResult += itemValue

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
                                    val imageVector = ImageVector.vectorResource(id = item.itemIconSymbol)

                                    Icon(
                                        imageVector = imageVector,
                                        contentDescription = null,
                                        tint = if (item.isEnable) keyboardTextColor else keyboardTextColor.copy(
                                            0.3f
                                        ),
                                        modifier = Modifier
                                            .height(50.dp)
                                            .then(
                                                if (item.weightFullSection == null)
                                                    Modifier.wrapContentWidth()
                                                else Modifier
                                                    .fillMaxWidth()
                                                    .weight(item.weightFullSection.toFloat())
                                            )
                                            .padding(1.dp)
                                            .background(
                                                if (item.isEnable) keyboardBgColor else keyboardBgColor.copy(
                                                    0.3f
                                                ), RoundedCornerShape(6.dp)
                                            )
                                            .padding(11.dp)
                                            .size(14.dp)
                                            .clickable {
                                                if (!item.isEnable) return@clickable

                                                when (item.itemValue.lowercase()) {
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

                                                    "\n" -> {
                                                        val newlineCount =
                                                            currentTypingResult.count { it == '\n' }
                                                        if (newlineCount < maxLine) {
                                                            currentTypingResult += itemValue

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
}

@Composable
private fun showingCustomNumberKeyboard(
    context: Context,
    keyboardType: KeyboardInputType,
    initialValue: String,
    isKeyboardShow: Boolean,
    onTextValueChange: (String, String) -> Unit = { masking, real -> },
    onDismiss: (Boolean) -> Unit = {}
) {
    BackHandler(enabled = isKeyboardShow) {
        onDismiss(!isKeyboardShow)
    }

    var currentTypingResult by remember { mutableStateOf(initialValue) }
    val phoneSymbol: ArrayList<ArrayList<KeyboardComponent>> = arrayListOf()
    val phoneSpecialSymbol: ArrayList<ArrayList<KeyboardComponent>> = arrayListOf()
    var alphabetOrSpecialCharState by remember { mutableStateOf(KeyboardItemSymbol.ALPHABET_OR_NUMBER) }

    val qwertyRows1 = remember {
        arrayListOf(
            KeyboardComponent(
                itemTextSymbol = "1",
                itemValue = "1",
                isEnable = true,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemTextSymbol = "2",
                itemValue = "2",
                isEnable = true,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemTextSymbol = "3",
                itemValue = "3",
                isEnable = true,
                weightFullSection = 1
            )
        )
    }
    val qwertyRows2 = remember {
        arrayListOf(
            KeyboardComponent(
                itemTextSymbol = "4",
                itemValue = "4",
                isEnable = true,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemTextSymbol = "5",
                itemValue = "5",
                isEnable = true,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemTextSymbol = "6",
                itemValue = "6",
                isEnable = true,
                weightFullSection = 1
            )
        )
    }
    val qwertyRows3 = remember {
        arrayListOf(
            KeyboardComponent(
                itemTextSymbol = "7",
                itemValue = "7",
                isEnable = true,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemTextSymbol = "8",
                itemValue = "8",
                isEnable = true,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemTextSymbol = "9",
                itemValue = "9",
                isEnable = true,
                weightFullSection = 1
            )
        )
    }
    val qwertyRows4 = remember {
        arrayListOf(
            KeyboardComponent(
                itemTextSymbol = "+*#",
                itemValue = "+*#",
                isEnable = keyboardType != KeyboardInputType.NUMBER,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemTextSymbol = "0",
                itemValue = "0",
                isEnable = true,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemIconSymbol = R.drawable.ic_backspace,
                itemValue = "_del_",
                isEnable = true,
                weightFullSection = 1
            )
        )
    }

    phoneSymbol.add(qwertyRows1)
    phoneSymbol.add(qwertyRows2)
    phoneSymbol.add(qwertyRows3)
    phoneSymbol.add(qwertyRows4)

    val qwertySpecialRows1 = remember {
        arrayListOf(
            KeyboardComponent(
                itemTextSymbol = ".",
                itemValue = ".",
                isEnable = true,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemTextSymbol = ",",
                itemValue = ",",
                isEnable = true,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemTextSymbol = "-",
                itemValue = "-",
                isEnable = true,
                weightFullSection = 1
            )
        )
    }
    val qwertySpecialRows2 = remember {
        arrayListOf(
            KeyboardComponent(
                itemTextSymbol = "+",
                itemValue = "+",
                isEnable = true,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemTextSymbol = "*",
                itemValue = "*",
                isEnable = true,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemTextSymbol = "#",
                itemValue = "#",
                isEnable = true,
                weightFullSection = 1
            )
        )
    }
    val qwertySpecialRows3 = remember {
        arrayListOf(
            KeyboardComponent(
                itemIconSymbol = R.drawable.ic_spacebar,
                itemValue = " ",
                isEnable = keyboardType != KeyboardInputType.PASSWORD_NUMBER,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemTextSymbol = " ",
                itemValue = "",
                isEnable = false,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemTextSymbol = " ",
                itemValue = "",
                isEnable = false,
                weightFullSection = 1
            )
        )
    }
    val qwertySpecialRows4 = remember {
        arrayListOf(
            KeyboardComponent(
                itemTextSymbol = "123",
                itemValue = "123",
                isEnable = keyboardType != KeyboardInputType.NUMBER,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemTextSymbol = " ",
                itemValue = "",
                isEnable = false,
                weightFullSection = 1
            ),
            KeyboardComponent(
                itemIconSymbol = R.drawable.ic_backspace,
                itemValue = "_del_",
                isEnable = true,
                weightFullSection = 1
            )
        )
    }

    phoneSpecialSymbol.add(qwertySpecialRows1)
    phoneSpecialSymbol.add(qwertySpecialRows2)
    phoneSpecialSymbol.add(qwertySpecialRows3)
    phoneSpecialSymbol.add(qwertySpecialRows4)

    val keyboardBgColor = Color(ContextCompat.getColor(context, R.color.gray_c8c8c8))
    val keyboardTextColor = Color(ContextCompat.getColor(context, R.color.black))
    val keyboardFontSize = 16

    Dialog(
        onDismissRequest = { onDismiss(!isKeyboardShow) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(ContextCompat.getColor(context, R.color.transparent)).copy(0.1f))
            .pointerInput(Unit) {
                detectTapGestures { onDismiss(!isKeyboardShow) }
            },
            contentAlignment = Alignment.BottomCenter,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(ContextCompat.getColor(context, R.color.gray)),
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
                    .pointerInput(Unit) { detectTapGestures { } }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 5.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row {
                        val scrollTextBoxInsideState = rememberScrollState()

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                .heightIn(max = 100.dp)
                                .padding(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(scrollTextBoxInsideState)
                            ) {
                                Text(
                                    text = if (keyboardType != KeyboardInputType.PASSWORD_NUMBER) currentTypingResult
                                    else "*".repeat(currentTypingResult.length),
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                    }

                    val listChar = if (alphabetOrSpecialCharState == KeyboardItemSymbol.ALPHABET_OR_NUMBER) {
                        phoneSymbol
                    } else {
                        phoneSpecialSymbol
                    }

                    listChar.forEachIndexed { index, oneRowItem ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            oneRowItem.forEach { item ->
                                if (!item.itemTextSymbol.isNullOrEmpty()) {
                                    val formattedDisplay = item.itemTextSymbol

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .fillMaxWidth().weight(1f)
                                            .padding(4.dp)
                                            .background(
                                                if (item.isEnable)
                                                    keyboardBgColor
                                                else
                                                    keyboardBgColor.copy(0.3f),
                                                RoundedCornerShape(6.dp)
                                            )
                                            .padding(11.dp)
                                            .clickable {
                                                if (!item.isEnable) return@clickable

                                                when (item.itemValue) {
                                                    "+*#" -> {
                                                        alphabetOrSpecialCharState = KeyboardItemSymbol.SPECIAL
                                                    }
                                                    "123" -> {
                                                        alphabetOrSpecialCharState = KeyboardItemSymbol.ALPHABET_OR_NUMBER
                                                    }
                                                    else -> {
                                                        currentTypingResult += item.itemValue

                                                        val maskingText =
                                                            if (keyboardType == KeyboardInputType.PASSWORD_NUMBER) {
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
                                    ) {
                                        Text(
                                            text = formattedDisplay,
                                            fontSize = keyboardFontSize.sp,
                                            textAlign = TextAlign.Center,
                                            color = if (item.isEnable) keyboardTextColor else keyboardTextColor.copy(0.3f),
                                        )
                                    }
                                } else if (item.itemIconSymbol != null) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .fillMaxWidth().weight(1f)
                                            .padding(4.dp)
                                            .background(
                                                if (item.isEnable)
                                                    keyboardBgColor
                                                else
                                                    keyboardBgColor.copy(0.3f),
                                                RoundedCornerShape(6.dp)
                                            )
                                            .padding(11.dp)
                                            .clickable {
                                                if (!item.isEnable) return@clickable

                                                when (item.itemValue) {
                                                    "_del_" -> {
                                                        currentTypingResult =
                                                            currentTypingResult.dropLast(1)

                                                        val maskingText =
                                                            if (keyboardType == KeyboardInputType.PASSWORD_NUMBER) {
                                                                "*".repeat(currentTypingResult.length)
                                                            } else {
                                                                currentTypingResult
                                                            }
                                                        onTextValueChange(
                                                            maskingText,
                                                            currentTypingResult
                                                        )
                                                    }
                                                    else -> {
                                                        currentTypingResult += item.itemValue

                                                        val maskingText =
                                                            if (keyboardType == KeyboardInputType.PASSWORD_NUMBER) {
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
                                    ) {
                                        val imageVector = ImageVector.vectorResource(id = item.itemIconSymbol)

                                        Icon(
                                            imageVector = imageVector,
                                            contentDescription = null,
                                            tint = if (item.isEnable) keyboardTextColor else keyboardTextColor.copy(0.3f),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}