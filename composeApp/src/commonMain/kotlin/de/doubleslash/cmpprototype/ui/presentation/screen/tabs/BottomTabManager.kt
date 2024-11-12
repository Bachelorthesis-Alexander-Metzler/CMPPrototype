package de.doubleslash.cmpprototype.ui.presentation.screen.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import de.doubleslash.cmpprototype.common.getPlatform
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.download.DownloadScreen
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.file_management.FileScreen
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.settings.SettingsScreen
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveNavigationBar
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveNavigationBarItem
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.theme.CupertinoTheme

/** Main Tab Screen only for Tab navigation
 * manages bottom tabs */
class BottomTabManager : Screen {
    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    override fun Content() {
        TabNavigator(FileScreen()) {
            Scaffold(
                bottomBar = {
                    AdaptiveNavigationBar {
                        TabItem(FileScreen())
                        TabItem(DownloadScreen())
                        TabItem(SettingsScreen())
                    }
                }
            ) {
                CurrentTab()
            }
        }
    }

    @Composable
    private fun RowScope.TabItem(tab: Tab) {
        val tabNavigator = LocalTabNavigator.current

        NavigationBarItem(
            selected = tabNavigator.current == tab,
            onClick = {
                tabNavigator.current = tab
            },
            icon = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    tab.options.icon?.let { painter ->
                        Icon(
                            painter = painter,
                            contentDescription = tab.options.title,
                            modifier = Modifier.size(24.dp))
                    }

                    if (getPlatform() == "android") Text(text = tab.options.title)
                }
            },
        )
    }
}