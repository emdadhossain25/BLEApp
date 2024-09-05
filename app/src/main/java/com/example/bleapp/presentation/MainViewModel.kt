// presentation/MainViewModel.kt
package com.example.bleapp.presentation

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bleapp.domain.usecase.ConnectToDeviceUseCase
import com.example.bleapp.domain.usecase.StartScanUseCase
import com.example.bleapp.domain.usecase.StopScanUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val startScanUseCase: StartScanUseCase,
    private val stopScanUseCase: StopScanUseCase,
    private val connectToDeviceUseCase: ConnectToDeviceUseCase
) : ViewModel() {

    val devicesFlow: StateFlow<List<BluetoothDevice>> = startScanUseCase.repository.devicesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val connectionStateFlow: StateFlow<Boolean> = connectToDeviceUseCase.repository.connectionStateFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val characteristicFlow: StateFlow<String> = connectToDeviceUseCase.repository.characteristicFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    fun startScan() {
        startScanUseCase()
    }

    fun stopScan() {
        stopScanUseCase()
    }

    fun connectToDevice(device: BluetoothDevice) {
        connectToDeviceUseCase(device)
    }

    fun closeConnection() {
        connectToDeviceUseCase.repository.closeConnection()
    }
}
