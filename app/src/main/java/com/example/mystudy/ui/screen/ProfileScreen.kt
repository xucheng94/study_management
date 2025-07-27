package com.example.mystudy.ui.screen
import com.example.mystudy.viewmodel.ProfileViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    // variables
    val currentDate = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    var showLoginDialog by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginMessage by remember { mutableStateOf<String?>(null) }


    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val loggedInName by viewModel.loggedInName.collectAsState()


    // 主界面内容
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("👤 User Profile", style = MaterialTheme.typography.headlineMedium)

        Text("🧑 Name: ${if (isLoggedIn) loggedInName else "Not logged in"}")
        Text("📅 current date: $currentDate")

        Divider()

        // log in
        Button(onClick = {
            showLoginDialog = true }) {
            Text("🔐 Log In")
        }

        // show successfully log in
        loginMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.primary)
        }

        Divider()

        Text("⚙ App Version: 1.0.0")
        Text("🌓 Theme: Dark / Light")
        if (isLoggedIn) {
            Button(onClick = {
                viewModel.logout()
                loginMessage = "👋 Logged out successfully."
            }) {
                Text("🚪 Log Out")
            }
        }

    }

    // log in page
    if (showLoginDialog) {
        AlertDialog(
            onDismissRequest = { showLoginDialog = false },
            title = { Text("Login") },
            text = {
                Column {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
                        viewModel.login(username)

                        loginMessage = "✅ Welcome, $username!"
                        showLoginDialog = false
                    } else {
                        loginMessage = "❌ Please enter both fields"
                    }
                }) {
                    Text("Login")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLoginDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
