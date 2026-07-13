package com.ferhatozcelik.jetpackcomposetemplate.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ferhatozcelik.jetpackcomposetemplate.ui.components.MinimalistTextField
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.*
import com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel.NearMissViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearMissScreen(
    navController: NavController,
    viewModel: NearMissViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var expandedArea by remember { mutableStateOf(false) }
    var selectedArea by remember { mutableStateOf("Select Area") }
    
    var expandedType by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf("Select Type") }
    val types = listOf("Unsafe Action", "Unsafe Site")
    
    var criticality by remember { mutableStateOf(0) }
    var probability by remember { mutableStateOf(0) }
    
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

    val areas = listOf("PAP", "SAP", "DAP", "Offsite", "Jetty")

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        capturedImage = bitmap
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(text = "Report Near Miss", fontWeight = FontWeight.Bold, color = White) 
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MinimalistTextField(
                value = title,
                onValueChange = { title = it },
                label = "Title",
                singleLine = true
            )

            MinimalistTextField(
                value = description,
                onValueChange = { description = it },
                label = "Description of Near Miss",
                singleLine = false
            )

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
                    label = { Text("Plant Area") },
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

            Spacer(modifier = Modifier.height(16.dp))

            // Type Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedType,
                onExpandedChange = { expandedType = !expandedType },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Near Miss Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedType,
                    onDismissRequest = { expandedType = false }
                ) {
                    types.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedType = selectionOption
                                expandedType = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            // Risk Assessment Matrix (RAM)
            Text("Risk Assessment Matrix (RAM)", fontWeight = FontWeight.Bold, color = PplTextDark)
            Spacer(modifier = Modifier.height(8.dp))
            
            // Criticality Selector
            Text("Criticality (1-5)", color = PplTextDark, modifier = Modifier.align(Alignment.Start))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                (1..5).forEach { level ->
                    RamChip(
                        level = level, 
                        isSelected = criticality == level, 
                        onClick = { criticality = level }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Probability Selector
            Text("Probability (1-5)", color = PplTextDark, modifier = Modifier.align(Alignment.Start))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                (1..5).forEach { level ->
                    RamChip(
                        level = level, 
                        isSelected = probability == level, 
                        onClick = { probability = level }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            // Display Total Risk Score
            val riskScore = criticality * probability
            if (criticality > 0 && probability > 0) {
                val riskColor = when {
                    riskScore <= 4 -> Color(0xFF4CAF50) // Green
                    riskScore <= 10 -> Color(0xFFFFC107) // Yellow
                    else -> Color(0xFFF44336) // Red
                }
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(riskColor.copy(alpha = 0.2f))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Total Risk Score: $riskScore", 
                        fontWeight = FontWeight.Bold, 
                        color = riskColor,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { cameraLauncher.launch(null) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PplDarkBlue,
                    contentColor = White
                )
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Photo")
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "Attach Photo", fontWeight = FontWeight.Bold)
            }

            capturedImage?.let { bitmap ->
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Captured image",
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
            }

            Button(
                onClick = {
                    if (title.isBlank() || selectedArea == "Select Area" || selectedType == "Select Type" || criticality == 0 || probability == 0) {
                        Toast.makeText(context, "Please fill all fields and select RAM levels", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.insertNearMiss(
                            title = title,
                            description = description,
                            plantArea = selectedArea,
                            type = selectedType,
                            criticality = criticality,
                            probability = probability,
                            photoBitmap = capturedImage
                        )
                        Toast.makeText(context, "Near Miss Reported Successfully", Toast.LENGTH_SHORT).show()
                        navController.navigateUp()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PplBrightBlue,
                    contentColor = White
                )
            ) {
                Text(text = "Submit Report", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RamChip(level: Int, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) PplBrightBlue else Color.LightGray
    val textColor = if (isSelected) White else Color.DarkGray
    
    Box(
        modifier = Modifier
            .size(48.dp)    
            .clip(CircleShape)
            .background(bgColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = level.toString(),
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}
