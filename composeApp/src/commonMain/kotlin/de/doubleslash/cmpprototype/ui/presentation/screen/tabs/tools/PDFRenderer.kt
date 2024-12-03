package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.tools

import androidx.compose.ui.graphics.ImageBitmap

expect fun renderAllPdfPagesToBitmaps(pdfData: ByteArray): List<ImageBitmap>