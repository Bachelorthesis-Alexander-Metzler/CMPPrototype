package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.file_management

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.camera_permission_denied_always
import cmpprototype.composeapp.generated.resources.choose_from_files
import cmpprototype.composeapp.generated.resources.choose_from_gallery
import cmpprototype.composeapp.generated.resources.file_tab_title
import cmpprototype.composeapp.generated.resources.gallery_permission_denied_always
import cmpprototype.composeapp.generated.resources.ic_add_from_gallery
import cmpprototype.composeapp.generated.resources.ic_camera
import cmpprototype.composeapp.generated.resources.ic_upload_file
import cmpprototype.composeapp.generated.resources.open_camera
import cmpprototype.composeapp.generated.resources.storage_permission_denied_always
import de.doubleslash.cmpprototype.ui.presentation.screen.PermissionsViewModel
import de.doubleslash.cmpprototype.ui.presentation.components.FabItem
import de.doubleslash.cmpprototype.ui.presentation.components.MultiFloatingActionButton
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.components.CustomTopAppBar
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveAlertDialog
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.icons.AdaptiveIcons
import io.github.alexzhirkevich.cupertino.adaptive.icons.Add
import io.github.alexzhirkevich.cupertino.adaptive.icons.Home
import io.github.alexzhirkevich.cupertino.default
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class FileScreen : Tab {
    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    override fun Content() {
        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) { factory.createPermissionsController() }
        BindEffect(controller)
        val permissionsViewModel = PermissionsViewModel(controller)

        var showDialog by remember { mutableStateOf(false) }
        var dialogMessage by remember { mutableStateOf("") }

        if (showDialog) {
            AdaptiveAlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = { Text("Permission Denied") },
                message = { Text(dialogMessage) },
                buttons = {
                    default(onClick = { showDialog = false }) { Text("OK") }
                }
            )
        }

        Scaffold(
            topBar = CustomTopAppBar(text = stringResource(Res.string.file_tab_title)),
            floatingActionButton = {
                MultiFloatingActionButton(fabIcon = rememberVectorPainter(AdaptiveIcons.Outlined.Add),
                    showLabels = false,
                    items = arrayListOf(
                        FabItem(
                            icon =  painterResource(Res.drawable.ic_upload_file),
                            label = stringResource(Res.string.choose_from_files),
                            onFabItemClicked = {
                                // get permission
                                permissionsViewModel.provideOrRequestStoragePermission()
                            }),
                        FabItem(
                            icon =  painterResource(Res.drawable.ic_add_from_gallery),
                            label = stringResource(Res.string.choose_from_gallery),
                            onFabItemClicked = {
                                permissionsViewModel.provideOrRequestGalleryPermission()
                            }),
                        FabItem(
                            icon =  painterResource(Res.drawable.ic_camera),
                            label = stringResource(Res.string.open_camera),
                            onFabItemClicked = {
                                permissionsViewModel.provideOrRequestCameraPermission()
                            })
                        ))
            }
        ) { paddingValues ->

            if (permissionsViewModel.storageState == PermissionState.DeniedAlways) {
                dialogMessage = stringResource(Res.string.storage_permission_denied_always)
                showDialog = true
            }

            if (permissionsViewModel.galleryState == PermissionState.DeniedAlways) {
                dialogMessage = stringResource(Res.string.gallery_permission_denied_always)
                showDialog = true
            }

            if (permissionsViewModel.cameraState == PermissionState.DeniedAlways) {
                dialogMessage = stringResource(Res.string.camera_permission_denied_always)
                showDialog = true
            }
        }
    }


    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(image = (AdaptiveIcons.Outlined.Home))
            val title = stringResource(Res.string.file_tab_title)
            val index: UShort = 0u

            return TabOptions(
                icon = icon,
                title = title,
                index = index
            )
        }

}