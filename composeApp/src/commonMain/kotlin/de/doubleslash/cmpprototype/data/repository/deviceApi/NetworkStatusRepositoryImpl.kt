package de.doubleslash.cmpprototype.data.repository.deviceApi

import com.plusmobileapps.konnectivity.Konnectivity
import com.plusmobileapps.konnectivity.NetworkConnection
import de.doubleslash.cmpprototype.domain.repository.deviceApi.NetworkStatusRepository
import kotlinx.coroutines.flow.StateFlow

/** Repository for checking the network status.
 * uses konnectivity library to retrieve data */
class NetworkStatusRepositoryImpl : NetworkStatusRepository {
    private val konnectivity = Konnectivity()

    override val currentNetworkConnectionState: StateFlow<NetworkConnection>
        get() = konnectivity.currentNetworkConnectionState
    override val isConnectedState: StateFlow<Boolean>
        get() = konnectivity.isConnectedState
}