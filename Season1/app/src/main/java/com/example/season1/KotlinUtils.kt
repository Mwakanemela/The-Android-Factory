package com.example.season1

import android.annotation.SuppressLint
import kotlin.math.log10
import kotlin.math.pow

@SuppressLint("DefaultLocale")
fun formatFileSize(sizeBytes: Long): String {
    if (sizeBytes <= 0) return "0 B"

    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(sizeBytes.toDouble()) / log10(1024.0)).toInt()

    return String.format(
        "%.2f %s",
        sizeBytes / 1024.0.pow(digitGroups.toDouble()),
        units[digitGroups]
    )
}

fun fileType(fileName: String): String {
    val extension = fileName.substringAfterLast(".", "Unknown")
    return extension
}
