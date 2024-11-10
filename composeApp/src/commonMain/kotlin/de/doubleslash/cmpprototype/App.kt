package de.doubleslash.cmpprototype


import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import de.doubleslash.cmpprototype.di.initKoin
import de.doubleslash.cmpprototype.ui.presentation.screen.login.LoginScreen
import de.doubleslash.cmpprototype.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    initKoin()

    AppTheme {
        Navigator(LoginScreen()) { navigator ->
            SlideTransition(navigator = navigator)
        }
//        Navigator(BottomTabManager()) { navigator ->
//            SlideTransition(navigator = navigator)
//        }
    }
}
