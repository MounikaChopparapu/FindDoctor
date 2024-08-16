package com.example.finddoctor

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_base)

        supportFragmentManager.beginTransaction()
            .add(R.id.main, SpecalityFragment())
            .addToBackStack(null).commit()

    }
}