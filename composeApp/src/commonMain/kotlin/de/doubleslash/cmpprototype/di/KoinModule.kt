package de.doubleslash.cmpprototype.di

import com.russhwolf.settings.Settings
import de.doubleslash.cmpprototype.data.datasource.local.preferences.EncryptedSharedPreferencesImpl
import de.doubleslash.cmpprototype.data.datasource.local.preferences.Preferences
import de.doubleslash.cmpprototype.data.datasource.local.preferences.SharedPreferencesImpl
import de.doubleslash.cmpprototype.data.datasource.local.preferences.provideEncryptedSharedPreferences
import de.doubleslash.cmpprototype.data.datasource.local.preferences.provideSharedPreferences
import de.doubleslash.cmpprototype.data.datasource.remote.rest.auth.AuthApi
import de.doubleslash.cmpprototype.data.datasource.remote.rest.auth.AuthApiImpl
import de.doubleslash.cmpprototype.data.repository.auth.AuthRepositoryImpl
import de.doubleslash.cmpprototype.data.repository.deviceApi.NetworkStatusRepositoryImpl
import de.doubleslash.cmpprototype.data.repository.storage.SecureRepositoryImpl
import de.doubleslash.cmpprototype.domain.repository.auth.AuthRepository
import de.doubleslash.cmpprototype.domain.repository.deviceApi.NetworkStatusRepository
import de.doubleslash.cmpprototype.domain.repository.storage.SecureRepository
import de.doubleslash.cmpprototype.domain.use_case.authenticateUser.AuthenticateUserUseCase
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetConnectionStatusUseCase
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetNetworkStatusUseCase
import de.doubleslash.cmpprototype.domain.use_case.getSessionData.GetSessionDataUseCase
import de.doubleslash.cmpprototype.ui.presentation.screen.login.LoginViewModel
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.settings.SettingsViewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

val moduleApplication = module {
    // injection for AuthApi and AuthRepository
    single<AuthApi> { AuthApiImpl() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    // injection for network status repository
    single<NetworkStatusRepository> { NetworkStatusRepositoryImpl() }

    // Injection for shared preferences Settings instance
    single<Settings>(named("shared_settings")) { provideSharedPreferences() }

    // Injection for encrypted shared preferences Settings instance
    single<Settings>(named("encrypted_settings")) { provideEncryptedSharedPreferences() }

    // Injection for shared preferences (non-encrypted)
    single<Preferences>(named("shared")) { SharedPreferencesImpl(get(named("shared_settings"))) }

    // Injection for encrypted shared preferences
    single<Preferences>(named("encrypted")) { EncryptedSharedPreferencesImpl(get(named("encrypted_settings"))) }

    // injection for secure repository
    single<SecureRepository> { SecureRepositoryImpl(get(named("encrypted"))) }

    // inject AuthenticateUserUseCase which needs AuthRepository
    single { AuthenticateUserUseCase(get()) }

    // inject network use cases
    single { GetConnectionStatusUseCase(get()) }
    single { GetNetworkStatusUseCase(get()) }

    // inject session data use case
    single { GetSessionDataUseCase(get()) }

    // inject LoginViewModel which needs AuthenticateUserUseCase
    factory { LoginViewModel(get(), get(), get(), get()) }
    factory { SettingsViewModel() }
}

fun initKoin() {
    startKoin {
        modules(moduleApplication)
    }
}