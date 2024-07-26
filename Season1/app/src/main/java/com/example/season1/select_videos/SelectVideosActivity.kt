package com.example.season1.select_videos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.season1.R
import com.example.season1.select_audios.model.AudioActivity

class SelectVideosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_select_videos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val button: Button = findViewById(R.id.button2)
        button.setOnClickListener {
            val intent = Intent(this@SelectVideosActivity, VideosActivity::class.java)
            startActivity(intent)
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "audio/*"
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            pickAudioFile.launch(intent)
        }

    }
}