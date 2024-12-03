package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.tools

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File

actual fun getPdfPageCount(pdfData: ByteArray): Int {
    // create temp file
    val tempFile = File.createTempFile("tempPdf", ".pdf").apply {
        writeBytes(pdfData)
    }

    // open pdf renderer
    val parcelFileDescriptor =
        ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
    val pdfRenderer = PdfRenderer(parcelFileDescriptor)

    val pageCount = pdfRenderer.pageCount

    pdfRenderer.close()
    tempFile.delete()

    return pageCount
}

actual fun renderPdfPageToBitmap(pdfData: ByteArray, pageIndex: Int): ImageBitmap {
    // create temp file
    val tempFile = File.createTempFile("tempPdf", ".pdf").apply {
        writeBytes(pdfData)
    }

    // open pdf renderer
    val parcelFileDescriptor =
        ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
    val pdfRenderer = PdfRenderer(parcelFileDescriptor)

    // check if page exists
    if (pageIndex < 0 || pageIndex >= pdfRenderer.pageCount) {
        pdfRenderer.close()
        tempFile.delete()
        throw IllegalArgumentException("Page index out of bounds")
    }

    // render page
    val page = pdfRenderer.openPage(pageIndex)
    val width = page.width
    val height = page.height
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    page.close()

    pdfRenderer.close()
    tempFile.delete()

    return bitmap.asImageBitmap()
}


