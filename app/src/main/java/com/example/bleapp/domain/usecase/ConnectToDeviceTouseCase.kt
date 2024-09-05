// domain/usecase/ConnectToDeviceUseCase.kt
package com.example.bleapp.domain.usecase

import android.bluetooth.BluetoothDevice
import com.example.bleapp.domain.BLERepository

class ConnectToDeviceUseCase( val repository: BLERepository) {
    operator fun invoke(device: BluetoothDevice) {
        repository.connectToDevice(device)
    }
}





// Define StartScanUseCase, StopScanUseCase, etc.
