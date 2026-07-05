package com.ferhatozcelik.jetpackcomposetemplate.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.ShiftLogbookEntity
import com.ferhatozcelik.jetpackcomposetemplate.navigation.Screen
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.*
import com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel.ShiftLogbookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastLogbooksScreen(
    navController: NavController,
    viewModel: ShiftLogbookViewModel = hiltViewModel()
) {
    val logbooks by viewModel.allLogbooks.collectAsState(initial = emptyList())

    val shifts = listOf("All", "Morning Shift (06:00 - 14:00)", "Afternoon Shift (14:00 - 22:00)", "Night Shift (22:00 - 06:00)")
    val areas = listOf("All", "PAP", "SAP", "DAP", "Offside", "Jetty")

    var expandedShift by remember { mutableStateOf(false) }
    var filterShift by remember { mutableStateOf(shifts[0]) }

    var expandedArea by remember { mutableStateOf(false) }
    var filterArea by remember { mutableStateOf(areas[0]) }
    
    var filterDate by remember { mutableStateOf("") } // Simple string for now e.g. 2026-07-03

    // Apply filters
    val filteredLogbooks = logbooks.filter {
        (filterShift == "All" || it.shift == filterShift) &&
        (filterArea == "All" || it.area == filterArea) &&
        (filterDate.isBlank() || it.date.contains(filterDate))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Past Log Books", fontWeight = FontWeight.Bold, color = White) },
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
        ) {
            // Filters Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Filters", fontWeight = FontWeight.Bold, color = PplDarkBlue)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = filterDate,
                        onValueChange = { filterDate = it },
                        label = { Text("Filter by Date (YYYY-MM-DD)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        ExposedDropdownMenuBox(
                            expanded = expandedArea,
                            onExpandedChange = { expandedArea = !expandedArea },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = filterArea,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Area") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedArea) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedArea,
                                onDismissRequest = { expandedArea = false }
                            ) {
                                areas.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption) },
                                        onClick = {
                                            filterArea = selectionOption
                                            expandedArea = false
                                        }
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))

                        ExposedDropdownMenuBox(
                            expanded = expandedShift,
                            onExpandedChange = { expandedShift = !expandedShift },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = filterShift,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Shift") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedShift) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedShift,
                                onDismissRequest = { expandedShift = false }
                            ) {
                                shifts.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption) },
                                        onClick = {
                                            filterShift = selectionOption
                                            expandedShift = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // List Section
            if (filteredLogbooks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No Logbooks Found", color = PplTextDark)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredLogbooks) { logbook ->
                        LogbookCard(logbook = logbook, onClick = {
                            navController.navigate(Screen.LogbookDetail.createRoute(logbook.id))
                        })
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
fun LogbookCard(logbook: ShiftLogbookEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = logbook.date,
                    fontWeight = FontWeight.Bold,
                    color = PplBrightBlue,
                    fontSize = 16.sp
                )
                Text(
                    text = logbook.area,
                    fontWeight = FontWeight.Bold,
                    color = PplDarkBlue,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Shift: ${logbook.shift}",
                color = PplTextDark,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Submitted by: ${logbook.submitterId}",
                color = PplTextDark,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Assets Monitored: ${logbook.assets.size}",
                color = Color.DarkGray,
                fontSize = 12.sp
            )
        }
    }
}
