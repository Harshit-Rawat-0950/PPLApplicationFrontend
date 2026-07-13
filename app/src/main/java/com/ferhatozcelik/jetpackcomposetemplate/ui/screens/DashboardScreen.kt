package com.ferhatozcelik.jetpackcomposetemplate.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.ferhatozcelik.jetpackcomposetemplate.navigation.Screen
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplBrightBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplDarkBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplLightGrayBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplTextDark
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.White
import com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Dashboard",
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome back, ${viewModel.userName}!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PplTextDark,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Log Book Dashboard Option
            Button(
                onClick = { navController.navigate(Screen.LogbookDashboard.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PplBrightBlue,
                    contentColor = White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Shift Logbook", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Near Miss Option
            Button(
                onClick = { navController.navigate(Screen.NearMissDashboard.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PplTextDark,
                    contentColor = White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Near Miss Reporting", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Work Permit Option
            Button(
                onClick = { navController.navigate(Screen.WorkPermit.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PplDarkBlue,
                    contentColor = White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Work Permits (ePTW)", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
