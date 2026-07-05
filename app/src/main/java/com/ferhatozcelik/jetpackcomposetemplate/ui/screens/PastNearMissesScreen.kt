package com.ferhatozcelik.jetpackcomposetemplate.ui.screens

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
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.NearMissEntity
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplDarkBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplLightGrayBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplTextDark
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.White
import com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel.NearMissViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastNearMissesScreen(
    navController: NavController,
    viewModel: NearMissViewModel = hiltViewModel()
) {
    val nearMisses by viewModel.allNearMisses.collectAsState(initial = emptyList())

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
        if (nearMisses.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No Near Misses Reported", color = PplTextDark)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(nearMisses) { entry ->
                    NearMissCard(entry = entry)
                }
            }
        }
    }
}

@Composable
fun NearMissCard(entry: NearMissEntity) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val dateString = dateFormat.format(Date(entry.timestamp))

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
            Text(text = entry.title, fontWeight = FontWeight.Bold, color = PplDarkBlue)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Area: ${entry.plantArea}", color = PplTextDark, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Description: ${entry.description}", color = PplTextDark, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Risk Score: ${entry.riskScore} (C:${entry.criticality} P:${entry.probability})", color = PplTextDark, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Reported On: $dateString", color = androidx.compose.ui.graphics.Color.Gray, style = MaterialTheme.typography.labelSmall)
        }
    }
}
