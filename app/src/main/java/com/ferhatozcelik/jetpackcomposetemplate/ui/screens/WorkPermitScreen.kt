package com.ferhatozcelik.jetpackcomposetemplate.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.WorkPermitDto
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplBrightBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplDarkBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplLightGrayBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplTextDark
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.White
import com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel.WorkPermitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkPermitScreen(
    navController: NavController,
    viewModel: WorkPermitViewModel = hiltViewModel()
) {
    val role = viewModel.userRole
    val permits by viewModel.workPermits.collectAsState()
    
    // View filtering based on role
    val displayPermits = when(role) {
        "WORKER" -> permits.filter { it.applicantId == viewModel.userId }
        "OPERATOR" -> permits.filter { it.assignedOperatorId == viewModel.userId }
        else -> permits // Maintenance Heads see all
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Work Permits (ePTW)", fontWeight = FontWeight.Bold, color = White) },
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
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            if (role == "WORKER") {
                WorkPermitApplyForm(viewModel)
                Spacer(modifier = Modifier.height(24.dp))
                Text("Your Permits", fontWeight = FontWeight.Bold, color = PplDarkBlue)
                Spacer(modifier = Modifier.height(8.dp))
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(displayPermits) { permit ->
                    WorkPermitCard(permit, role, viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkPermitApplyForm(viewModel: WorkPermitViewModel) {
    var assetName by remember { mutableStateOf("") }
    var taskDesc by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    
    var expandedShift by remember { mutableStateOf(false) }
    var selectedShift by remember { mutableStateOf("Select Shift") }
    val shifts = listOf("Morning", "Afternoon", "Night", "General")

    var expandedSite by remember { mutableStateOf(false) }
    var selectedSite by remember { mutableStateOf("Select Site") }
    val sites = listOf("PAP", "SAP", "DAP", "Offsite", "Jetty")

    var expandedApprover by remember { mutableStateOf(false) }
    var selectedApproverId by remember { mutableStateOf("") }
    var selectedApproverName by remember { mutableStateOf("Select Approver") }

    val heads by viewModel.maintenanceHeads.collectAsState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Apply for Permit", fontWeight = FontWeight.Bold, color = PplDarkBlue)
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = assetName,
                onValueChange = { assetName = it },
                label = { Text("Asset Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = taskDesc,
                onValueChange = { taskDesc = it },
                label = { Text("Task Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

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

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expandedSite,
                onExpandedChange = { expandedSite = !expandedSite },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedSite,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Site") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSite) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedSite,
                    onDismissRequest = { expandedSite = false }
                ) {
                    sites.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedSite = selectionOption
                                expandedSite = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expandedApprover,
                onExpandedChange = { expandedApprover = !expandedApprover },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedApproverName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Approver") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedApprover) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedApprover,
                    onDismissRequest = { expandedApprover = false }
                ) {
                    heads.forEach { user ->
                        DropdownMenuItem(
                            text = { Text(user.name) },
                            onClick = {
                                selectedApproverName = user.name
                                selectedApproverId = user.employeeId
                                expandedApprover = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (assetName.isNotBlank() && taskDesc.isNotBlank() && date.isNotBlank() && selectedApproverId.isNotBlank() && selectedShift != "Select Shift" && selectedSite != "Select Site") {
                        viewModel.applyForPermit(assetName, taskDesc, date, selectedShift, selectedSite, selectedApproverId)
                        assetName = ""
                        taskDesc = ""
                        date = ""
                        selectedShift = "Select Shift"
                        selectedSite = "Select Site"
                        selectedApproverName = "Select Approver"
                        selectedApproverId = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PplBrightBlue)
            ) {
                Text("Submit")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkPermitCard(permit: WorkPermitDto, role: String, viewModel: WorkPermitViewModel) {
    var showApproveDialog by remember { mutableStateOf(false) }
    var showLotoDialog by remember { mutableStateOf(false) }
    var showCloseDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (role == "MAINTENANCE_HEAD" && permit.status == "PENDING") {
                    showApproveDialog = true
                } else if (role == "OPERATOR" && permit.status == "APPROVED") {
                    showLotoDialog = true
                } else if (role == "WORKER" && permit.status == "LOTO_VERIFIED") {
                    showCloseDialog = true
                }
            },
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Asset: ${permit.assetName}", fontWeight = FontWeight.Bold, color = PplDarkBlue)
                val statusColor = when (permit.status) {
                    "APPROVED" -> Color(0xFF4CAF50)
                    "PENDING" -> Color(0xFFFFC107)
                    "REJECTED" -> Color(0xFFF44336)
                    "LOTO_VERIFIED" -> Color(0xFF2196F3)
                    "CLOSED" -> Color(0xFF9E9E9E)
                    else -> PplTextDark
                }
                Text(permit.status, color = statusColor, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Task: ${permit.taskDescription}", color = PplTextDark)
            Text("Date: ${permit.date} | Shift: ${permit.shift}", color = PplTextDark, style = MaterialTheme.typography.bodySmall)
            
            if (permit.status != "PENDING" && permit.status != "REJECTED") {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Assigned to: ${permit.assignedOperatorId}", color = PplBrightBlue, style = MaterialTheme.typography.bodySmall)
                if (permit.instructions != null) {
                    Text("Instructions: ${permit.instructions}", color = PplTextDark, style = MaterialTheme.typography.bodySmall)
                }
            }
            
            if (permit.lotoVerified) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("LOTO: Verified", color = Color(0xFF4CAF50), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                Text("Gas: O2=${permit.o2Reading}%, LEL=${permit.lelReading}%, CO=${permit.coReading}ppm, H2S=${permit.h2sNh3Reading}ppm", color = PplTextDark, style = MaterialTheme.typography.bodySmall)
            }
        }
    }

    if (showApproveDialog) {
        var expandedOp by remember { mutableStateOf(false) }
        var selectedOpId by remember { mutableStateOf("") }
        var selectedOpName by remember { mutableStateOf("Select Operator") }
        var instructions by remember { mutableStateOf("") }

        val ops by viewModel.operators.collectAsState()

        AlertDialog(
            onDismissRequest = { showApproveDialog = false },
            title = { Text("Approve or Reject Permit") },
            text = {
                Column {
                    ExposedDropdownMenuBox(
                        expanded = expandedOp,
                        onExpandedChange = { expandedOp = !expandedOp },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedOpName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Assign Operator") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedOp) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedOp,
                            onDismissRequest = { expandedOp = false }
                        ) {
                            ops.forEach { op ->
                                DropdownMenuItem(
                                    text = { Text(op.name) },
                                    onClick = {
                                        selectedOpName = op.name
                                        selectedOpId = op.employeeId
                                        expandedOp = false
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = instructions,
                        onValueChange = { instructions = it },
                        label = { Text("Instructions") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (selectedOpId.isNotBlank()) {
                        viewModel.approvePermit(permit.id, selectedOpId, instructions)
                        showApproveDialog = false
                    }
                }) {
                    Text("Approve & Assign")
                }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = { 
                        viewModel.rejectPermit(permit.id)
                        showApproveDialog = false 
                    }) {
                        Text("Reject", color = Color(0xFFF44336))
                    }
                    TextButton(onClick = { showApproveDialog = false }) {
                        Text("Cancel")
                    }
                }
            }
        )
    }

    if (showLotoDialog) {
        var lotoVerified by remember { mutableStateOf(false) }
        var o2 by remember { mutableStateOf("") }
        var lel by remember { mutableStateOf("") }
        var co by remember { mutableStateOf("") }
        var h2s by remember { mutableStateOf("") }
        var pin by remember { mutableStateOf("") }

        val isO2Warning = o2.toDoubleOrNull()?.let { it < 19.5 || it > 23.5 } ?: false
        val isLelWarning = lel.toDoubleOrNull()?.let { it > 10.0 } ?: false
        val isCoWarning = co.toDoubleOrNull()?.let { it > 35.0 } ?: false
        val isH2sWarning = h2s.toDoubleOrNull()?.let { it > 10.0 } ?: false

        AlertDialog(
            onDismissRequest = { showLotoDialog = false },
            title = { Text("LOTO & Gas Verification") },
            text = {
                LazyColumn {
                    item {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Checkbox(checked = lotoVerified, onCheckedChange = { lotoVerified = it })
                            Text("I have physically verified the LOTO.")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = o2,
                            onValueChange = { o2 = it },
                            label = { Text("O2 Reading (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = isO2Warning,
                            supportingText = if(isO2Warning) { { Text("Warning: Outside safe range (19.5-23.5)!") } } else null,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = lel,
                            onValueChange = { lel = it },
                            label = { Text("LEL Reading (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = isLelWarning,
                            supportingText = if(isLelWarning) { { Text("Warning: Exceeds safe limit (>10%)!") } } else null,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = co,
                            onValueChange = { co = it },
                            label = { Text("CO Reading (ppm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = isCoWarning,
                            supportingText = if(isCoWarning) { { Text("Warning: Exceeds safe limit (>35ppm)!") } } else null,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = h2s,
                            onValueChange = { h2s = it },
                            label = { Text("H2S/NH3 Reading (ppm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = isH2sWarning,
                            supportingText = if(isH2sWarning) { { Text("Warning: Exceeds safe limit (>10ppm)!") } } else null,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = pin,
                            onValueChange = { pin = it },
                            label = { Text("Digital Signature (PIN)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (lotoVerified && pin.isNotBlank() && o2.isNotBlank() && lel.isNotBlank() && co.isNotBlank() && h2s.isNotBlank()) {
                        viewModel.verifyLoto(permit.id, true, o2.toDoubleOrNull(), lel.toDoubleOrNull(), co.toDoubleOrNull(), h2s.toDoubleOrNull(), pin)
                        showLotoDialog = false
                    }
                }) {
                    Text("Verify & Sign")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLotoDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showCloseDialog) {
        var toolsRemoved by remember { mutableStateOf(false) }
        var pin by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showCloseDialog = false },
            title = { Text("Close Permit") },
            text = {
                Column {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Checkbox(checked = toolsRemoved, onCheckedChange = { toolsRemoved = it })
                        Text("I confirm all tools are removed and the area is clear.")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = pin,
                        onValueChange = { pin = it },
                        label = { Text("Requesting Authority (PIN)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (toolsRemoved && pin.isNotBlank()) {
                        viewModel.closePermit(permit.id, pin)
                        showCloseDialog = false
                    }
                }) {
                    Text("Close Permit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCloseDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
