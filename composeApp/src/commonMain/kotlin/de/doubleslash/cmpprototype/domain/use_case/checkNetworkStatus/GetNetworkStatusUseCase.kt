package de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus

import com.plusmobileapps.konnectivity.NetworkConnection
import de.doubleslash.cmpprototype.domain.repository.deviceApi.NetworkStatusRepository
import kotlinx.coroutines.flow.StateFlow

class GetNetworkStatusUseCase(
    private val networkStatusRepository: NetworkStatusRepository
) {
    operator fun invoke(): StateFlow<NetworkConnection> {
        return networkStatusRepository.currentNetworkConnectionState
    }
}