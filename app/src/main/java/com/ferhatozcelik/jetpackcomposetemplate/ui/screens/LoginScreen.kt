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
fun LoginScreen(
    navController: NavController,
    viewModel: com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel.AuthViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel.LoginState.Success) {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

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
            
            if (loginState is com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel.LoginState.Error) {
                Text(
                    text = (loginState as com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel.LoginState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = { viewModel.login(username, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PplBrightBlue,
                    contentColor = White
                ),
                enabled = loginState !is com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel.LoginState.Loading
            ) {
                if (loginState is com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel.LoginState.Loading) {
                    CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
