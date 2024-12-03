package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.content_description_back
import cmpprototype.composeapp.generated.resources.file_preview_title
import cmpprototype.composeapp.generated.resources.image
import cmpprototype.composeapp.generated.resources.no_content
import cmpprototype.composeapp.generated.resources.no_file_preview_available
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.tools.getPdfPageCount
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.tools.renderPdfPageToBitmap
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
    )
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<FilePreviewViewModel>()

        Scaffold(
            topBar = {
                AdaptiveTopAppBar(
                    title = { Text(stringResource(Res.string.file_preview_title)) },
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
            if (file._id == null || file.extension !in listOf("png", "jpg", "txt", "pdf")) {
                Text(
                    modifier = Modifier.padding(paddingValues),
                    text = stringResource(Res.string.no_file_preview_available),
                )
            } else {
                // Load file with byte array
                val fileWithContent = viewModel.loadLocalFile(file._id!!)
                when (fileWithContent.extension) {
                    "png", "jpg" -> {
                        ImagePreview(fileWithContent, paddingValues)
                    }

                    "txt" -> {
                        txtPreview(fileWithContent, paddingValues)
                    }

                    "pdf" -> {
                        PdfPreview(fileWithContent, paddingValues)
                    }
                }
            }
        }
    }

    @Composable
    private fun PdfPreview(
        fileWithContent: FileModel,
        paddingValues: PaddingValues
    ) {
        val pdfData = fileWithContent.fileContent
        if (pdfData == null) {
            Text(
                modifier = Modifier.padding(paddingValues),
                text = stringResource(Res.string.no_content),
            )
            return
        }

        val pageCount = remember { mutableStateOf(0) }
        val lazyBitmaps = remember { mutableMapOf<Int, ImageBitmap>() }

        // load page count
        LaunchedEffect(pdfData) {
            pageCount.value = getPdfPageCount(pdfData)
        }

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            items(pageCount.value) { pageIndex ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                ) {
                    // just render, if the bitmap is already loaded
                    val bitmap = lazyBitmaps[pageIndex]
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap,
                            contentDescription = "PDF Page ${pageIndex + 1}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.White)
                        )
                    } else {
                        // render page
                        LaunchedEffect(pageIndex) {
                            val renderedBitmap = renderPdfPageToBitmap(pdfData, pageIndex)
                            lazyBitmaps[pageIndex] = renderedBitmap
                        }
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }


    @Composable
    private fun txtPreview(
        fileWithContent: FileModel,
        paddingValues: PaddingValues
    ) {
        val textContent =
            fileWithContent.fileContent?.decodeToString()
                ?: stringResource(Res.string.no_content)
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

    @Composable
    @OptIn(ExperimentalResourceApi::class)
    private fun ImagePreview(
        fileWithContent: FileModel,
        paddingValues: PaddingValues
    ) {
        val bitmapImage = fileWithContent.fileContent?.decodeToImageBitmap()
        bitmapImage?.let {
            // Display image
            Image(
                bitmap = it,
                contentDescription = stringResource(Res.string.image),
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            )
        }
    }
}