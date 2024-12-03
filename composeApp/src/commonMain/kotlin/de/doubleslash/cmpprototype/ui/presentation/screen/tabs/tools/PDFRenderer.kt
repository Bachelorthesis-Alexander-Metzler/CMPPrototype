package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.tools

import androidx.compose.ui.graphics.ImageBitmap

expect fun getPdfPageCount(pdfData: ByteArray): Int

expect fun renderPdfPageToBitmap(pdfData: ByteArray, pageIndex: Int): ImageBitmap