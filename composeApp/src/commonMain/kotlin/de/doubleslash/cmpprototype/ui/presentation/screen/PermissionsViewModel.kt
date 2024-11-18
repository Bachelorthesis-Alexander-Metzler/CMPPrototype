package de.doubleslash.cmpprototype.ui.presentation.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import kotlinx.coroutines.launch

class PermissionsViewModel(
    private val permissionsController: PermissionsController
) : ScreenModel {

    var storageState by mutableStateOf(PermissionState.NotDetermined)
        private set  // only change value from inside this view model

    var galleryState by mutableStateOf(PermissionState.NotDetermined)
        private set  // only change value from inside this view model

    var cameraState by mutableStateOf(PermissionState.NotDetermined)
        private set  // only change value from inside this view model

    init {
        screenModelScope.launch {
            storageState = permissionsController.getPermissionState(Permission.STORAGE)
            galleryState = permissionsController.getPermissionState(Permission.GALLERY)
            cameraState = permissionsController.getPermissionState(Permission.CAMERA)
        }
    }

    fun provideOrRequestStoragePermission() {
        screenModelScope.launch {
            try {
                // check if permission was already granted
                // if not granted, request the permission
                permissionsController.providePermission(Permission.STORAGE)
                storageState = PermissionState.Granted
            } catch (e: DeniedAlwaysException) {
                // handle denied permission with "don't ask again" option
                storageState = PermissionState.DeniedAlways
            } catch (e: DeniedException) {
                // handle denied permission
                storageState = PermissionState.Denied
            } catch (e: RequestCanceledException) {
                // handle request cancellation (only for push notifications permission)
                e.printStackTrace()
            }
        }
    }

    fun provideOrRequestGalleryPermission() {
        screenModelScope.launch {
            try {
                // check if permission was already granted
                // if not granted, request the permission
                permissionsController.providePermission(Permission.GALLERY)
                galleryState = PermissionState.Granted
            } catch (e: DeniedAlwaysException) {
                // handle denied permission with "don't ask again" option
                galleryState = PermissionState.DeniedAlways
            } catch (e: DeniedException) {
                // handle denied permission
                galleryState = PermissionState.Denied
            } catch (e: RequestCanceledException) {
                // handle request cancellation (only for push notifications permission)
                e.printStackTrace()
            }
        }
    }

    fun provideOrRequestCameraPermission() {
        screenModelScope.launch {
            try {
                // check if permission was already granted
                // if not granted, request the permission
                permissionsController.providePermission(Permission.CAMERA)
                cameraState = PermissionState.Granted
            } catch (e: DeniedAlwaysException) {
                // handle denied permission with "don't ask again" option
                cameraState = PermissionState.DeniedAlways
            } catch (e: DeniedException) {
                // handle denied permission
                cameraState = PermissionState.Denied
            } catch (e: RequestCanceledException) {
                // handle request cancellation (only for push notifications permission)
                e.printStackTrace()
            }
        }
    }
}