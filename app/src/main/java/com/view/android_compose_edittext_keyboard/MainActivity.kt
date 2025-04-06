package com.view.android_compose_edittext_keyboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.view.android_compose_edittext_keyboard.ui.theme.AndroidcomposeedittextkeyboardTheme
import com.view.compose_keyboard_edittext.customEditTextWithKeyboard
import com.view.compose_keyboard_edittext.enums.KeyboardInputType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidcomposeedittextkeyboardTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        customEditTextWithKeyboard(hintText = "input type text", keyboardType = KeyboardInputType.TEXT)
                        customEditTextWithKeyboard(hintText = "input type email", keyboardType = KeyboardInputType.EMAIL)
                        customEditTextWithKeyboard(hintText = "input type currency", keyboardType = KeyboardInputType.CURRENCY)
                        customEditTextWithKeyboard(hintText = "input type number", keyboardType = KeyboardInputType.NUMBER)
                        customEditTextWithKeyboard(hintText = "input type phone", keyboardType = KeyboardInputType.PHONE)
                        customEditTextWithKeyboard(hintText = "input type password number", keyboardType = KeyboardInputType.PASSWORD_NUMBER)
                        customEditTextWithKeyboard(hintText = "input type password text", keyboardType = KeyboardInputType.PASSWORD_TEXT)
                    }
                }
            }
        }
    }
}