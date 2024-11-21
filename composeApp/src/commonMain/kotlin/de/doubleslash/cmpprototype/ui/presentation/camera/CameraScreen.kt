package de.doubleslash.cmpprototype.ui.presentation.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.content_description_back
import cmpprototype.composeapp.generated.resources.ic_flash_off
import cmpprototype.composeapp.generated.resources.ic_flash_on
import cmpprototype.composeapp.generated.resources.ic_flip_camera
import cmpprototype.composeapp.generated.resources.ic_take_picture
import coil3.PlatformContext
import coil3.compose.LocalPlatformContext
import com.kashif.cameraK.controller.CameraController
import com.kashif.cameraK.enums.CameraLens
import com.kashif.cameraK.enums.Directory
import com.kashif.cameraK.enums.FlashMode
import com.kashif.cameraK.enums.ImageFormat
import com.kashif.cameraK.result.ImageCaptureResult
import com.kashif.cameraK.ui.CameraPreview
import com.kashif.imageSaverPlugin.ImageSaverConfig
import com.kashif.imageSaverPlugin.ImageSaverPlugin
import com.kashif.imageSaverPlugin.createPlatformImageSaverPlugin
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveIconButton
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.icons.AdaptiveIcons
import io.github.alexzhirkevich.cupertino.adaptive.icons.KeyboardArrowLeft
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalResourceApi::class)
class CameraScreen : Screen {

    @Composable
    override fun Content() {
        val cameraController = remember { mutableStateOf<CameraController?>(null) }
        val imageSaverPlugin = rememberImageSaverPlugin(
            config = ImageSaverConfig(
                isAutoSave = true, // Set to true to enable automatic saving
                prefix = "prototype", // Prefix for image names when auto-saving
                directory = Directory.PICTURES, // Directory to save images
                customFolderName = "CMPPrototype" // Custom folder name within the directory, only works on android for now
            )
        )
        Box {
            CameraPreview(modifier = Modifier.fillMaxSize(), cameraConfiguration = {
                setCameraLens(CameraLens.BACK)
                setFlashMode(FlashMode.OFF)
                setImageFormat(ImageFormat.JPEG)
                setDirectory(Directory.PICTURES)
                addPlugin(imageSaverPlugin)
            }, onCameraControllerReady = {
                cameraController.value = it
                println("Camera Controller Ready ${cameraController.value}")
            })
            cameraController.value?.let { controller ->
                CameraContent(cameraController = controller)
            }
        }
    }

    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    fun CameraContent(cameraController: CameraController) {
        val viewModel = getScreenModel<CameraViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()
        var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
        var isFlashOn by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp).align(Alignment.TopStart),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Flash Mode Button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AdaptiveIconButton(
                        onClick = {
                            cameraController.toggleFlashMode()
                            isFlashOn = !isFlashOn
                        },
                        content = {
                            Icon(
                                imageVector = if (isFlashOn) vectorResource(Res.drawable.ic_flash_on)
                                else vectorResource(Res.drawable.ic_flash_off),
                                contentDescription = "Flash",
                                tint = Color.White,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    )
                }

                // Camera Lens Toggle Button
                AdaptiveIconButton(
                    onClick = {
                        scope.launch {
                            try {
                                cameraController.toggleCameraLens()
                                println("Camera lens toggled successfully")
                            } catch (e: Exception) {
                                println("Error toggling camera lens: ${e.message}")
                            }
                        }
                    },
                    content = {
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_flip_camera),
                            contentDescription = "Flip Camera",
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                )
            }

            // Back button
            AdaptiveIconButton(
                onClick = { navigator.pop() },
                modifier = Modifier.size(100.dp).clip(CircleShape).align(Alignment.BottomStart)
                    .padding(15.dp),
                content = {
                    Icon(
                        imageVector = AdaptiveIcons.Outlined.KeyboardArrowLeft,
                        contentDescription = stringResource(Res.string.content_description_back),
                        tint = Color.White,
                        modifier = Modifier.matchParentSize()
                    )
                }
            )

            // Capture Button at the Bottom Center
            AdaptiveIconButton(
                onClick = {
                    scope.launch {
                        when (val result = cameraController.takePicture()) {
                            is ImageCaptureResult.Success -> {

                                imageBitmap = result.byteArray.decodeToImageBitmap()

                                // save file to local mongo db
                                viewModel.saveImage(
                                    name = "img_${Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())}.jpg",
                                    extension = "jpg",
                                    path = "",
                                    fileContent = result.byteArray
                                )
                            }

                            is ImageCaptureResult.Error -> {
                                println("Image Capture Error: ${result.exception.message}")
                            }
                        }
                    }
                },
                modifier = Modifier.size(130.dp).clip(CircleShape).align(Alignment.BottomCenter)
                    .padding(15.dp),
                content = {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_take_picture),
                        contentDescription = "Capture Image",
                        tint = Color.White,
                        modifier = Modifier.matchParentSize()
                    )
                }
            )

            // Display the captured image
            imageBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = "Captured Image",
                    modifier = Modifier.fillMaxSize().padding(16.dp).rotate(90f)
                )

                LaunchedEffect(bitmap) {
                    delay(3000)
                    imageBitmap = null
                    navigator.pop()
                }
            }
        }

    }

    @Composable
    fun rememberImageSaverPlugin(
        config: ImageSaverConfig,
        context: PlatformContext = LocalPlatformContext.current
    ): ImageSaverPlugin {
        return remember(config) {
            createPlatformImageSaverPlugin(context, config)
        }
    }
}