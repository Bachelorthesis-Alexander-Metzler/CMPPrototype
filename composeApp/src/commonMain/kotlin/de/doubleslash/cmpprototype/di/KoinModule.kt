package de.doubleslash.cmpprototype.di

import de.doubleslash.cmpprototype.data.datasource.remote.rest.auth.AuthApi
import de.doubleslash.cmpprototype.data.datasource.remote.rest.auth.AuthApiImpl
import de.doubleslash.cmpprototype.data.repository.AuthRepositoryImpl
import de.doubleslash.cmpprototype.data.repository.NetworkStatusRepositoryImpl
import de.doubleslash.cmpprototype.domain.repository.AuthRepository
import de.doubleslash.cmpprototype.domain.repository.NetworkStatusRepository
import de.doubleslash.cmpprototype.domain.use_case.authenticateUser.AuthenticateUserUseCase
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetConnectionStatusUseCase
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetNetworkStatusUseCase
import de.doubleslash.cmpprototype.ui.presentation.screen.login.LoginViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val moduleApplication = module {
    // injection for AuthApi and AuthRepository
    single<AuthApi> { AuthApiImpl() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    // injection for network status repository
    single<NetworkStatusRepository> { NetworkStatusRepositoryImpl() }

    // inject AuthenticateUserUseCase which needs AuthRepository
    single { AuthenticateUserUseCase(get()) }

    // inject network use cases
    single { GetConnectionStatusUseCase(get()) }
    single { GetNetworkStatusUseCase(get()) }

    // inject LoginViewModel which needs AuthenticateUserUseCase
    factory { LoginViewModel(get(), get(), get()) }
}

fun initKoin() {
    startKoin {
        modules(moduleApplication)
    }
}