package com.example.seasontwo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.seasontwo.dataclass.ProgrammingLanguagesTile

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

        val programmingLanguagesTileList = getProgrammingLanguagesTileList()
    }

    private fun getProgrammingLanguagesTileList() : ArrayList<ProgrammingLanguagesTile> {
        return ArrayList<ProgrammingLanguagesTile>().apply{
            add(ProgrammingLanguagesTile(
                id = "Kotlin",
                title = "Kotlin",
                description = "Kotlin description",
                descriptionLong = "Kotlin long description",
                buttonText = "Learn More",
                headerImageResourceId = R.drawable.kotlin_header,
                headerImageUrl = "https://s3.ap-southeast-1.amazonaws.com/arrowhitech.com/wp-content/uploads/2020/09/04082531/1_99YiKjwB2TliKVA-yGogNQ.png"
            ))

            add(ProgrammingLanguagesTile(
                id = "Java",
                title = "Java",
                description = "Java description",
                descriptionLong = "Java long description",
                buttonText = "Learn More",
                headerImageResourceId = R.drawable.java_header,
                headerImageUrl = "https://1001programming.com/wp-content/uploads/2021/12/AddText_04-22-02.27.05-1.jpg"
            ))

            add(ProgrammingLanguagesTile(
                id = "Python",
                title = "Python",
                description = "Python description",
                descriptionLong = "Python long description",
                buttonText = "Learn More",
                headerImageResourceId = R.drawable.python_header,
                headerImageUrl = "https://www.teahub.io/photos/full/21-216425_2048x1152-python-programming-python-az-python-for.jpg"
            ))

            add(ProgrammingLanguagesTile(
                id = "CPP",
                title = "CPP",
                description = "CPP description",
                descriptionLong = "CPP long description",
                buttonText = "Learn More",
                headerImageResourceId = R.drawable.cpp_header,
                headerImageUrl = "https://static.vecteezy.com/system/resources/previews/012/697/300/original/3d-c-programming-language-logo-free-png.png"
            ))

            add(ProgrammingLanguagesTile(
                id = "C#",
                title = "C#",
                description = "C# description",
                descriptionLong = "C# long description",
                buttonText = "Learn More",
                headerImageResourceId = R.drawable.c_sharp_header,
                headerImageUrl = "https://www.adesso-mobile.de/wp-content/uploads/2021/05/c-sharp.jpg"
            ))

            add(ProgrammingLanguagesTile(
                id = "C",
                title = "C",
                description = "C description",
                descriptionLong = "C long description",
                buttonText = "Learn More",
                headerImageResourceId = R.drawable.c_header,
                headerImageUrl = "https://cdn3.vectorstock.com/i/1000x1000/14/42/c-programming-language-icon-vector-46821442.jpg"
            ))
        }
    }
}