package com.ferhatozcelik.jetpackcomposetemplate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ferhatozcelik.jetpackcomposetemplate.R
import com.ferhatozcelik.jetpackcomposetemplate.navigation.Screen
import com.ferhatozcelik.jetpackcomposetemplate.ui.components.MinimalistTextField
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplBrightBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplLightGrayBlue
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.PplTextDark
import com.ferhatozcelik.jetpackcomposetemplate.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
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
                text = "PPL Operations Portal",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PplTextDark,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            MinimalistTextField(
                value = username,
                onValueChange = { username = it },
                label = "Username"
            )

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = PplBrightBlue) },
                placeholder = { Text("Enter your password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PplBrightBlue,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = PplBrightBlue,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = PplBrightBlue,
                    textColor = PplTextDark,
                    containerColor = White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Navigate to Dashboard
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PplBrightBlue,
                    contentColor = White
                )
            ) {
                Text(text = "Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
