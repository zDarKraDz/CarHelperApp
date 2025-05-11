package com.example.carhelperapp

import com.google.firebase.auth.FirebaseAuth

class SignUp {
    public fun signUp(
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