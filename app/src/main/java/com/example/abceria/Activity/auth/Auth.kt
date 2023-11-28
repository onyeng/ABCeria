package com.example.abceria.Activity.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


object Auth {
    fun getAuthInstance(): FirebaseAuth {
        return Firebase.auth
    }
}