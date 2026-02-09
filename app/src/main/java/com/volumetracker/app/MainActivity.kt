package com.volumetracker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.volumetracker.app.presentation.navigation.NavGraph
import com.volumetracker.app.presentation.navigation.Screen
import com.volumetracker.app.presentation.theme.VolumeTrackerTheme
import com.volumetracker.app.security.EncryptedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var preferencesManager: EncryptedPreferencesManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            val disclaimerAccepted = remember {
                mutableStateOf(
                    preferencesManager.getBoolean(EncryptedPreferencesManager.KEY_DISCLAIMER_ACCEPTED)
                )
            }
            
            val darkTheme = preferencesManager.getBoolean(
                EncryptedPreferencesManager.KEY_DARK_THEME,
                defaultValue = true
            )
            
            VolumeTrackerTheme(darkTheme = darkTheme) {
                if (disclaimerAccepted.value) {
                    MainScreen()
                } else {
                    DisclaimerScreen(
                        onAccept = {
                            preferencesManager.putBoolean(
                                EncryptedPreferencesManager.KEY_DISCLAIMER_ACCEPTED,
                                true
                            )
                            disclaimerAccepted.value = true
                        },
                        onDecline = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                val items = listOf(
                    BottomNavItem(Screen.Dashboard.route, "Dashboard", Icons.Default.Dashboard),
                    BottomNavItem(Screen.Search.route, "Search", Icons.Default.Search),
                    BottomNavItem(Screen.Watchlist.route, "Watchlist", Icons.Default.Star),
                    BottomNavItem(Screen.Settings.route, "Settings", Icons.Default.Settings)
                )
                
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(Screen.Dashboard.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavGraph(
            navController = navController,
            startDestination = Screen.Dashboard.route
        )
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun DisclaimerScreen(
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Important Disclaimer",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "This app is for informational purposes only. Not financial advice. " +
                        "Memecoin trading is extremely high-risk. Past volume spikes do not " +
                        "guarantee future gains. Always do your own research and never invest " +
                        "more than you can afford to lose.",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onAccept,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("I Understand")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedButton(
                onClick = onDecline,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Exit")
            }
        }
    }
}
