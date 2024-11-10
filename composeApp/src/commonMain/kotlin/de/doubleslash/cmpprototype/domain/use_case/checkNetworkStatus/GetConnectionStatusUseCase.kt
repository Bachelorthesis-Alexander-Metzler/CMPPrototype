package de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus

import de.doubleslash.cmpprototype.domain.repository.deviceApi.NetworkStatusRepository
import kotlinx.coroutines.flow.StateFlow

class GetConnectionStatusUseCase(
    private val networkStatusRepository: NetworkStatusRepository
) {
    operator fun invoke(): StateFlow<Boolean> {
        return networkStatusRepository.isConnectedState
    }
}