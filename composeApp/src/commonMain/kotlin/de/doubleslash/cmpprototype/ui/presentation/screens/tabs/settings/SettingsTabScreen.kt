package de.doubleslash.cmpprototype.ui.presentation.screens.tabs.settings


import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.settings_tab_title
import io.github.alexzhirkevich.cupertino.adaptive.icons.AdaptiveIcons
import io.github.alexzhirkevich.cupertino.adaptive.icons.Settings
import org.jetbrains.compose.resources.stringResource

/** Settings tab screen which holds the settings screen and pass it the navigator for
 * linear navigation inside of the tab */
class SettingsTabScreen : Tab {

    @Composable
    override fun Content() {
        Navigator(screen = SettingsScreen()) { navigator ->
            SlideTransition(navigator = navigator)
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