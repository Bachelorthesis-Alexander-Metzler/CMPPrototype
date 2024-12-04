package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.tools

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.Foundation.*
import platform.PDFKit.PDFDocument
import platform.PDFKit.PDFPage
import platform.UIKit.UIGraphicsBeginImageContext
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.CoreGraphics.CGSizeMake
import platform.CoreGraphics.CGContextSaveGState
import platform.CoreGraphics.CGContextRestoreGState
import platform.CoreGraphics.CGContextTranslateCTM
import platform.CoreGraphics.CGContextScaleCTM

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import platform.CoreGraphics.CGRectGetHeight
import platform.CoreGraphics.CGRectGetWidth
import platform.UIKit.UIGraphicsGetCurrentContext
import platform.Foundation.NSData

actual fun getPdfPageCount(pdfData: ByteArray): Int {
    println("Converting ByteArray to NSData")
    val nsData = pdfData.toNSData()
    println("Creating PDFDocument from NSData")
    val pdfDocument = PDFDocument(nsData)

    println("PDFDocument created successfully, page count: ${pdfDocument.pageCount}")
    return pdfDocument.pageCount.toInt()
}

@OptIn(ExperimentalForeignApi::class)
actual fun renderPdfPageToBitmap(pdfData: ByteArray, pageIndex: Int): ImageBitmap {
    println("Converting ByteArray to NSData")
    val nsData = pdfData.toNSData()

    println("Creating PDFDocument from NSData")
    val pdfDocument = PDFDocument(nsData)

    println("Getting page at index $pageIndex")
    val page: PDFPage = pdfDocument.pageAtIndex(pageIndex.toULong())
        ?: throw IllegalArgumentException("Page index out of bounds")

    println("Getting MediaBox for page")
    val mediaBox: Long = 0
    val pageRect = page.boundsForBox(mediaBox)
    println("Page rect: $pageRect")

    println("Calculating width and height from page rect")
    val width = CGRectGetWidth(pageRect)
    val height = CGRectGetHeight(pageRect)
    println("Width: $width, Height: $height")

    println("Creating CGSize from width and height")
    val size = CGSizeMake(width, height)

    println("Starting image context")
    UIGraphicsBeginImageContext(size)
    val context = UIGraphicsGetCurrentContext()
        ?: throw IllegalStateException("Failed to get graphics context")
    println("Graphics context retrieved")

    // Flip the context vertically to correct the upside-down rendering
    println("Flipping the context vertically")
    CGContextSaveGState(context)
    CGContextTranslateCTM(context, 0.0, height)
    CGContextScaleCTM(context, 1.0, -1.0)

    println("Rendering page to graphics context")
    page.drawWithBox(mediaBox, toContext = context)
    CGContextRestoreGState(context)

    println("Retrieving image from context")
    val image = UIGraphicsGetImageFromCurrentImageContext()
        ?: throw IllegalArgumentException("Failed to retrieve image from context")

    println("Ending image context")
    UIGraphicsEndImageContext()

    println("Converting UIImage to ImageBitmap")
    return image.toComposeImageBitmap()
}


@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun ByteArray.toNSData(): NSData {
    println("Converting ByteArray to NSData")
    return this.usePinned {
        NSData.create(bytes = it.addressOf(0), length = this.size.toULong())
    }
}

private fun UIImage.toComposeImageBitmap(): ImageBitmap {
    println("Starting conversion from UIImage to ImageBitmap")

    // Convert UIImage to PNG NSData
    val pngData: NSData = UIImagePNGRepresentation(this)
        ?: throw IllegalArgumentException("Failed to get PNG representation of UIImage")

    // Convert NSData to ByteArray
    val byteArray = pngData.toByteArray()

    // Use Skia Image to decode PNG
    val skiaImage = Image.makeFromEncoded(byteArray)

    println("Conversion complete")
    return skiaImage.toComposeImageBitmap()
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val length = this.length.toInt()
    val bytes: CPointer<ByteVar> = this.bytes?.reinterpret()
        ?: throw IllegalArgumentException("NSData is empty")
    return ByteArray(length) { index -> bytes[index].toByte() }
}
