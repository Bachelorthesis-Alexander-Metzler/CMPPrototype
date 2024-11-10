package de.doubleslash.cmpprototype.data.repository

import com.plusmobileapps.konnectivity.Konnectivity
import com.plusmobileapps.konnectivity.NetworkConnection
import de.doubleslash.cmpprototype.domain.repository.NetworkStatusRepository
import kotlinx.coroutines.flow.StateFlow

class NetworkStatusRepositoryImpl : NetworkStatusRepository {
    private val konnectivity = Konnectivity()

    override val currentNetworkConnectionState: StateFlow<NetworkConnection>
        get() = konnectivity.currentNetworkConnectionState
    override val isConnectedState: StateFlow<Boolean>
        get() = konnectivity.isConnectedState
}