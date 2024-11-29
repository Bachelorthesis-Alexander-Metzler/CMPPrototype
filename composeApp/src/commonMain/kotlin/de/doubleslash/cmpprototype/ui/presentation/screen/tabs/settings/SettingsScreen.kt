package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.permissions
import cmpprototype.composeapp.generated.resources.setting_app_theme
import cmpprototype.composeapp.generated.resources.settings_tab_title
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.components.CustomTopAppBar
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.settings.details.permission_details.PermissionDetailsScreen
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.settings.details.theme_details.ThemeDetailsScreen
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveHorizontalDivider
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.icons.AdaptiveIcons
import io.github.alexzhirkevich.cupertino.adaptive.icons.KeyboardArrowRight
import org.jetbrains.compose.resources.stringResource

class SettingsScreen : Screen {
    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = CustomTopAppBar(text = stringResource(Res.string.settings_tab_title))
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                // theme settings
                item {
                    SettingNavigationItem(
                        title = stringResource(Res.string.setting_app_theme),
                        onClick = { navigator.push(ThemeDetailsScreen()) }
                    )
                    AdaptiveHorizontalDivider()
                }

                // permission settings
                item {
                    SettingNavigationItem(
                        title = stringResource(Res.string.permissions),
                        onClick = { navigator.push(PermissionDetailsScreen()) }
                    )
                    AdaptiveHorizontalDivider()
                }
            }
        }
    }

    @Composable
    private fun SettingNavigationItem(
        title: String,
        onClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = MaterialTheme.typography.bodyLarge)
            Icon(
                imageVector = AdaptiveIcons.Outlined.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}