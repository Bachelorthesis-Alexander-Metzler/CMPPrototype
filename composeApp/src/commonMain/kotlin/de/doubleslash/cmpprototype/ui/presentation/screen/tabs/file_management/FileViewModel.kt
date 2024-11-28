package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.file_management

import cafe.adriel.voyager.core.model.ScreenModel
import com.plusmobileapps.konnectivity.NetworkConnection
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetConnectionStatusUseCase
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetNetworkStatusUseCase
import kotlinx.coroutines.flow.StateFlow

class FileViewModel(
    getConnectionStatusUseCase: GetConnectionStatusUseCase,
    getNetworkConnectionUseCase: GetNetworkStatusUseCase,
) : ScreenModel {
    // network status
    var networkStatus: StateFlow<NetworkConnection> = getNetworkConnectionUseCase.invoke()
    var isConnected: StateFlow<Boolean> = getConnectionStatusUseCase.invoke()


}