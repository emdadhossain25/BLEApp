package com.example.bleapp.domain.usecase

import com.example.bleapp.domain.BLERepository

class StartScanUseCase(val repository: BLERepository) {
    operator fun invoke() {
        repository.startScan()
    }
}