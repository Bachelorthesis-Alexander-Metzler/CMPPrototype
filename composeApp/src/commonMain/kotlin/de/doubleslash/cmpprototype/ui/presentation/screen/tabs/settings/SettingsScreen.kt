package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.settings


import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.settings_tab_title
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.components.CustomTopAppBar
import io.github.alexzhirkevich.cupertino.adaptive.icons.AdaptiveIcons
import io.github.alexzhirkevich.cupertino.adaptive.icons.Settings
import org.jetbrains.compose.resources.stringResource

class SettingsScreen : Tab {

    @Composable
    override fun Content() {
        Scaffold(
            topBar = CustomTopAppBar()
        ) {

        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(image = (AdaptiveIcons.Outlined.Settings))
            val title = stringResource(Res.string.settings_tab_title)
            val index: UShort = 2u

            return TabOptions(
                icon = icon,
                title = title,
                index = index
            )
        }
}