package com.ferhatozcelik.jetpackcomposetemplate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.AssetData
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.ShiftLogbookEntity
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.*
import com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel.ShiftLogbookViewModel
import kotlinx.coroutines.flow.firstOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogbookDetailScreen(
    navController: NavController,
    logbookId: Int,
    viewModel: ShiftLogbookViewModel = hiltViewModel()
) {
    val allLogbooks by viewModel.allLogbooks.collectAsState()
    var logbook by remember { mutableStateOf<ShiftLogbookEntity?>(null) }

    LaunchedEffect(allLogbooks, logbookId) {
        logbook = allLogbooks.find { it.id == logbookId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Log Book Details", fontWeight = FontWeight.Bold, color = White) },
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
        if (logbook == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val data = logbook!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Shift Info", fontWeight = FontWeight.Bold, color = PplDarkBlue, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Date: ${data.date}", color = PplTextDark, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Shift: ${data.shift}", color = PplTextDark)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Area: ${data.area}", color = PplTextDark)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Submitted By: ${data.submitterId}", color = PplTextDark)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Assets", fontWeight = FontWeight.Bold, color = PplDarkBlue, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))

                data.assets.forEachIndexed { index, asset ->
                    AssetDetailCard(index = index, asset = asset)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun AssetDetailCard(index: Int, asset: AssetData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Asset #${index + 1}: ${asset.assetTag}", fontWeight = FontWeight.Bold, color = PplBrightBlue)
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Standing Alarms & Parameters:", fontWeight = FontWeight.Bold, color = PplTextDark, fontSize = 14.sp)
            Text(asset.standingAlarms.ifBlank { "None" }, color = Color.DarkGray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Maintenance Status:", fontWeight = FontWeight.Bold, color = PplTextDark, fontSize = 14.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(PplBrightBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = asset.maintenanceStatus.toString(),
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when(asset.maintenanceStatus) {
                        1 -> "Urgent Maintenance Needed"
                        5 -> "Running Perfectly"
                        else -> "Moderate Maintenance Needed"
                    },
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Maintenance Done:", fontWeight = FontWeight.Bold, color = PplTextDark, fontSize = 14.sp)
            Text(asset.maintenanceDone.ifBlank { "None" }, color = Color.DarkGray, fontSize = 14.sp)
        }
    }
}
