package com.example.season1.select_videos

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.season1.R
import com.example.season1.databinding.ActivityVideosBinding
import com.example.season1.select_audios.AudioAdapter
import com.example.season1.select_audios.SelectedFilesCount
import com.example.season1.select_audios.model.AudioDataClass

class VideosActivity : AppCompatActivity(), SelectedFilesCount {
    private lateinit var binding: ActivityVideosBinding
    private val REQUEST_PERMISSIONS_CODE = 123
    private val imagesList = ArrayList<String>()

    var TAG = ""
    private lateinit var videoAdapter: VideoAdapter
    private val selectedVideos: MutableList<String> = mutableListOf()
    val videoList = mutableListOf<VideoDataClass>()
    val uriList = mutableListOf<Uri>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVideosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.toolbar.likeIcon.setOnClickListener {
            val selectedAudios = videoAdapter.getSelectedVideos()
            Log.d(TAG, "onCreate: selectedAudios size: ${selectedAudios.size} ")
            for(i in selectedAudios) {
                this.selectedVideos.add(i.videoList)
            }

            val arrayListToSend = ArrayList(this.selectedVideos)

            Log.d(TAG, "onCreate: arrayListToSend ${arrayListToSend.size}")
            val resultIntent = Intent()
            resultIntent.putStringArrayListExtra("audio_url", arrayListToSend)
            setResult(RESULT_OK, resultIntent)
            finish()

        }

        binding.toolbar.backIcon.setOnClickListener { onBackPressed() }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSIONS_CODE
            )
        } else {
            loadAndDisplayVideos()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadAndDisplayVideos()
            } else {
                // Handle permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    lateinit var vUri: Uri
    private fun loadAndDisplayVideos() {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_ADDED
        )

        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Video.Media.DATE_ADDED} DESC"
        )

        val videosUri = MediaStore.Video.Media.getContentUri("external")
        cursor?.use {
            if (it.moveToFirst()) {
//                val videoList = mutableListOf<String>()
                val uriList = mutableListOf<Uri>()
                val dataIndex = it.getColumnIndex(MediaStore.Video.Media.DATA)

                do {
                    val videoPath = it.getString(dataIndex)
                    if (!videoPath.isNullOrBlank()) {
                        videoList.add(VideoDataClass(videoPath))
                        val uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, it.getLong(0))
                        uriList.add(uri)
//                        vUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, it.getLong(0))
//                        uriList.add(ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, it.getLong(0)))
                    }
                } while (it.moveToNext())

                val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                val layoutManager = GridLayoutManager(this, 3)
                recyclerView.layoutManager = layoutManager
                videoAdapter = VideoAdapter(videoList, this ) { selectedVideoPath ->
                    // Handle the click event
//                    val selectedVideoIndex = videoList.indexOf(selectedVideoPath)
//                    val selectedVideoUri = uriList[selectedVideoIndex]
//                    val resultIntent = Intent()
//                    resultIntent.putExtra("video_url", selectedVideoPath)
//                    resultIntent.putExtra("vUri", selectedVideoUri.toString())
//                    setResult(Activity.RESULT_OK, resultIntent)
//                    finish()
                }
                recyclerView.adapter = videoAdapter
            } else {
                // Handle the case where there are no videos
                Toast.makeText(this, "No videos found", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun loadImage() {
        val projection = arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA)
        val sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC"

        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        cursor?.use { rs ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)

            while (cursor.moveToNext()) {
                val imageId = cursor.getLong(idColumn)
                val imagePath = cursor.getString(dataColumn)
                val imageUri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    imageId.toString()
                )

                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    imageId
                )
                val selectedImagePath =
                    contentUri.toString() // Get the selected image URI as a string
                imagesList.add(selectedImagePath)
            }

//            val adapter = VideoAdapter(imagesList) {
//
//            }
            binding.recyclerView.layoutManager =
                GridLayoutManager(this, 3) // Grid layout with 3 columns
//            binding.recyclerView.adapter = adapter
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onFilesCount(count: Int) {
        binding.toolbar.count.text = "$count"
    }
}