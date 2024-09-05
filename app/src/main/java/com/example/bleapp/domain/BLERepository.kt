// domain/BLERepository.kt
package com.example.bleapp.domain

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow

interface BLERepository {
    val devicesFlow: Flow<List<BluetoothDevice>>
    val connectionStateFlow: Flow<Boolean>
    val characteristicFlow: Flow<String>

    fun startScan()
    fun stopScan()
    fun connectToDevice(device: BluetoothDevice)
    fun closeConnection()
}
