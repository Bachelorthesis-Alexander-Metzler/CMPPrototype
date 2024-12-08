package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.file_management

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.camera_permission_denied_always
import cmpprototype.composeapp.generated.resources.cancel
import cmpprototype.composeapp.generated.resources.choose_from_files
import cmpprototype.composeapp.generated.resources.choose_from_gallery
import cmpprototype.composeapp.generated.resources.file_tab_title
import cmpprototype.composeapp.generated.resources.gallery_permission_denied_always
import cmpprototype.composeapp.generated.resources.ic_add_from_gallery
import cmpprototype.composeapp.generated.resources.ic_camera
import cmpprototype.composeapp.generated.resources.ic_cloud_file_download
import cmpprototype.composeapp.generated.resources.ic_cloud_upload
import cmpprototype.composeapp.generated.resources.ic_file
import cmpprototype.composeapp.generated.resources.ic_folder
import cmpprototype.composeapp.generated.resources.ic_upload_file
import cmpprototype.composeapp.generated.resources.local_object
import cmpprototype.composeapp.generated.resources.no_files_loaded
import cmpprototype.composeapp.generated.resources.open_camera
import cmpprototype.composeapp.generated.resources.open_settings
import cmpprototype.composeapp.generated.resources.permission_denied
import cmpprototype.composeapp.generated.resources.remote_object
import cmpprototype.composeapp.generated.resources.storage_permission_denied_always
import cmpprototype.composeapp.generated.resources.unable_to_load_files
import cmpprototype.composeapp.generated.resources.warn_dialog_cancel
import cmpprototype.composeapp.generated.resources.warn_dialog_confirm
import cmpprototype.composeapp.generated.resources.warn_dialog_message
import cmpprototype.composeapp.generated.resources.warn_dialog_title
import com.plusmobileapps.konnectivity.NetworkConnection
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FolderModel
import de.doubleslash.cmpprototype.ui.presentation.components.FabItem
import de.doubleslash.cmpprototype.ui.presentation.components.MultiFloatingActionButton
import de.doubleslash.cmpprototype.ui.presentation.screen.PermissionsViewModel
import de.doubleslash.cmpprototype.ui.presentation.screen.camera.CameraScreen
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.common.FilePreviewScreen
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.components.CustomTopAppBar
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveAlertDialog
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveCircularProgressIndicator
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveHorizontalDivider
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveIconButton
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.icons.AdaptiveIcons
import io.github.alexzhirkevich.cupertino.adaptive.icons.Add
import io.github.alexzhirkevich.cupertino.cancel
import io.github.alexzhirkevich.cupertino.default
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import io.github.vinceglb.filekit.core.extension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class FileScreen : Screen {
    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<FileViewModel>()

        // Refresh files and folders when screen is opened
        LaunchedEffect(Unit) {
            viewModel.refreshFilesAndFolders()
        }

        val isConnected by viewModel.isConnected.collectAsState()
        val networkStatus by viewModel.networkStatus.collectAsState()
        val fileState = viewModel.fetchRemoteFilesState
        val folderState = viewModel.fetchRemoteFoldersState
        var isLoading by remember { mutableStateOf(false) }

        // Dialogs
        var showPermissionDialog by remember { mutableStateOf(false) }
        var permissionDialogMessage by remember { mutableStateOf("") }
        val showWarnDialog = remember { mutableStateOf(false) }

        // Create permissions controller
        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) { factory.createPermissionsController() }
        BindEffect(controller)
        val permissionsViewModel = PermissionsViewModel(controller)

        // Permission denied messages
        val storagePermissionDeniedMessage =
            stringResource(Res.string.storage_permission_denied_always)
        val galleryPermissionDeniedMessage =
            stringResource(Res.string.gallery_permission_denied_always)
        val cameraPermissionDeniedMessage =
            stringResource(Res.string.camera_permission_denied_always)

        // File pickers
        val documentLauncher = createLauncher(viewModel, PickerType.File())
        val imgVidLauncher = createLauncher(viewModel, PickerType.ImageAndVideo)

        // inform user about denied permissions
        PermissionAlertDialog(
            showDialog = showPermissionDialog,
            title = stringResource(Res.string.permission_denied),
            message = permissionDialogMessage,
            onCancelClick = { showPermissionDialog = false },
            onSettingsClick = { controller.openAppSettings() },
            onDismissRequest = { showPermissionDialog = false }
        )

        // warn user about mobile data usage
        WarnAlertDialog(
            showDialog = showWarnDialog.value,
            onConfirmClick = { showWarnDialog.value = false },
            onCancelClick = { showWarnDialog.value = false },
            onDismissRequest = { showWarnDialog.value = false }
        )

        Scaffold(
            topBar = CustomTopAppBar(text = stringResource(Res.string.file_tab_title)),
            floatingActionButton = {
                if (isConnected) {
                    MultiFloatingActionButton(
                        fabIcon = rememberVectorPainter(AdaptiveIcons.Outlined.Add),
                        showLabels = false,
                        items = arrayListOf(
                            // Choose files
                            FabItem(
                                icon = painterResource(Res.drawable.ic_upload_file),
                                label = stringResource(Res.string.choose_from_files),
                                onFabItemClicked = {
                                    when (permissionsViewModel.storageState) {
                                        PermissionState.Granted -> documentLauncher.launch()
                                        PermissionState.DeniedAlways -> {
                                            permissionDialogMessage = storagePermissionDeniedMessage
                                            showPermissionDialog = true
                                        }

                                        else -> permissionsViewModel.provideOrRequestStoragePermission()
                                    }
                                }
                            ),
                            // Choose from gallery
                            FabItem(
                                icon = painterResource(Res.drawable.ic_add_from_gallery),
                                label = stringResource(Res.string.choose_from_gallery),
                                onFabItemClicked = {
                                    when (permissionsViewModel.galleryState) {
                                        PermissionState.Granted -> imgVidLauncher.launch()
                                        PermissionState.DeniedAlways -> {
                                            permissionDialogMessage = galleryPermissionDeniedMessage
                                            showPermissionDialog = true
                                        }

                                        else -> permissionsViewModel.provideOrRequestGalleryPermission()
                                    }
                                }
                            ),
                            // Open camera
                            FabItem(
                                icon = painterResource(Res.drawable.ic_camera),
                                label = stringResource(Res.string.open_camera),
                                onFabItemClicked = {
                                    when (permissionsViewModel.cameraState) {
                                        PermissionState.Granted -> navigator.push(CameraScreen())
                                        PermissionState.DeniedAlways -> {
                                            permissionDialogMessage = cameraPermissionDeniedMessage
                                            showPermissionDialog = true
                                        }

                                        else -> permissionsViewModel.provideOrRequestCameraPermission()
                                    }
                                }
                            )
                        )
                    )
                }
            }
        ) { paddingValues ->
            if (isConnected) {
                when {
                    fileState is RequestCondition.LoadingCondition ||
                            folderState is RequestCondition.LoadingCondition -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentAlignment = Alignment.Center
                        ) {
                            AdaptiveCircularProgressIndicator()
                        }
                    }

                    fileState is RequestCondition.SuccessCondition &&
                            folderState is RequestCondition.SuccessCondition -> {
                        val allFolders = viewModel.getAllFolders().sortedBy { it.name }
                        val allFiles = viewModel.getAllFiles().sortedBy { it.baseName }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            // Display folders
                            items(allFolders) { folder ->
                                FolderItemEntry(folder)
                            }

                            // Display files
                            items(allFiles) { file ->
                                FileItemEntry(
                                    file, onClick = {
                                        // only allow preview for downloaded files
                                        if (!file.isRemoteFile) {
                                            if (!file.isRemoteFile) {
                                                isLoading = true
                                                navigator.push(FilePreviewScreen(file))
                                                isLoading = false
                                            }
                                        }
                                    },
                                    networkStatus = networkStatus,
                                    showWarnDialog = showWarnDialog
                                )
                            }
                        }
                    }

                    fileState is RequestCondition.ErrorCondition ||
                            folderState is RequestCondition.ErrorCondition -> {
                        val errorMessage = (fileState as? RequestCondition.ErrorCondition)?.errorMsg
                            ?: (folderState as? RequestCondition.ErrorCondition)?.errorMsg
                            ?: stringResource(Res.string.unable_to_load_files)

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    else -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.no_files_loaded),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.unable_to_load_files),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun createLauncher(viewModel: FileViewModel, pickerType: PickerType) =
        rememberFilePickerLauncher(
            mode = PickerMode.Multiple(),
            type = pickerType
        ) { files ->
            files?.forEach { file ->
                CoroutineScope(Dispatchers.IO).launch {
                    val fileName = file.name
                    val extension = file.extension
                    val filePath = file.path ?: ""
                    val fileContent = file.readBytes()

                    viewModel.saveFileLocally(fileName, extension, filePath, fileContent)
                }
            }
        }

    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    private fun FolderItemEntry(folder: FolderModel, onClick: () -> Unit = {}) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .heightIn(min = 50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp),
                painter = painterResource(Res.drawable.ic_folder),
                contentDescription = stringResource(Res.string.remote_object),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = folder.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
        }
        AdaptiveHorizontalDivider()
    }

    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    private fun FileItemEntry(
        file: FileModel,
        onClick: () -> Unit = {},
        networkStatus: NetworkConnection,
        showWarnDialog: MutableState<Boolean>
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .heightIn(min = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp),
                painter = painterResource(Res.drawable.ic_file),
                contentDescription = stringResource(Res.string.remote_object),
                tint = MaterialTheme.colorScheme.primary
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = file.baseName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )

                if (file.path != null) {
                    Text(
                        text = file.path,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }

            // Conditional Icon Buttons (Download/Upload)
            if (file.isRemoteFile) {
                AdaptiveIconButton(
                    onClick = {
                        // Action for downloading remote file
                        if (networkStatus == NetworkConnection.CELLULAR) {
                            // Warn user about mobile data usage
                            showWarnDialog.value = true
                        } else {
                            // Download file
                        }
                    },
                    content = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_cloud_file_download),
                            contentDescription = stringResource(Res.string.remote_object),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            } else {
                AdaptiveIconButton(
                    onClick = {
                        // Action for uploading remote file
                        if (networkStatus == NetworkConnection.CELLULAR) {
                            // Warn user about mobile data usage
                            showWarnDialog.value = true
                        } else {
                            // Upload file
                        }
                    },
                    content = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_cloud_upload),
                            contentDescription = stringResource(Res.string.local_object),
                            tint = Color(0xFFFFA500) // Orange
                        )
                    }
                )
            }
        }
        AdaptiveHorizontalDivider()
    }

    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    fun PermissionAlertDialog(
        showDialog: Boolean,
        title: String,
        message: String,
        onCancelClick: () -> Unit,
        onSettingsClick: () -> Unit,
        onDismissRequest: () -> Unit
    ) {
        if (showDialog) {
            AdaptiveAlertDialog(
                onDismissRequest = onDismissRequest,
                title = { Text(title) },
                message = { Text(message) }
            ) {
                cancel(onClick = onCancelClick) {
                    Text(stringResource(Res.string.cancel))
                }
                default(onClick = onSettingsClick) {
                    Text(stringResource(Res.string.open_settings))
                }
            }
        }
    }

    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    fun WarnAlertDialog(
        showDialog: Boolean,
        onConfirmClick: () -> Unit,
        onCancelClick: () -> Unit,
        onDismissRequest: () -> Unit
    ) {
        if (showDialog) {
            val title = stringResource(Res.string.warn_dialog_title)
            val message = stringResource(Res.string.warn_dialog_message)
            val cancelText = stringResource(Res.string.warn_dialog_cancel)
            val confirmText = stringResource(Res.string.warn_dialog_confirm)

            AdaptiveAlertDialog(
                onDismissRequest = onDismissRequest,
                title = { Text(title) },
                message = { Text(message) }
            ) {
                cancel(onClick = onCancelClick) {
                    Text(cancelText)
                }
                default(onClick = onConfirmClick) {
                    Text(confirmText)
                }
            }
        }
    }

}
