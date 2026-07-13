package com.ferhatozcelik.jetpackcomposetemplate.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.NearMissEntity
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplDarkBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplLightGrayBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplTextDark
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.White
import com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel.NearMissViewModel
import com.ferhatozcelik.jetpackcomposetemplate.util.BASE_URL
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastNearMissesScreen(
    navController: NavController,
    viewModel: NearMissViewModel = hiltViewModel()
) {
    val nearMisses by viewModel.allNearMisses.collectAsState(initial = emptyList())
    var selectedNearMiss by remember { mutableStateOf<NearMissEntity?>(null) }
    
    var sortByDesc by remember { mutableStateOf(true) }
    var minScoreStr by remember { mutableStateOf("") }
    var selectedFilterArea by remember { mutableStateOf("All") }
    var expandedFilterArea by remember { mutableStateOf(false) }
    val areas = listOf("All", "PAP", "SAP", "DAP", "Offsite", "Jetty")

    val filteredNearMisses = nearMisses.filter {
        val minScore = minScoreStr.toIntOrNull() ?: 0
        it.riskScore >= minScore && (selectedFilterArea == "All" || it.plantArea == selectedFilterArea)
    }.let { list ->
        if (sortByDesc) list.sortedByDescending { it.riskScore } else list.sortedBy { it.riskScore }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(text = "Current Nearmisses", fontWeight = FontWeight.Bold, color = White) 
                },
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
        if (filteredNearMisses.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No Near Misses Found", color = PplTextDark)
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                
                // Filtering UI
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Filters & Sorting", fontWeight = FontWeight.Bold, color = PplDarkBlue)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text("Sort by Score:")
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { sortByDesc = !sortByDesc }) {
                                Text(if (sortByDesc) "Desc" else "Asc")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = minScoreStr,
                            onValueChange = { minScoreStr = it },
                            label = { Text("Min Risk Score") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        ExposedDropdownMenuBox(
                            expanded = expandedFilterArea,
                            onExpandedChange = { expandedFilterArea = !expandedFilterArea },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedFilterArea,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Filter by Area") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFilterArea) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedFilterArea,
                                onDismissRequest = { expandedFilterArea = false }
                            ) {
                                areas.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption) },
                                        onClick = {
                                            selectedFilterArea = selectionOption
                                            expandedFilterArea = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredNearMisses) { entry ->
                        NearMissCard(entry = entry, onClick = { selectedNearMiss = entry })
                    }
                }
            }
        }
        
        selectedNearMiss?.let { nm ->
            NearMissDetailDialog(
                entry = nm,
                onDismiss = { selectedNearMiss = null },
                onResolve = { 
                    viewModel.resolveNearMiss(nm.id)
                    selectedNearMiss = null
                }
            )
        }
    }
}

@Composable
fun NearMissDetailDialog(
    entry: NearMissEntity,
    onDismiss: () -> Unit,
    onResolve: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Near Miss Details", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (entry.photoUrl != null) {
                    val imageUrl = "${BASE_URL.dropLast(1)}${entry.photoUrl}"
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Near Miss Photo",
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Text("Title: ${entry.title}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Area: ${entry.plantArea}")
                Text("Type: ${entry.type}")
                Text("Risk Score: ${entry.riskScore}")
                Text("Description: ${entry.description}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Status: ${if (entry.resolved) "Resolved" else "Pending"}", 
                    fontWeight = FontWeight.Bold, 
                    color = if (entry.resolved) androidx.compose.ui.graphics.Color(0xFF4CAF50) else androidx.compose.ui.graphics.Color.Red
                )
            }
        },
        confirmButton = {
            if (!entry.resolved) {
                Button(onClick = onResolve) {
                    Text("Mark as Resolved")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun NearMissCard(entry: NearMissEntity, onClick: () -> Unit) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val dateString = dateFormat.format(Date(entry.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = entry.title, fontWeight = FontWeight.Bold, color = PplDarkBlue)
                if (entry.resolved) {
                    Text("Resolved", color = androidx.compose.ui.graphics.Color(0xFF4CAF50), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                } else {
                    Text("Pending", color = androidx.compose.ui.graphics.Color.Red, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Area: ${entry.plantArea} | Type: ${entry.type}", color = PplTextDark, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Description: ${entry.description}", color = PplTextDark, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Risk Score: ${entry.riskScore} (C:${entry.criticality} P:${entry.probability})", color = PplTextDark, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Reported On: $dateString", color = androidx.compose.ui.graphics.Color.Gray, style = MaterialTheme.typography.labelSmall)
        }
    }
}
