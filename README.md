# Keyditboard, One-stop Edit Text with built-in keyboard (Jetpack Compose) ⌨️

<div align="left">
  <img src="https://github.com/mikkelofficial7/android-compose-edittext-keyboard/blob/main/demo.gif" alt="demo" width="200" height="400">
  <img src="https://github.com/mikkelofficial7/android-compose-edittext-keyboard/blob/main/sample.png" alt="demo" width="200" height="400">
</div>

Latest stable version: 

[![](https://jitpack.io/v/mikkelofficial7/android-compose-edittext-keyboard.svg)](https://github.com/mikkelofficial7/android-compose-edittext-keyboard/releases/tag/v1.2)

Deprecated/Previous version: 

[![old version](https://img.shields.io/badge/JitPack-v1.2-f30a06)](https://github.com/mikkelofficial7/android-compose-edittext-keyboard/releases/tag/v1.2)

[![old version](https://img.shields.io/badge/JitPack-v1.1-f30a06)](https://github.com/mikkelofficial7/android-compose-edittext-keyboard/releases/tag/v1.1)

[![old version](https://img.shields.io/badge/JitPack-v1.0-f30a06)](https://github.com/mikkelofficial7/android-compose-edittext-keyboard/releases/tag/v1.0)

How to use (Sample demo provided):

1. Add this gradle in ```build.gradle(:app)``` :
```
dependencies {
   implementation 'com.github.mikkelofficial7:android-compose-edittext-keyboard:v1.0'
}
 ```
or gradle.kts:
```
dependencies {
   implementation("com.github.mikkelofficial7:android-compose-edittext-keyboard:v1.0")
}
 ```

2. Add it in your root settings.gradle at the end of repositories:
```
repositories {
  mavenCentral()
  maven { url 'https://jitpack.io' }
}
```

3. Access the edit text in your compose ```Activity``` or ```Fragment``` class
```
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidcomposeedittextkeyboardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        customEditTextWithKeyboard(hintText = "input type text", keyboardType = KeyboardInputType.TEXT)
                    }
                }
            }
        }
    }
```

4. List of supported parameters you can use
```
defaultText: String,
hintText: String,
@ColorRes textColor: Int = R.color.black, // via colors.xml
@ColorRes hintColor: Int = R.color.black, // via colors.xml
@ColorRes bgColor: Int = R.color.black, // via colors.xml
@ColorRes borderColor: Int = R.color.black, // via colors.xml
borderSize: Int,
textSize: Int,
cornerRadiusSize: Int,
height: Int,
width: Int,
keyboardType: KeyboardInputType,
@DrawableRes iconLeft: Int? = null,
@DrawableRes iconRight: Int? = null,
@ColorRes iconLeftTint: Int? = null,
@ColorRes iconRightTint: Int? = null,
isAllCaps: Boolean, // true or false
isLowerText: Boolean, // true or false
isFullWidth: Boolean, // true or false
isShowKeyboard: Boolean, // true or false
isShowCurrencyType: Boolean = false, // for currency keyboard purpose, true or false
maxLine: Int,
onTextValueChange: (String, String) -> Unit, // if you want to get result typed
modifier: Modifier = Modifier // other customize modifier (if necessary)
```

List of Supported Keyboard Input Type: 
```
NUMBER,
PHONE,
TEXT,
EMAIL,
PASSWORD_TEXT,
PASSWORD_NUMBER,
CURRENCY
```
