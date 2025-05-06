package com.example.carhelperapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carhelperapp.databinding.ActivitySignupBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    private val auth = Firebase.auth
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
            signUp(
                auth,
                email,
                password,
                onSignUpSuccess = {
                    //Log.d("MyLog", "Success")
                    startActivity(Intent(this, MainActivity::class.java))
                },
                onSignUpFailure = {error ->
                    binding.errorText.text = error
                    //Log.d("MyLog", "Failure: $error")
                })
        }
    }
    private fun signUp(
        auth: FirebaseAuth,
        email: String,
        password: String,
        onSignUpSuccess: () -> Unit,
        onSignUpFailure: (String) -> Unit
    ) {
        //If user has not entered the data
        if(email.isBlank() || password.isBlank()){
            onSignUpFailure("Email and password cannot be empty")
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(it.isSuccessful) onSignUpSuccess()
            }
            .addOnFailureListener {
                onSignUpFailure(it.message ?:"Sign Up Error")
            }
    }
}


