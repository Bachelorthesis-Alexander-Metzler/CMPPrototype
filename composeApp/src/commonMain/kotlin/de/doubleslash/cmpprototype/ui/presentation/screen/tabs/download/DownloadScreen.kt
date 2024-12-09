package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.download


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.downloads_tab_title
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.ui.presentation.screen.file_preview.FilePreviewScreen
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.components.CustomTopAppBar
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveHorizontalDivider
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveIconButton
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.icons.AdaptiveIcons
import io.github.alexzhirkevich.cupertino.adaptive.icons.Delete
import org.jetbrains.compose.resources.stringResource

class DownloadScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<DownloadViewModel>()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            viewModel.refreshFiles()
        }

        Scaffold(
            topBar = CustomTopAppBar(text = stringResource(Res.string.downloads_tab_title))
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                items(viewModel.getAllFiles()) { file ->
                    DownloadedFileItemEntry(file, viewModel, onClick = {
                        navigator.push(FilePreviewScreen(file))
                    })
                }
            }
        }
    }

    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    private fun DownloadedFileItemEntry(
        file: FileModel,
        viewModel: DownloadViewModel,
        onClick: () -> Unit = {}
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

            AdaptiveIconButton(
                onClick = { viewModel.deleteFileLocally(file) },
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