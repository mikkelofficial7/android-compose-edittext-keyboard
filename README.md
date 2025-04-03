# Jetpack Compose based Edit Text with built-in keyboard ⌨️

<div align="left">
  <img src="https://github.com/mikkelofficial7/android-compose-edittext-keyboard/blob/main/demo.gif" alt="demo" width="200" height="400">
</div>

Latest stable version: 

[![](https://jitpack.io/v/mikkelofficial7/android-compose-edittext-keyboard.svg)](https://jitpack.io/#mikkelofficial7/android-compose-edittext-keyboard)

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
isAllCaps: Boolean, // true or false
isLowerText: Boolean, // true or false
isFullWidth: Boolean, // true or false
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
PASSWORD_NUMBER
```
