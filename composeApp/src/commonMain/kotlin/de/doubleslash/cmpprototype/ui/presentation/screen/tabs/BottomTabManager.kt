package de.doubleslash.cmpprototype.ui.presentation.screen.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.download.DownloadScreen
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.file_management.FileScreen
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.file_management.FileTabScreen
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.settings.SettingsTabScreen
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveNavigationBar
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveNavigationBarItem
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi

/** Main Tab Screen only for Tab navigation
 * manages bottom tabs */
class BottomTabManager(
    private val offlineLogin: Boolean
) : Screen {
    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    override fun Content() {
        if (!offlineLogin) {
            TabNavigator(FileTabScreen()) {
                Scaffold(
                    bottomBar = {
                        AdaptiveNavigationBar {
                            TabItem(FileTabScreen())
                            TabItem(DownloadScreen())
                            TabItem(SettingsTabScreen())
                        }
                    }
                ) { paddingValues ->
                    // give screens the padding values because of the bottom bar
                    Column(modifier = Modifier.padding(paddingValues)) {
                        CurrentTab()
                    }
                }
            }
        } else {
            TabNavigator(DownloadScreen()) {
                Scaffold(
                    bottomBar = {
                        AdaptiveNavigationBar {
                            TabItem(FileTabScreen())
                            TabItem(DownloadScreen())
                            TabItem(SettingsTabScreen())
                        }
                    }
                ) { paddingValues ->
                    // give screens the padding values because of the bottom bar
                    Column(modifier = Modifier.padding(paddingValues)) {
                        CurrentTab()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    private fun RowScope.TabItem(tab: Tab) {
        val tabNavigator = LocalTabNavigator.current

        AdaptiveNavigationBarItem(
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

                    Text(text = tab.options.title)
                }
            },
        )
    }
}