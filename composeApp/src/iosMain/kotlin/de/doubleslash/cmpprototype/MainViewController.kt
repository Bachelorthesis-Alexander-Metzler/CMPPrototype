package de.doubleslash.cmpprototype

import androidx.compose.ui.window.ComposeUIViewController
import de.doubleslash.cmpprototype.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}