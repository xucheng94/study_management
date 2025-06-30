package com.example.mystudy


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
            composable("home") { DailyTaskScreen() }
            composable("profile") {ProfileScreen()  }
        }
    }
}



@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry=navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute=="home", onClick = {
            navController.navigate("home") },
            icon = {Icon(Icons.Filled.Home, contentDescription = "Home")},
            label ={Text("Home")}
            )
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") },
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTaskScreen() {
    val tasks=listOf("report","meeting","assignment")
    var showDialog by remember { mutableStateOf(false) }
    var taskname by remember {mutableStateOf("")}
    Scaffold(floatingActionButton = {FloatingActionButton(onClick = {showDialog=true})
    {Icon(Icons.Filled.Add, contentDescription = "Add task") }
    })
    {innerPadding
        ->Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
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

    }  }
    if (showDialog){
        AlertDialog(onDismissRequest = {showDialog=false}, title = {Text("Add Task")},
         text = {Column { OutlinedTextField(
             value = taskname,
             onValueChange = {taskname=it},
             label = {Text("Task Name")},
             singleLine = true
         )
         }
                },
            confirmButton = {
             TextButton(onClick = {showDialog=false}) { Text("Cancel")}
            }
            )
    }
}

@Composable
fun ProfileScreen(){
    Column(modifier = Modifier.padding(16.dp)) {
        Text("This is profile Screen")
    }
}