package de.doubleslash.cmpprototype


import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import de.doubleslash.cmpprototype.di.initKoin
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.BottomTabManager
import de.doubleslash.cmpprototype.ui.theme.AppTheme
import de.doubleslash.cmpprototype.ui.theme.DarkModeSettings
import de.doubleslash.cmpprototype.ui.theme.LocalDarkModeSettings
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    remember { initKoin() }

    // create a mutable state object for DarkModeSettings, storing global dark mode settings
    val darkModeSettings = remember { mutableStateOf(DarkModeSettings()) }

    // provide the DarkModeSettings to the Composition using CompositionLocalProvider
    CompositionLocalProvider(LocalDarkModeSettings provides darkModeSettings){
        AppTheme(
            content = {
//            Navigator(LoginScreen()) { navigator ->
//                    ScaleTransition(navigator = navigator)
                Navigator(BottomTabManager()) { navigator ->
                    SlideTransition(navigator = navigator)
                }
            }
        )
    }

}