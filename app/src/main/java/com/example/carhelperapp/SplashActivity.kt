package com.example.carhelperapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.yandex.mapkit.MapKitFactory


class SplashActivity : AppCompatActivity() {
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("2482fb77-39b0-491e-9b89-71e242e9e69f")
        MapKitFactory.initialize(this)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        if(auth.currentUser != null){
            // If the user logged into the account earlier
            switching(MainActivity::class.java)
        }else{
            // If the user has not logged into the account earlier
            switching(StartActivity::class.java)
        }

    }
    // Delay between SplashActivity and targetActivity
    private fun switching(targetActivity: Class<*>){
        lifecycleScope.launch {
            delay(3000)
            startActivity(Intent(this@SplashActivity, targetActivity))
            finish()
        }
    }
}
