package com.ferhatozcelik.jetpackcomposetemplate.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ferhatozcelik.jetpackcomposetemplate.navigation.Screen
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplBrightBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplDarkBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplLightGrayBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplTextDark
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearMissDashboardScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Near Miss Dashboard",
                        fontWeight = FontWeight.Bold,
                        color = White
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = White
                        )
                    }
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Select Action",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PplTextDark,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Current Nearmisses Option
            Button(
                onClick = { navController.navigate(Screen.PastNearMisses.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PplTextDark,
                    contentColor = White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Current Nearmisses", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Report Nearmiss Option
            Button(
                onClick = { navController.navigate(Screen.NearMiss.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PplBrightBlue,
                    contentColor = White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Report Nearmiss", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
