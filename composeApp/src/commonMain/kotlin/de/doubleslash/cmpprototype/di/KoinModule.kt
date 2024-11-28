package de.doubleslash.cmpprototype.di

import com.russhwolf.settings.Settings
import de.doubleslash.cmpprototype.data.datasource.local.localDB.MongoDB
import de.doubleslash.cmpprototype.data.datasource.local.localDB.MongoDBImpl
import de.doubleslash.cmpprototype.data.datasource.local.preferences.EncryptedSharedPreferencesImpl
import de.doubleslash.cmpprototype.data.datasource.local.preferences.Preferences
import de.doubleslash.cmpprototype.data.datasource.local.preferences.SharedPreferencesImpl
import de.doubleslash.cmpprototype.data.datasource.local.preferences.provideEncryptedSharedPreferences
import de.doubleslash.cmpprototype.data.datasource.local.preferences.provideSharedPreferences
import de.doubleslash.cmpprototype.data.datasource.remote.rest.auth.AuthApi
import de.doubleslash.cmpprototype.data.datasource.remote.rest.auth.AuthApiImpl
import de.doubleslash.cmpprototype.data.repository.auth.AuthRepositoryImpl
import de.doubleslash.cmpprototype.data.repository.deviceApi.NetworkStatusRepositoryImpl
import de.doubleslash.cmpprototype.data.repository.localStorage.FileStorageRepositoryImpl
import de.doubleslash.cmpprototype.domain.repository.auth.AuthRepository
import de.doubleslash.cmpprototype.domain.repository.deviceApi.NetworkStatusRepository
import de.doubleslash.cmpprototype.domain.repository.localStorage.FileStorageRepository
import de.doubleslash.cmpprototype.domain.use_case.authenticateUser.AuthenticateUserUseCase
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetConnectionStatusUseCase
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetNetworkStatusUseCase
import de.doubleslash.cmpprototype.domain.use_case.getPreviouslyAuthenticated.GetPreviouslyAuthenticatedUseCase
import de.doubleslash.cmpprototype.domain.use_case.getSessionData.GetCredentialsUseCase
import de.doubleslash.cmpprototype.domain.use_case.getSessionData.GetSessionDataUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.DeleteFileUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.LoadAllFilesUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.SaveFileUseCase
import de.doubleslash.cmpprototype.ui.presentation.screen.login.LoginViewModel
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.file_management.FileViewModel
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.settings.SettingsViewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

// Auth Module: Auth API and Repository
val authModule = module {
    single<AuthApi> { AuthApiImpl() }
    single<AuthRepository> { AuthRepositoryImpl(get(), EncryptedSharedPreferencesImpl(get(named("encrypted_settings")))) }
}

// Network Module: Network Status Repository
val networkModule = module {
    single<NetworkStatusRepository> { NetworkStatusRepositoryImpl() }
}

// Preferences Module: Encrypted and Shared Preferences
val preferencesModule = module {
    single<Settings>(named("encrypted_settings")) { provideEncryptedSharedPreferences() }
    single<Settings>(named("shared_settings")) { provideSharedPreferences() }
    single<Preferences>(named("shared")) { SharedPreferencesImpl(get(named("shared_settings"))) }
}

val filePersistenceModule = module {
    single<MongoDB> { MongoDBImpl() }
    single<FileStorageRepository> { FileStorageRepositoryImpl(get()) }
}

// Use Case Module: Business Logic
val useCaseModule = module {
    single { AuthenticateUserUseCase(get()) }
    single { GetConnectionStatusUseCase(get()) }
    single { GetNetworkStatusUseCase(get()) }
    single { GetSessionDataUseCase(get()) }
    single { GetPreviouslyAuthenticatedUseCase(get()) }
    single { GetCredentialsUseCase(get()) }

    // File Management Use Cases
    single { DeleteFileUseCase(get()) }
    single { LoadAllFilesUseCase(get()) }
    single { SaveFileUseCase(get()) }
}

// ViewModel Module: Login and Settings ViewModels
val viewModelModule = module {
    factory { LoginViewModel(get(), get(), get(), get(), get(), get()) }
    factory { SettingsViewModel() }
    factory { FileViewModel(get(), get(), get(), get(), get()) }
}

// Combine all modules
val moduleApplication = module {
    includes(authModule, networkModule, preferencesModule, filePersistenceModule, useCaseModule, viewModelModule)
}

fun initKoin() {
    startKoin {
        modules(moduleApplication)
    }
}