package com.example.carhelperapp

import com.google.firebase.auth.FirebaseAuth

class Login {
    public fun logInFun(
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