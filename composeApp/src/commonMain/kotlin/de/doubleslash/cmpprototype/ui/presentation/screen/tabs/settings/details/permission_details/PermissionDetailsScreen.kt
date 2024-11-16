package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.settings.details.permission_details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.content_description_back
import cmpprototype.composeapp.generated.resources.ic_permissions
import cmpprototype.composeapp.generated.resources.setting_enable_dark_mode
import cmpprototype.composeapp.generated.resources.setting_use_system_theme
import cmpprototype.composeapp.generated.resources.theme_details_title
import de.doubleslash.cmpprototype.ui.theme.LocalDarkModeSettings
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveButton
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveHorizontalDivider
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveIconButton
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveSwitch
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveTopAppBar
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.icons.AdaptiveIcons
import io.github.alexzhirkevich.cupertino.adaptive.icons.KeyboardArrowLeft
import io.github.alexzhirkevich.cupertino.adaptive.icons.KeyboardArrowRight
import io.github.alexzhirkevich.cupertino.adaptive.icons.Person
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class PermissionDetailsScreen : Screen {
    @OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) { factory.createPermissionsController() }

        Scaffold(
            topBar = {
                AdaptiveTopAppBar(
                    title = { Text("Permission Settings") },
                    navigationIcon = {
                        AdaptiveIconButton(
                            onClick = { navigator.pop() },
                            content = {
                                Icon(
                                    imageVector = AdaptiveIcons.Outlined.KeyboardArrowLeft,
                                    contentDescription = stringResource(Res.string.content_description_back)
                                )
                            }
                        )
                    },
                    adaptation = {
                        material {
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            },

            ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                item {
                    OpenSettingsItem(
                        title = "Manage Permissions in Settings",
                        onClick = {
                            controller.openAppSettings()
                        }
                    )
                    AdaptiveHorizontalDivider()
                }
            }
        }
    }

    @Composable
    private fun OpenSettingsItem(
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
                imageVector = vectorResource(Res.drawable.ic_permissions),
                contentDescription = null
            )
        }
    }
}