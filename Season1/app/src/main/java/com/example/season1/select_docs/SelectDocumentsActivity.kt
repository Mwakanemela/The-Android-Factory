package com.example.season1.select_docs

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.season1.R
import com.example.season1.fileType
import com.example.season1.formatFileSize

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val TAG = "SelectDocumentsActivity"
class SelectDocumentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_select_documents)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button3: Button = findViewById(R.id.button3)
        button3.setOnClickListener {
//            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//                addCategory(Intent.CATEGORY_OPENABLE)
//                type = "applications/*"
////            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//            }

//            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//                addCategory(Intent.CATEGORY_OPENABLE)
//                type = "application/pdf"
//
//                // Optionally, specify a URI for the file that should appear in the
//                // system file picker when it loads.
////                putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
//            }
//            val intent = Intent()
////            intent.type = "*/*"
//            intent.type = "application/pdf"
//            intent.action = Intent.ACTION_GET_CONTENT
//            intent.flags = FLAG_GRANT_READ_URI_PERMISSION
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

            val intent = Intent().apply {
                type = "application/*" // Use a wildcard type
                action = Intent.ACTION_GET_CONTENT
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf", "application/msword", "application/vnd.ms-excel"))


            }
            getDocumentContent.launch(intent)

        }

    }

    private val getDocumentContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    // Handle the selected document URI
                    handleDocumentUri(uri)
                }
            }
        }


    @SuppressLint("SetTextI18n")
    private fun handleDocumentUri(uri: Uri) {
        // Handle the selected document URI here
        // For example, you can retrieve the file name
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            val fileName = cursor.getString(nameIndex)
            val fileSize = cursor.getLong(sizeIndex)


//            val numberOfPages = getNumberOfPagesFromUri(this, uri)
            var numberOfPages = 0
            val formattedFileSize = formatFileSize(fileSize)

            val documentType = fileType(fileName)
            Log.d("handleDocumentUri", ": $fileName")
            Log.d("handleDocumentUri", "uri $uri")
            Log.d("handleDocumentUri", "formattedFileSize $formattedFileSize")


//            numberOfPages = when (documentType) {
//                "doc" -> {
//                    getNumberOfPagesFromUriForDoc(uri)
//                }
//
//                "docx", "xlsx", "pptx" -> {
//                    getNumberOfPagesFromUriForDocx(uri)
//                }
//
//                else -> {
//                    getNumberOfPagesFromUriForPDF(this, uri)
//                }
//            }

            retrieveFirstPageAndSaveAsImage(this, uri)
//            binding.content.text =
        }
    }
    // Function to save bitmap to cache directory
    private fun saveBitmapToCache(context: Context, bitmap: Bitmap) {
        val cacheDir = context.cacheDir // Get the cache directory

        // Create a file in the cache directory
        val file = File(cacheDir, "first_page_image.png")

        try {
            // Write the bitmap data to the file
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }
            // Bitmap saved successfully
            Log.d(TAG, "Bitmap saved to cache directory: ${file.absolutePath}")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private fun retrieveFirstPageAndSaveAsImage(context: Context, uri: Uri) {
        val contentResolver = context.contentResolver

        try {
            // Open a ParcelFileDescriptor from the URI
            val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")

            parcelFileDescriptor?.use { pfd ->
                // Create a PdfRenderer from the ParcelFileDescriptor
                val pdfRenderer = PdfRenderer(pfd)

                // Open the first page
                val page = pdfRenderer.openPage(0)

                // Create a bitmap of the page
                val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)

                // Render the page content into the bitmap
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                // Close the page and the PdfRenderer
                page.close()
                pdfRenderer.close()

                // Save bitmap to cache directory
                saveBitmapToCache(context, bitmap)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
//    private fun getNumberOfPagesFromUriForPDF(context: Context, uri: Uri): Int {
//        var inputStream: InputStream? = null
//        var numberOfPages = 0
//        try {
//            inputStream = context.contentResolver.openInputStream(uri)
//            if (inputStream != null) {
//                val document = PDDocument.load(inputStream)
//                numberOfPages = document.numberOfPages
//                document.close()
//            }
//        } catch (e: Exception) {
//            // Handle exceptions
//            Log.e("getNumberOfPagesFromUri", "getNumberOfPagesFromUri ex $e")
//            e.printStackTrace()
//        } finally {
//            inputStream?.close()
//        }
//        return numberOfPages
//    }
//    private fun getNumberOfPagesFromUriForDocx(uri: Uri): Int {
//        var numberOfPages = 0
//        val inputStream: InputStream = contentResolver.openInputStream(uri) ?: return 0
//        val xwpfDocument = XWPFDocument(inputStream)
//
//        // Count the paragraphs or sections in the document
//        numberOfPages = xwpfDocument.paragraphs.size
//
//        xwpfDocument.close()
//        inputStream.close()
//
//        return numberOfPages
//
//    }
//    private fun getNumberOfPagesFromUriForDoc(uri: Uri): Int {
//        var numberOfPages = 0
//        val inputStream: InputStream = contentResolver.openInputStream(uri) ?: return 0
//        val hwpfDocument = HWPFDocument(inputStream)
//        val range = hwpfDocument.range
//
//        // Count the paragraphs within the range
//        val paragraphs = Range(range.startOffset, range.endOffset, hwpfDocument).numParagraphs()
//        numberOfPages = paragraphs
//
//        hwpfDocument.close()
//        inputStream.close()
//
//        return numberOfPages
//
//    }
}