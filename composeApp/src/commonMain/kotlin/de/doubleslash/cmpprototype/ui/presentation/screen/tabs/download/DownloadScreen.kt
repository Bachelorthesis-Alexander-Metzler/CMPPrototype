package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.download


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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.downloads_tab_title
import cmpprototype.composeapp.generated.resources.ic_cloud_download
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.components.CustomTopAppBar
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveHorizontalDivider
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveIconButton
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.icons.AdaptiveIcons
import io.github.alexzhirkevich.cupertino.adaptive.icons.Delete
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class DownloadScreen : Tab {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<DownloadViewModel>()
//        viewModel.loadFiles()

        Scaffold(
            topBar = CustomTopAppBar(text = stringResource(Res.string.downloads_tab_title))
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                items(viewModel.getAllFiles()) { file ->
                    DownloadedFileItemEntry(file, viewModel)
                }
            }
        }
    }

    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    private fun DownloadedFileItemEntry(
        file: FileModel,
        viewModel: DownloadViewModel
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .heightIn(min = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            if (file.path != null) {
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

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(vectorResource(Res.drawable.ic_cloud_download))
            val title = stringResource(Res.string.downloads_tab_title)
            val index: UShort = 1u

            return TabOptions(
                icon = icon,
                title = title,
                index = index
            )
        }
}