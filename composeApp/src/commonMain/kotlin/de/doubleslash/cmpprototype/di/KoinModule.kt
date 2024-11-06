package de.doubleslash.cmpprototype.di

import de.doubleslash.cmpprototype.data.datasource.remote.rest.AuthApi
import de.doubleslash.cmpprototype.data.datasource.remote.rest.AuthApiImpl
import de.doubleslash.cmpprototype.data.repository.AuthRepositoryImpl
import de.doubleslash.cmpprototype.domain.repository.AuthRepository
import de.doubleslash.cmpprototype.domain.use_case.authenticateUser.AuthenticateUserUseCase
import de.doubleslash.cmpprototype.ui.presentation.screen.login.LoginViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val moduleApplication = module {
    // injection for AuthApi and AuthRepository
    single<AuthApi> { AuthApiImpl() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    // inject AuthenticateUserUseCase which needs AuthRepository
    single { AuthenticateUserUseCase(get()) }

    // inject LoginViewModel which needs AuthenticateUserUseCase
    factory { LoginViewModel(get()) }
}

fun initKoin() {
    startKoin {
        modules(moduleApplication)
    }
}