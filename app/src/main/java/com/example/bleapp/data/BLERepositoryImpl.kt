// data/BLERepositoryImpl.kt
package com.example.bleapp.data

import android.bluetooth.BluetoothDevice
import com.example.bleapp.domain.BLERepository
import kotlinx.coroutines.flow.Flow

class BLERepositoryImpl(private val bleManager: BLEManager) : BLERepository {

    override val devicesFlow: Flow<List<BluetoothDevice>> = bleManager.devicesFlow
    override val connectionStateFlow: Flow<Boolean> = bleManager.connectionStateFlow
    override val characteristicFlow: Flow<String> = bleManager.characteristicFlow

    override fun startScan() {
        bleManager.startScan()
    }

    override fun stopScan() {
        bleManager.stopScan()
    }

    override fun connectToDevice(device: BluetoothDevice) {
        bleManager.connectToDevice(device)
    }

    override fun closeConnection() {
        bleManager.closeConnection()
    }
}
