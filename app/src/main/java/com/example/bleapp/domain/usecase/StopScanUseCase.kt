package com.example.bleapp.domain.usecase

import com.example.bleapp.domain.BLERepository


class StopScanUseCase(val repository: BLERepository) {
    operator fun invoke() {
        repository.stopScan()
    }
}