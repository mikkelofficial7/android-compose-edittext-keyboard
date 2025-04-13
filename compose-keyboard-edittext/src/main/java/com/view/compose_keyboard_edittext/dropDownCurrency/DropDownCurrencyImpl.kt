package com.view.compose_keyboard_edittext.dropDownCurrency

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.view.compose_keyboard_edittext.R
import com.view.compose_keyboard_edittext.enums.CurrencySymbolList

class DropDownCurrencyImpl: DropDownCurrency {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun showDropdown(defaultSymbol: CurrencySymbolList, onFinished: (CurrencySymbolList) -> Unit) {
        val options = CurrencySymbolList.values().toList()
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(defaultSymbol) }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    TextField(
                        value = "${selectedOptionText.abbreviation} (${selectedOptionText.symbol})",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RectangleShape // remove inner rounding
                    )
                }

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .exposedDropdownSize(true)
                ) {
                    options.forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText = item
                                expanded = false
                            },
                            text = {
                                Text(
                                    text = "${item.abbreviation} - ${item.symbol}",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Button(
                onClick = { onFinished(selectedOptionText) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(start = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(ContextCompat.getColor(LocalContext.current, R.color.blue_700))
                )
            ) {
                Text(text = LocalContext.current.getString(R.string.default_btn_currency))
            }
        }
    }

}