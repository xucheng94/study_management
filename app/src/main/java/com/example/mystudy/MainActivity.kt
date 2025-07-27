package com.example.mystudy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
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
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.mystudy.model.TaskDatabase
import com.example.mystudy.model.TaskRepository
import com.example.mystudy.viewmodel.TaskViewModel
import com.example.mystudy.viewmodel.TaskViewModelFactory
import com.example.mystudy.ui.screen.SettingsScreen
import com.example.mystudy.ui.screen.StatsScreen
import androidx.compose.material.icons.filled.BarChart
import com.example.mystudy.ui.screen.ProfileScreen
import com.example.mystudy.ui.theme.MYstudyTheme
import com.example.mystudy.viewmodel.ProfileViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

import androidx.compose.material.icons.filled.Settings


class MainActivity: ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        //get  ProfileViewModel
        val profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        //get database sample
        val database = TaskDatabase.getDatabase(applicationContext)

       // build Repository
        val repository = TaskRepository(database.taskDao())

       // build ViewModelFactory
        val viewModelFactory = TaskViewModelFactory(repository)

       // get ViewModel sample
        val viewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) } // 添加主题状态

            MYstudyTheme(darkTheme = isDarkTheme) {
                MainScreen(
                    viewModel = viewModel,

                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: TaskViewModel, isDarkTheme: Boolean, onThemeToggle: () -> Unit){
    val navController= rememberNavController()
    val owner = LocalViewModelStoreOwner.current

    Scaffold(
        topBar = {TopAppBar(title = {Text("MyStudy")},
            actions = {IconButton(onClick = {})
            {Icon(Icons.Filled.AccountCircle, contentDescription = "Profile") }})},
        bottomBar = {BottomNavBar(navController)}
    ){paddingValues->
        NavHost(navController = navController, startDestination = "home",
            modifier = Modifier.padding(paddingValues)) {
            composable("home") { DailyTaskScreen(viewModel) }
            composable("profile") {   owner?.let {
                val profileViewModel: ProfileViewModel = viewModel(it)
                ProfileScreen(profileViewModel)
            }     }
            composable("settings") {
                SettingsScreen(
                    onClearAllTasks = {
                        viewModel.clearAll() // 调用 TaskViewModel 中的清空方法
                    },
                    isDarkTheme = isDarkTheme,
                    onToggleDarkMode = onThemeToggle
                )
            }
            composable("stats") {
                StatsScreen(viewModel)
            }
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
        NavigationBarItem(
            selected = currentRoute == "settings",
            onClick = { navController.navigate("settings") },
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text("Settings") }
        )
        NavigationBarItem(
            selected = currentRoute == "stats",
            onClick = { navController.navigate("stats") },
            icon = { Icon(Icons.Filled.BarChart, contentDescription = "Stats") },
            label = { Text("Stats") }
        )


    }

}




