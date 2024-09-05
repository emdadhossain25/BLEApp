
// presentation/ui/ComposeUI.kt
package com.example.bleapp.presentation.ui

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bleapp.presentation.MainViewModel

@Composable
fun BLEAppScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "scanner") {
        composable("scanner") { ScannerScreen(viewModel, navController) }
        composable("details") { DeviceDetailsScreen(viewModel) }
    }
}

@Composable
fun ScannerScreen(viewModel: MainViewModel, navController: NavController) {
    val devices by viewModel.devicesFlow.collectAsState()
    val isConnected by viewModel.connectionStateFlow.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("BLE Device Scanner", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(devices) { device ->
                BLEDeviceItem(device, onClick = {
                    viewModel.connectToDevice(device)
                    navController.navigate("details")
                })
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (isConnected) "Connected" else "Disconnected",
            color = if (isConnected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun DeviceDetailsScreen(viewModel: MainViewModel) {
    val characteristicValue by viewModel.characteristicFlow.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Device Details", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
        Text("Characteristic Value: $characteristicValue", fontSize = 16.sp)
    }
}

@SuppressLint("MissingPermission")
@Composable
fun BLEDeviceItem(device: BluetoothDevice, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = device.name ?: "Unnamed Device", fontSize = 18.sp)
            Text(text = device.address, fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)
        }
    }
}
