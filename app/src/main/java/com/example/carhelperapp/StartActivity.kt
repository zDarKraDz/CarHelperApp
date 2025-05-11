package com.example.carhelperapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carhelperapp.databinding.ActivityStartBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class StartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStartBinding
    private val auth = Firebase.auth
    private val login = Login()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.goSignUpUser.setOnClickListener{
            startActivity(Intent(this@StartActivity,SignupActivity::class.java))
            finish()
        }

        binding.loginButton.setOnClickListener{
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()
            login.logInFun(
                auth,
                email,
                password,
                onLogInSuccess = {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                },
                onLogInFailure = {error ->
                    binding.errorText.text = error
                })

        }
    }

}
