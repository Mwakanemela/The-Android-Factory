package com.example.season1.select_audios.model

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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.season1.R
import com.example.season1.databinding.ActivityAudioBinding
import com.example.season1.select_audios.AudioAdapter
import com.example.season1.select_audios.SelectedFilesCount

private const val TAG = "AudioActivity"
class AudioActivity : AppCompatActivity(), SelectedFilesCount {

    private lateinit var binding: ActivityAudioBinding
    private val REQUEST_PERMISSION = 158
    val audioList = mutableListOf<AudioDataClass>()
    val uriList = mutableListOf<Uri>()
    private lateinit var audioAdapter: AudioAdapter
    private val selectedAudios: MutableList<String> = mutableListOf()
    private val selectedUris: MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_audio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        binding.toolbar.backIcon.setOnClickListener {
            onBackPressed()
        }


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION
            )
        } else {
            loadAndDisplayAudio()
        }

        binding.toolbar.likeIcon.setOnClickListener {
            val selectedAudios = audioAdapter.getSelectedAudios()
            Log.d(TAG, "onCreate: selectedAudios size: ${selectedAudios.size} ")
//            for(i in selectedAudios) {
//                this.selectedAudios.add(i.audioList)
//            }
//            val selectedAudioIndex = audioList.indexOf(selectedAudios)
//            val selectedAudioUri = uriList[selectedAudioIndex]
//            val resultIntent = Intent()
//            resultIntent.putExtra("audio_url", audioUrl)
//            resultIntent.putExtra("aUri", selectedAudioUri.toString())
//            setResult(RESULT_OK, resultIntent)
//            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadAndDisplayAudio()
        }
    }


    private fun loadAndDisplayAudio() {
        val projection = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DATA)
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {

                val dataIndex = it.getColumnIndex(MediaStore.Audio.Media.DATA)

                do {
                    val audioPath = it.getString(dataIndex)
                    if (!audioPath.isNullOrBlank()) {
                        audioList.add(AudioDataClass(audioPath))
                        val uri = ContentUris.withAppendedId(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            it.getLong(0)
                        )
                        uriList.add(uri)
                    }
                } while (it.moveToNext())

                val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                audioAdapter = AudioAdapter(audioList, resources, this) { audioUrl ->
                    // Pass the image URL back to the UserActivity
//                    val selectedAudioIndex = audioList.indexOf(audioUrl)
//                    val selectedAudioUri = uriList[selectedAudioIndex]
//                    val resultIntent = Intent()
//                    resultIntent.putExtra("audio_url", audioUrl)
//                    resultIntent.putExtra("aUri", selectedAudioUri.toString())
//                    setResult(RESULT_OK, resultIntent)
//                    finish()
                }
                recyclerView.adapter = audioAdapter
            } else {
                // Handle the case where there are no audio files
                Toast.makeText(this, "No audio files found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onFilesCount(count: Int) {
        binding.toolbar.count.text = "$count"
    }
}

interface DoneClicked{
    fun onDoneClicked()
}