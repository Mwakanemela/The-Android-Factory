package com.example.season1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.season1.adapter.SimpleAdapter

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val learnMoreButton: Button = findViewById(R.id.learnMoreButtton)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val data = ArrayList<String>()

        repeat(15) {
            index -> data.add("Element $index")
        }
        val simpleAdapter = SimpleAdapter()
        simpleAdapter.setData(data)
        recyclerView.adapter = simpleAdapter

        learnMoreButton.setOnClickListener {
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            startActivity(intent)
        }

    }
}