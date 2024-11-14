package de.doubleslash.cmpprototype.ui.presentation.screens.tabs.download


import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.downloads_tab_title
import cmpprototype.composeapp.generated.resources.ic_cloud_download
import de.doubleslash.cmpprototype.ui.presentation.screens.tabs.components.CustomTopAppBar
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class DownloadScreen : Tab {
    @Composable
    override fun Content() {
        Scaffold(
            topBar = CustomTopAppBar(text = stringResource(Res.string.downloads_tab_title))
        ) {

        }
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