package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //to make this activity as splash screen we use handler
        Handler(Looper.getMainLooper()).postDelayed({
            val intent= Intent(this,sunny_activity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}