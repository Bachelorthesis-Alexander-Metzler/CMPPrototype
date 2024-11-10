package de.doubleslash.cmpprototype.domain.repository

import com.plusmobileapps.konnectivity.NetworkConnection
import kotlinx.coroutines.flow.StateFlow

interface NetworkStatusRepository {
    val currentNetworkConnectionState: StateFlow<NetworkConnection>
    val isConnectedState: StateFlow<Boolean>
}