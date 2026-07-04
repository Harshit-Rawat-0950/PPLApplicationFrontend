package com.ferhatozcelik.jetpackcomposetemplate.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ferhatozcelik.jetpackcomposetemplate.ui.components.MinimalistTextField
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplBrightBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplDarkBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplLightGrayBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShiftLogbookScreen() {
    var assetTag by remember { mutableStateOf("") }
    var dcsDeviations by remember { mutableStateOf("") }
    var standingAlarms by remember { mutableStateOf("") }
    var carryoverPermits by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Shift Handover Log",
                        fontWeight = FontWeight.Bold,
                        color = White
                    ) 
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = PplDarkBlue
                )
            )
        },
        containerColor = PplLightGrayBlue
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            MinimalistTextField(
                value = assetTag,
                onValueChange = { assetTag = it },
                label = "Asset Tag (e.g., Slurry Pump 201-P-02)",
                singleLine = true
            )

            MinimalistTextField(
                value = dcsDeviations,
                onValueChange = { dcsDeviations = it },
                label = "DCS Parameter Deviations",
                singleLine = false
            )

            MinimalistTextField(
                value = standingAlarms,
                onValueChange = { standingAlarms = it },
                label = "Standing Alarms & Interlock Bypasses",
                singleLine = false
            )

            MinimalistTextField(
                value = carryoverPermits,
                onValueChange = { carryoverPermits = it },
                label = "Carryover Maintenance Permits",
                singleLine = false
            )

            Button(
                onClick = { /* Submit logic */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PplBrightBlue,
                    contentColor = White
                )
            ) {
                Text(text = "Lock & Submit Log", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun ShiftLogbookScreenPreview() {
    com.ferhatozcelik.jetpackcomposetemplate.ui.theme.MyApplicationTheme {
        ShiftLogbookScreen()
    }
}

