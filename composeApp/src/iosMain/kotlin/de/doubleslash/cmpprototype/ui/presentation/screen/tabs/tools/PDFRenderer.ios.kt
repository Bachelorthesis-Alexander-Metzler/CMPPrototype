package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.tools

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSData
import platform.PDFKit.PDFDocument
import platform.PDFKit.PDFPage
import platform.UIKit.UIGraphicsBeginImageContext
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext


actual fun getPdfPageCount(pdfData: ByteArray): Int {
    // load pdf data into an NSData object
    val data = NSData.create(pdfData)
        ?: throw IllegalArgumentException("Invalid PDF data")

    // create pdf document
    val pdfDocument = PDFDocument(data)
        ?: throw IllegalArgumentException("Unable to create PDFDocument from data")

    // return page count
    return pdfDocument.pageCount.toInt()
}


actual fun renderPdfPageToBitmap(pdfData: ByteArray, pageIndex: Int): ImageBitmap {
    // load pdf data into an NSData object
    val data = NSData.create(pdfData)
        ?: throw IllegalArgumentException("Invalid PDF data")

    // create pdf document
    val pdfDocument = PDFDocument(data)
        ?: throw IllegalArgumentException("Unable to create PDFDocument from data")

    // get page at index
    val page = pdfDocument.pageAtIndex(pageIndex.toULong())
        ?: throw IllegalArgumentException("Page index out of bounds")

    // get page size
    val pageSize = page.boundsForBox(PDFPage.BoxMediaBox).useContents {
        CGRectMake(this.origin.x, this.origin.y, this.size.width, this.size.height)
    }

    // create image context
    UIGraphicsBeginImageContext(pageSize.size)
    page.drawWithBox(PDFPage.BoxMediaBox, pageSize)
    val uiImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()

    return uiImage?.toComposeImageBitmap()
        ?: throw IllegalStateException("Failed to render PDF page")
}
