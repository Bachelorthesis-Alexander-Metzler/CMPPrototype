package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.content_description_back
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveIconButton
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveTopAppBar
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.icons.AdaptiveIcons
import io.github.alexzhirkevich.cupertino.adaptive.icons.KeyboardArrowLeft
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.stringResource

class FilePreviewScreen(
    private val file: FileModel
) : Screen {
    @OptIn(
        ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class,
        ExperimentalResourceApi::class
    )
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<FilePreviewViewModel>()

        Scaffold(
            topBar = {
                AdaptiveTopAppBar(
                    title = { Text("File Preview") },
                    navigationIcon = {
                        AdaptiveIconButton(
                            onClick = { navigator.pop() },
                            content = {
                                Icon(
                                    imageVector = AdaptiveIcons.Outlined.KeyboardArrowLeft,
                                    contentDescription = stringResource(Res.string.content_description_back)
                                )
                            }
                        )
                    },
                    adaptation = {
                        material {
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->


            // File preview for images or text files
            if (file._id == null || file.extension !in listOf("png", "jpg", "txt")) {
                Text(
                    modifier = Modifier.padding(paddingValues),
                    text = "No File Preview available",
                )
            } else {
                // Load file with byte array
                val fileWithContent = viewModel.loadLocalFile(file._id!!)
                when (fileWithContent.extension) {
                    "png", "jpg" -> {
                        val bitmapImage = fileWithContent.fileContent?.decodeToImageBitmap()
                        bitmapImage?.let {
                            // Display image
                            Image(
                                bitmap = it,
                                contentDescription = "Image",
                                modifier = Modifier
                                    .padding(paddingValues)
                                    .fillMaxSize()
                            )
                        }
                    }

                    "txt" -> {
                        val textContent =
                            fileWithContent.fileContent?.decodeToString() ?: "No Content"
                        // Display text content
                        LazyColumn(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize()
                        ) {
                            item {
                                Text(
                                    text = textContent,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}