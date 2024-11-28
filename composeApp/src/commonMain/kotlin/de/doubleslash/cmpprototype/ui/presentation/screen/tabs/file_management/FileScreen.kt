package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.file_management

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.koin.getScreenModel
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
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
import cmpprototype.composeapp.generated.resources.ic_upload_file
import cmpprototype.composeapp.generated.resources.open_camera
import cmpprototype.composeapp.generated.resources.open_settings
import cmpprototype.composeapp.generated.resources.permission_denied
import cmpprototype.composeapp.generated.resources.storage_permission_denied_always
import de.doubleslash.cmpprototype.ui.presentation.camera.CameraScreen
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.ui.presentation.screen.PermissionsViewModel
import de.doubleslash.cmpprototype.ui.presentation.components.FabItem
import de.doubleslash.cmpprototype.ui.presentation.components.MultiFloatingActionButton
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.components.CustomTopAppBar
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveAlertDialog
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveHorizontalDivider
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveIconButton
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.icons.AdaptiveIcons
import io.github.alexzhirkevich.cupertino.adaptive.icons.Add
import io.github.alexzhirkevich.cupertino.adaptive.icons.Delete
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
        viewModel.loadFiles()
        val isConnected by viewModel.isConnected.collectAsState()

        // create a permissions controller
        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) { factory.createPermissionsController() }
        BindEffect(controller)
        val permissionsViewModel = PermissionsViewModel(controller)

        // dialog
        var showDialog by remember { mutableStateOf(false) }
        var dialogMessage by remember { mutableStateOf("") }

        // String-resources
        val storagePermissionDeniedMessage =
            stringResource(Res.string.storage_permission_denied_always)
        val galleryPermissionDeniedMessage =
            stringResource(Res.string.gallery_permission_denied_always)
        val cameraPermissionDeniedMessage =
            stringResource(Res.string.camera_permission_denied_always)

        // create a launcher for picking files
        val documentLauncher = createLauncher(viewModel, PickerType.File())
        val imgVidLauncher = createLauncher(viewModel, PickerType.ImageAndVideo)

        if (showDialog) {
            AdaptiveAlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = { Text(stringResource(Res.string.permission_denied)) },
                message = { Text(dialogMessage) }
            ) {
                cancel(onClick = { showDialog = false }) {
                    Text(stringResource(Res.string.cancel))
                }
                default(onClick = {
                    controller.openAppSettings()
                }) { Text(stringResource(Res.string.open_settings)) }
            }
        }

        Scaffold(
            topBar = CustomTopAppBar(text = stringResource(Res.string.file_tab_title)),
            floatingActionButton = {
                if (isConnected) {
                    MultiFloatingActionButton(
                        fabIcon = rememberVectorPainter(AdaptiveIcons.Outlined.Add),
                        showLabels = false,
                        items = arrayListOf(
                            // FabItem for choosing files
                            FabItem(
                                icon = painterResource(Res.drawable.ic_upload_file),
                                label = stringResource(Res.string.choose_from_files),
                                onFabItemClicked = {
                                    when (permissionsViewModel.storageState) {
                                        PermissionState.Granted -> {
                                            // launch file picker
                                            documentLauncher.launch()
                                        }

                                        PermissionState.DeniedAlways -> {
                                            dialogMessage = storagePermissionDeniedMessage
                                            showDialog = true
                                        }

                                        else -> {
                                            permissionsViewModel.provideOrRequestStoragePermission()
                                        }
                                    }
                                }),
                            // FabItem for choosing from gallery
                            FabItem(
                                icon = painterResource(Res.drawable.ic_add_from_gallery),
                                label = stringResource(Res.string.choose_from_gallery),
                                onFabItemClicked = {
                                    when (permissionsViewModel.galleryState) {
                                        PermissionState.Granted -> {
                                            // launch image/video picker
                                            imgVidLauncher.launch()
                                        }

                                        PermissionState.DeniedAlways -> {
                                            dialogMessage = galleryPermissionDeniedMessage
                                            showDialog = true
                                        }

                                        else -> {
                                            permissionsViewModel.provideOrRequestGalleryPermission()
                                        }
                                    }
                                }),
                            // FabItem for opening camera
                            FabItem(
                                icon = painterResource(Res.drawable.ic_camera),
                                label = stringResource(Res.string.open_camera),
                                onFabItemClicked = {
                                    when (permissionsViewModel.cameraState) {
                                        PermissionState.Granted -> {
                                            navigator.push(CameraScreen())
                                        }

                                        PermissionState.DeniedAlways -> {
                                            dialogMessage = cameraPermissionDeniedMessage
                                            showDialog = true
                                        }

                                        else -> {
                                            permissionsViewModel.provideOrRequestCameraPermission()
                                        }
                                    }
                                }
                            )
                        )
                    )
                }
            }
        ) { paddingValues ->
            if (isConnected) {
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                ) {
                    items(viewModel.getAllFiles()) { file ->
                        FileItemEntry(file, viewModel)
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Unable to load files. No internet connection.",
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
            type = pickerType) { files ->
            // extract file names and store in selectedFiles list
            files?.forEach { file ->
                CoroutineScope(Dispatchers.IO).launch {
                    val fileName = file.name
                    val extension = file.extension
                    val filePath = file.path ?: ""

                    // read content of file
                    val fileContent = file.readBytes()

                    viewModel.saveFile(fileName, extension, filePath, fileContent)
                }
            }
        }

    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    private fun FileItemEntry(
        file: FileModel,
        viewModel: FileViewModel
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .heightIn(min = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            if (file.path.isNotBlank()) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Base Name
                    Text(
                        text = file.baseName,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = file.path,
                        style = MaterialTheme
                            .typography
                            .bodySmall
                            .copy(
                                color = MaterialTheme
                                    .colorScheme
                                    .secondary
                            ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = file.baseName,
                    style = MaterialTheme.typography.titleMedium,
                )
            }


            AdaptiveIconButton(
                onClick = { viewModel.deleteFile(file) },
                content = {
                    Icon(
                        imageVector = AdaptiveIcons.Outlined.Delete,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            )
        }

        AdaptiveHorizontalDivider()
    }
}