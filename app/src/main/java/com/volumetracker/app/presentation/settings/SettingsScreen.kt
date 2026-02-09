package com.volumetracker.app.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.volumetracker.app.security.EncryptedPreferencesManager
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val darkTheme by viewModel.darkTheme.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Enable notifications")
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { viewModel.setNotificationsEnabled(it) }
                )
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Dark theme")
                Switch(
                    checked = darkTheme,
                    onCheckedChange = { viewModel.setDarkTheme(it) }
                )
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium
            )
            
            Text(
                text = "Version 1.0.0",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

// Simple ViewModel for settings
@dagger.hilt.android.lifecycle.HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: EncryptedPreferencesManager
) : androidx.lifecycle.ViewModel() {
    
    val notificationsEnabled = kotlinx.coroutines.flow.MutableStateFlow(
        preferencesManager.getBoolean(EncryptedPreferencesManager.KEY_NOTIFICATIONS_ENABLED, true)
    )
    
    val darkTheme = kotlinx.coroutines.flow.MutableStateFlow(
        preferencesManager.getBoolean(EncryptedPreferencesManager.KEY_DARK_THEME, true)
    )
    
    fun setNotificationsEnabled(enabled: Boolean) {
        preferencesManager.putBoolean(EncryptedPreferencesManager.KEY_NOTIFICATIONS_ENABLED, enabled)
        notificationsEnabled.value = enabled
    }
    
    fun setDarkTheme(enabled: Boolean) {
        preferencesManager.putBoolean(EncryptedPreferencesManager.KEY_DARK_THEME, enabled)
        darkTheme.value = enabled
    }
}
