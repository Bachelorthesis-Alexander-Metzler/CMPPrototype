package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.file_tab_title
import io.github.alexzhirkevich.cupertino.CupertinoTopAppBarDefaults
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveTopAppBar
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar() = @Composable {
    AdaptiveTopAppBar(
        title = { Text(text = stringResource(Res.string.file_tab_title)) },
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