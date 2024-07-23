package com.example.season1.select_audios.model

import android.net.Uri

data class Audio(
    val uri: Uri,
    val name: String,
    val duration: Int,
    val size: Int
)
