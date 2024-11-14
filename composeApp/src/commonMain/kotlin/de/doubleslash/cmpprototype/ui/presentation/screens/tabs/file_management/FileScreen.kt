package de.doubleslash.cmpprototype.ui.presentation.screens.tabs.file_management

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.file_tab_title
import de.doubleslash.cmpprototype.ui.presentation.screens.tabs.components.CustomTopAppBar
import io.github.alexzhirkevich.cupertino.adaptive.icons.AdaptiveIcons
import io.github.alexzhirkevich.cupertino.adaptive.icons.Home
import org.jetbrains.compose.resources.stringResource

class FileScreen : Tab {
    @Composable
    override fun Content() {
        Scaffold(
            topBar = CustomTopAppBar(text = stringResource(Res.string.file_tab_title))
        ) {

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