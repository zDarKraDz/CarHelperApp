package com.example.carhelperapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carhelperapp.databinding.ActivitySignupBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val auth = Firebase.auth
    private val signUp = SignUp()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.goLogInUser.setOnClickListener {
            startActivity(Intent(this@SignupActivity, StartActivity()::class.java))
        }

        binding.createButton.setOnClickListener {
            val email = binding.signupEmail.text.toString().trim()
            val password = binding.signupPassword.text.toString().trim()
            signUp.signUp(
                auth,
                email,
                password,
                onSignUpSuccess = {
                    startActivity(Intent(this, MainActivity::class.java))
                },
                onSignUpFailure = {error ->
                    binding.errorText.text = error
                })
        }
    }
}


