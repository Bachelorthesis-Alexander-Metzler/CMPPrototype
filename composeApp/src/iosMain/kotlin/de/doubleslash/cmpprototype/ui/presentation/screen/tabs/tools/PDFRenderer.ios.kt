package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.tools

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSize
import platform.Foundation.NSData
import platform.PDFKit.PDFDocument
import platform.PDFKit.PDFPage
import platform.UIKit.UIGraphicsBeginImageContext
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIGraphicsPopContext
import platform.UIKit.UIGraphicsPushContext

actual fun renderAllPdfPagesToBitmaps(pdfData: ByteArray): List<ImageBitmap> {
    val data = NSData.create(pdfData)
    val pdfDocument = PDFDocument(data)
        ?: error("Unable to create PDFDocument from provided data")

    val bitmaps = mutableListOf<ImageBitmap>()

    // Render each page
    for (pageIndex in 0 until pdfDocument.pageCount) {
        val page = pdfDocument.pageAtIndex(pageIndex.toULong())
            ?: continue // Skip invalid pages

        val pageSize = page.boundsForBox(PDFPage.BoxMediaBox).useContents {
            CGSize(this.size.width, this.size.height)
        }

        UIGraphicsBeginImageContext(pageSize)
        val context = UIGraphicsGetImageFromCurrentImageContext()
            ?: continue // Skip if context creation fails

        UIGraphicsPushContext(context)
        page.drawWithBox(PDFPage.BoxMediaBox, CGRectMake(0.0, 0.0, pageSize.width, pageSize.height))
        UIGraphicsPopContext()

        val image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        image?.let {
            bitmaps.add(it.toComposeImageBitmap())
        }
    }

    return bitmaps
}
