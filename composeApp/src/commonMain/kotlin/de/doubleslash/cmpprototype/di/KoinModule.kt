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
import de.doubleslash.cmpprototype.data.datasource.remote.rest.cmis.CMISService
import de.doubleslash.cmpprototype.data.datasource.remote.rest.cmis.CMISServiceImpl
import de.doubleslash.cmpprototype.data.repository.auth.AuthRepositoryImpl
import de.doubleslash.cmpprototype.data.repository.cmis.CMISRepositoryImpl
import de.doubleslash.cmpprototype.data.repository.deviceApi.NetworkStatusRepositoryImpl
import de.doubleslash.cmpprototype.data.repository.localStorage.FileStorageRepositoryImpl
import de.doubleslash.cmpprototype.domain.repository.auth.AuthRepository
import de.doubleslash.cmpprototype.domain.repository.cmis.CMISRepository
import de.doubleslash.cmpprototype.domain.repository.deviceApi.NetworkStatusRepository
import de.doubleslash.cmpprototype.domain.repository.localStorage.FileStorageRepository
import de.doubleslash.cmpprototype.domain.use_case.authenticateUser.AuthenticateUserUseCase
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetConnectionStatusUseCase
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetNetworkStatusUseCase
import de.doubleslash.cmpprototype.domain.use_case.cmis.LoadAllRemoteFilesUseCase
import de.doubleslash.cmpprototype.domain.use_case.cmis.LoadAllRemoteFoldersUseCase
import de.doubleslash.cmpprototype.domain.use_case.getPreviouslyAuthenticated.GetPreviouslyAuthenticatedUseCase
import de.doubleslash.cmpprototype.domain.use_case.getSessionData.GetCredentialsUseCase
import de.doubleslash.cmpprototype.domain.use_case.getSessionData.GetSessionDataUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.DeleteLocalFileUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.LoadAllLocalFilesUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.LoadLocalFileUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.SaveLocalFileUseCase
import de.doubleslash.cmpprototype.ui.presentation.screen.camera.CameraViewModel
import de.doubleslash.cmpprototype.ui.presentation.screen.file_preview.FilePreviewViewModel
import de.doubleslash.cmpprototype.ui.presentation.screen.login.LoginViewModel
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.download.DownloadViewModel
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.file_management.FileViewModel
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.settings.SettingsViewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

// Auth Module: Auth API and Repository
val authModule = module {
    single<AuthApi> { AuthApiImpl() }
    single<AuthRepository> {
        AuthRepositoryImpl(
            get(),
            EncryptedSharedPreferencesImpl(get(named("encrypted_settings")))
        )
    }
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
    // Local Database
    single<MongoDB> { MongoDBImpl() }
    single<FileStorageRepository> { FileStorageRepositoryImpl(get()) }

    // cmis file management
    single<CMISService> { CMISServiceImpl() }
    single<CMISRepository> { CMISRepositoryImpl(get()) }
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
    single { DeleteLocalFileUseCase(get()) }
    single { LoadAllLocalFilesUseCase(get()) }
    single { SaveLocalFileUseCase(get()) }
    single { LoadAllRemoteFilesUseCase(get()) }
    single { LoadAllRemoteFoldersUseCase(get()) }
    single { LoadLocalFileUseCase(get()) }
}

// ViewModel Module: Login and Settings ViewModels
val viewModelModule = module {
    factory { LoginViewModel(get(), get(), get(), get(), get(), get()) }
    factory { SettingsViewModel() }
    factory { FileViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    factory { CameraViewModel(get()) }
    factory { DownloadViewModel(get(), get()) }
    factory { FilePreviewViewModel(get()) }
}

// Combine all modules
val moduleApplication = module {
    includes(
        authModule,
        networkModule,
        preferencesModule,
        filePersistenceModule,
        useCaseModule,
        viewModelModule
    )
}

private var isKoinStarted = false

fun initKoin() {
    if (!isKoinStarted) { // initialize koin only, if it has not been started before
        startKoin {
            modules(moduleApplication)
        }
        isKoinStarted = true
    }
}
