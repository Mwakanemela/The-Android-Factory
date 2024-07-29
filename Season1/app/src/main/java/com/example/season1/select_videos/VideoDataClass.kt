package com.example.season1.select_videos

import android.net.Uri

data class VideoDataClass(
    val videoList : String,
    val videoUri: Uri,
    var isSelected: Boolean = false
)
