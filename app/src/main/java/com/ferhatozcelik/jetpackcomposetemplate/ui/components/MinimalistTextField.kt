package com.ferhatozcelik.jetpackcomposetemplate.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplBrightBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplDarkBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.White
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplTextDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinimalistTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = singleLine,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = PplBrightBlue,
            unfocusedBorderColor = PplDarkBlue,
            focusedLabelColor = PplBrightBlue,
            unfocusedLabelColor = PplDarkBlue,
            cursorColor = PplBrightBlue,
            textColor = PplTextDark,
            containerColor = White
        )
    )
}
