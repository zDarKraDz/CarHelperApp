package com.example.carhelperapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carhelperapp.databinding.ActivityStartBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class StartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStartBinding
    private val auth = Firebase.auth
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

        }

        binding.loginButton.setOnClickListener{
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()
            logIn(
                auth,
                email,
                password,
                onLogInSuccess = {
                    //Log.d("myLog", "Login Success")
                    startActivity(Intent(this, MainActivity::class.java))
                },
                onLogInFailure = {error ->
                    binding.errorText.text = error
                    //Log.d("MyLog", "Failure: $error")
                })

        }
    }
    private fun logIn(
        auth: FirebaseAuth,
        email: String,
        password: String,
        onLogInSuccess: () -> Unit,
        onLogInFailure: (String) -> Unit
    ) {
        // If user has not entered the data
        if(email.isBlank() || password.isBlank()){
            onLogInFailure("Email and password cannot be empty")
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) onLogInSuccess()

            }
            .addOnFailureListener {
                onLogInFailure(it.message ?:"Log In Error")
            }
    }
}
