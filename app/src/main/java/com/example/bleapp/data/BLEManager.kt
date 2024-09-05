// data/BLEManager.kt
package com.example.bleapp.data

import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BLEManager(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter
    private val bluetoothLeScanner: BluetoothLeScanner
    private var bluetoothGatt: BluetoothGatt? = null

    private val _devicesFlow = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val devicesFlow: StateFlow<List<BluetoothDevice>> get() = _devicesFlow

    private val _connectionStateFlow = MutableStateFlow(false)
    val connectionStateFlow: StateFlow<Boolean> get() = _connectionStateFlow

    private val _characteristicFlow = MutableStateFlow("")
    val characteristicFlow: StateFlow<String> get() = _characteristicFlow

    init {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    }

    fun startScan() {
        bluetoothLeScanner.startScan(scanCallback)
    }

    fun stopScan() {
        bluetoothLeScanner.stopScan(scanCallback)
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            result.device?.let { device ->
                val currentList = _devicesFlow.value.toMutableList()
                if (!currentList.contains(device)) {
                    currentList.add(device)
                    _devicesFlow.value = currentList
                }
            }
        }
    }

    fun connectToDevice(device: BluetoothDevice) {
        bluetoothGatt = device.connectGatt(context, false, gattCallback)
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                _connectionStateFlow.value = true
                gatt.discoverServices()
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                _connectionStateFlow.value = false
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val service =
                    gatt.getService(gatt.services.get(0).uuid) // Replace with your service UUID
                val characteristic =
                    service?.getCharacteristic(gatt.services.get(0).characteristics.get(0).uuid) // Replace with your characteristic UUID
                gatt.readCharacteristic(characteristic)
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                _characteristicFlow.value = characteristic.value?.let { String(it) } ?: ""
            }
        }
    }

    fun closeConnection() {
        bluetoothGatt?.close()
        bluetoothGatt = null
    }
}
