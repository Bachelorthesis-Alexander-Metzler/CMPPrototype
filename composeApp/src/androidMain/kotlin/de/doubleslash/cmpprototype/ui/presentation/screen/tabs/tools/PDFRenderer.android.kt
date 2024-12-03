package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.tools

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File
import java.io.FileOutputStream

actual fun renderAllPdfPagesToBitmaps(pdfData: ByteArray): List<ImageBitmap> {
    // create temp file
    val tempFile = File.createTempFile("tempPdf", ".pdf").apply {
        FileOutputStream(this).use { it.write(pdfData) }
    }

    // open pdf renderer
    val parcelFileDescriptor =
        ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
    val pdfRenderer = PdfRenderer(parcelFileDescriptor)

    // list for rendered pages
    val bitmaps = mutableListOf<ImageBitmap>()

    // render each page
    for (pageIndex in 0 until pdfRenderer.pageCount) {
        val page = pdfRenderer.openPage(pageIndex)
        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()

        bitmaps.add(bitmap.asImageBitmap())
    }

    pdfRenderer.close()
    tempFile.delete()

    return bitmaps
}
