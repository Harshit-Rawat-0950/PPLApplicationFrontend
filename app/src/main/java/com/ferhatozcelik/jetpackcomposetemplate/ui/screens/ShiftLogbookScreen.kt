package com.ferhatozcelik.jetpackcomposetemplate.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.AssetData
import com.ferhatozcelik.jetpackcomposetemplate.ui.components.MinimalistTextField
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.*
import com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel.ShiftLogbookViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Temporary class to hold state during composition
data class AssetState(
    var assetTag: String = "",
    var standingAlarms: String = "",
    var maintenanceStatus: Int = 3, // 1: urgent, 5: perfect
    var maintenanceDone: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShiftLogbookScreen(
    navController: NavController,
    viewModel: ShiftLogbookViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDate = sdf.format(Date())

    // Options
    val shifts = listOf("Morning Shift (06:00 - 14:00)", "Afternoon Shift (14:00 - 22:00)", "Night Shift (22:00 - 06:00)")
    val areas = listOf("PAP", "SAP", "DAP", "Offside", "Jetty")

    // State
    var expandedShift by remember { mutableStateOf(false) }
    var selectedShift by remember { mutableStateOf(shifts[0]) }

    var expandedArea by remember { mutableStateOf(false) }
    var selectedArea by remember { mutableStateOf("Select Area") }

    // Dynamic Assets list
    val assetStates = remember { mutableStateListOf(AssetState()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Report Log Book", fontWeight = FontWeight.Bold, color = White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = White)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = PplDarkBlue)
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
            Text(text = "Date: $currentDate", fontWeight = FontWeight.Bold, color = PplTextDark)
            Spacer(modifier = Modifier.height(16.dp))

            // Shift Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedShift,
                onExpandedChange = { expandedShift = !expandedShift },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedShift,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Shift") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedShift) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedShift,
                    onDismissRequest = { expandedShift = false }
                ) {
                    shifts.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedShift = selectionOption
                                expandedShift = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Area Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedArea,
                onExpandedChange = { expandedArea = !expandedArea },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedArea,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Area") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedArea) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedArea,
                    onDismissRequest = { expandedArea = false }
                ) {
                    areas.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedArea = selectionOption
                                expandedArea = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Asset List
            Text("Assets", fontWeight = FontWeight.Bold, color = PplDarkBlue, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))

            assetStates.forEachIndexed { index, assetState ->
                AssetEntryCard(
                    index = index,
                    state = assetState,
                    onStateChange = { newState ->
                        assetStates[index] = newState
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Add Asset Button
            OutlinedButton(
                onClick = { assetStates.add(AssetState()) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = PplDarkBlue)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Asset")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Another Asset")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (selectedArea == "Select Area") {
                        Toast.makeText(context, "Please select an Area", Toast.LENGTH_SHORT).show()
                    } else if (assetStates.any { it.assetTag.isBlank() }) {
                        Toast.makeText(context, "Please fill in all Asset Tags", Toast.LENGTH_SHORT).show()
                    } else {
                        // Map state to Data entities
                        val assetDataList = assetStates.map {
                            AssetData(
                                assetTag = it.assetTag,
                                standingAlarms = it.standingAlarms,
                                maintenanceStatus = it.maintenanceStatus,
                                maintenanceDone = it.maintenanceDone
                            )
                        }

                        // Submit via ViewModel. Using hardcoded mock user for now, simulating cached auth.
                        viewModel.insertLogbook(
                            date = currentDate,
                            shift = selectedShift,
                            area = selectedArea,
                            submitterId = "EID-1042", // Mock user identity cache
                            assets = assetDataList
                        )

                        Toast.makeText(context, "Logbook Submitted", Toast.LENGTH_SHORT).show()
                        navController.navigateUp()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PplBrightBlue,
                    contentColor = White
                )
            ) {
                Text(text = "Submit Log Book", fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun AssetEntryCard(
    index: Int,
    state: AssetState,
    onStateChange: (AssetState) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Asset #${index + 1}", fontWeight = FontWeight.Bold, color = PplTextDark)
            Spacer(modifier = Modifier.height(8.dp))

            MinimalistTextField(
                value = state.assetTag,
                onValueChange = { onStateChange(state.copy(assetTag = it)) },
                label = "Asset Tag",
                singleLine = true
            )

            MinimalistTextField(
                value = state.standingAlarms,
                onValueChange = { onStateChange(state.copy(standingAlarms = it)) },
                label = "Standing Alarms & Parameters",
                singleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Maintenance Status (1: Urgent, 5: Perfect)", color = PplTextDark, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                (1..5).forEach { level ->
                    MaintenanceChip(
                        level = level,
                        isSelected = state.maintenanceStatus == level,
                        onClick = { onStateChange(state.copy(maintenanceStatus = level)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            MinimalistTextField(
                value = state.maintenanceDone,
                onValueChange = { onStateChange(state.copy(maintenanceDone = it)) },
                label = "Maintenance Done in Shift",
                singleLine = false
            )
        }
    }
}

@Composable
fun MaintenanceChip(level: Int, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) PplBrightBlue else Color.LightGray
    val textColor = if (isSelected) White else Color.DarkGray

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(bgColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = level.toString(),
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}
