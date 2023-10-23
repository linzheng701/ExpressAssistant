package com.base.expressassistant

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.base.expressassistant.ui.components.AddScreen
import com.base.expressassistant.ui.components.ListScreen
import com.base.expressassistant.ui.components.SettingScreen
import com.base.expressassistant.ui.theme.ExpressAssistantTheme
import com.github.gzuliyujiang.oaid.DeviceIdentifier


class MainActivity : ComponentActivity() {
    private var privacyPolicyAgreed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //注意APP合规性，若最终用户未同意隐私政策则不要调用
        if (privacyPolicyAgreed) {
            DeviceIdentifier.register(application)
        }
        setContent {
            ExpressAssistantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainPage()
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        privacyPolicyAgreed = true
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object AddScreen : Screen("AddScreen", R.string.add, Icons.Filled.Add)
    object ListScreen : Screen("ListScreen", R.string.list, Icons.Filled.List)
    object SettingScreen : Screen("SettingScreen", R.string.setting, Icons.Filled.Settings)
}

val bottomNavItems = listOf(
    Screen.AddScreen,
    Screen.ListScreen,
    Screen.SettingScreen,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage() {
    val navController = rememberNavController()
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavItems.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = {
                            Text(stringResource(screen.resourceId))
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.AddScreen.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.AddScreen.route) {
                AddScreen(
                    navController,
                    address,
                    { address = it },
                    code,
                    { code = it },
                    phoneNumber,
                    { phoneNumber = it },
                    name,
                    { name = it })
            }
            composable(Screen.ListScreen.route) {
                ListScreen(address, code, phoneNumber, name)
            }
            composable(Screen.SettingScreen.route) {
                SettingScreen()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExpressAssistantTheme {
        MainPage()
    }
}
