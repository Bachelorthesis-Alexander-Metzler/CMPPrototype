package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.download

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.downloads_tab_title
import cmpprototype.composeapp.generated.resources.ic_cloud_download
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

/** Download tab screen which holds the download screen and pass it the navigator for
 * linear navigation inside of the tab */
class DownloadTabScreen : Tab {

    @Composable
    override fun Content() {
        Navigator(screen = DownloadScreen()) { navigator ->
            SlideTransition(navigator = navigator)
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