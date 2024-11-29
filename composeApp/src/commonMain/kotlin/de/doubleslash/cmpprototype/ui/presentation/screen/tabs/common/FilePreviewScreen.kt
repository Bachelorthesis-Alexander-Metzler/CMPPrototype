package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.content_description_back
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveIconButton
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveTopAppBar
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.icons.AdaptiveIcons
import io.github.alexzhirkevich.cupertino.adaptive.icons.KeyboardArrowLeft
import org.jetbrains.compose.resources.stringResource

class FilePreviewScreen : Screen {
    @OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                AdaptiveTopAppBar(
                    title = { Text("File Preview") },
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
            }
        ) { paddingValues ->
            Text(
                modifier = Modifier.padding(paddingValues),
                text = "File Preview",
            )

        }
    }

}