package com.example.mystudy


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mystudy.ui.theme.MYstudyTheme



class MainActivity: ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MYstudyTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(){
    val navController= rememberNavController()
    Scaffold(
        topBar = {TopAppBar(title = {Text("MyStudy")},
            actions = {IconButton(onClick = {})
            {Icon(Icons.Filled.AccountCircle, contentDescription = "Profile") }})},
        bottomBar = {BottomNavBar(navController)}
    ){paddingValues->
        NavHost(navController = navController, startDestination = "home",
            modifier = Modifier.padding(paddingValues)) {
            composable("home") { DailyTaskScreen()  }
        }
    }
}



@Composable
fun BottomNavBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(selected = true, onClick = {
            navController.navigate("home") },
            icon = {Icon(Icons.Filled.Home, contentDescription = "Home")},
            label ={Text("Home")}
            )
    }

}


@Composable
fun DailyTaskScreen() {
    val tasks=listOf("report","meeting","assignment")
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "today's tasks",
            style=MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        tasks.forEach { tasks->
            ElevatedCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
            {Row(modifier = Modifier.padding(16.dp)) {
                Icon(imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp)
                )
                Text(tasks)
            }  }
        }

    }
}

