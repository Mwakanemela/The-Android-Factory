package com.example.season1.select_audios

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.season1.R
import com.example.season1.select_audios.model.Audio
import com.example.season1.select_audios.model.AudioActivity
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val TAG = "AudiosActivity"
class AudiosActivity : AppCompatActivity() {

    val videoList = mutableListOf<Audio>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audios)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this@AudiosActivity, AudioActivity::class.java)
            startActivity(intent)
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "audio/*"
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            pickAudioFile.launch(intent)
        }
    }

    private val pickAudioFile =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                // Handle the selected audio file here
                val selectedAudioUri = data?.data

                Log.d(TAG, "selected audio uri: $selectedAudioUri ")
                // Use the selectedAudioUri as needed (e.g., save it to a database, process it, etc.)
            }
        }
}