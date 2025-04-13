package com.view.compose_keyboard_edittext

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.view.compose_keyboard_edittext.enums.KeyboardInputType
import com.view.compose_keyboard_edittext.ext.ObjectImpl.generateKeyboard
import com.view.compose_keyboard_edittext.keyboard.Keyboard
import com.view.compose_keyboard_edittext.keyboard.KeyboardImpl
import kotlinx.coroutines.delay

@Composable
fun customEditTextWithKeyboard(
    context: Context = LocalContext.current,
    defaultText: String = "",
    hintText: String = LocalContext.current.getString(R.string.default_hint),
    @ColorRes textColor: Int = R.color.black,
    @ColorRes hintColor: Int = R.color.gray_7a7a7a,
    @ColorRes bgColor: Int = R.color.gray,
    @ColorRes borderColor: Int = R.color.transparent,
    @DrawableRes iconLeft: Int? = null,
    @DrawableRes iconRight: Int? = null,
    @ColorRes iconLeftTint: Int? = null,
    @ColorRes iconRightTint: Int? = null,
    borderSize: Int = 1,
    textSize: Int = 16,
    cornerRadiusSize: Int = 8,
    height: Int = 70,
    width: Int = 200,
    keyboardType: KeyboardInputType = KeyboardInputType.NUMBER,
    isAllCaps: Boolean = false,
    isLowerText: Boolean = false,
    isFullWidth: Boolean = true,
    isShowCurrencyType: Boolean = false,
    isShowKeyboard: Boolean = false,
    maxLine: Int = 1,
    onTextValueChange: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val keyboard: Keyboard = generateKeyboard()
    var cursorVisible by remember { mutableStateOf(true) }
    var text by remember { mutableStateOf(defaultText) }
    var maskingText by remember { mutableStateOf(defaultText) }
    var isKeyboardShow by remember { mutableStateOf(isShowKeyboard) }

    LaunchedEffect(true) {
        while (true) {
            delay(1000)
            cursorVisible = !cursorVisible // Toggle cursor visibility
        }
    }

    Box(modifier = Modifier
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
        contentAlignment = Alignment.CenterStart) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (iconLeft != null) {
                Icon(
                    imageVector = ImageVector.vectorResource(iconLeft),
                    contentDescription = "Icon Left",
                    tint = if (iconLeftTint == null) LocalContentColor.current else Color(context.getColor(iconLeftTint)),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val scrollTextBoxInsideState = rememberScrollState()

                Box{
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
                            keyboard.showKeyboardText(
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
                        KeyboardInputType.NUMBER, KeyboardInputType.PHONE, KeyboardInputType.PASSWORD_NUMBER, KeyboardInputType.CURRENCY -> {
                            keyboard.showKeyboardNumber(
                                context,
                                keyboardType,
                                text,
                                isShowCurrencySelection = isShowCurrencyType,
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
            if (iconRight != null) {
                Icon(
                    imageVector = ImageVector.vectorResource(iconRight),
                    contentDescription = "Icon Right",
                    tint = if (iconRightTint == null) LocalContentColor.current else Color(context.getColor(iconRightTint)),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}
