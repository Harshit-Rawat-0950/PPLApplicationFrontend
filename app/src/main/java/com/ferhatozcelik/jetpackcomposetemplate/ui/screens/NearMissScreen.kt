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
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplDarkBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplLightGrayBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplTextDark
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearMissScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Near Miss Reporting",
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
                text = "Near Miss Section",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PplTextDark,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Reporting features and risk forms will be integrated here.",
                fontSize = 16.sp,
                color = PplTextDark,
                textAlign = TextAlign.Center
            )
        }
    }
}
