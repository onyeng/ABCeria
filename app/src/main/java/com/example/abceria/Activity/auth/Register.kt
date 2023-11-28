package com.example.abceria.Activity.auth

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.abceria.R
import com.example.abceria.db.DB
import com.example.abceria.model.user.User
import com.google.firebase.auth.FirebaseUser

class Register : AppCompatActivity() {
    private val auth = Auth.getAuthInstance()
    private val fireStore = DB.getFirestoreInstance()
    private var currentUser: FirebaseUser? = null

    private lateinit var btnRegister : Button
    private lateinit var pbRegister: ProgressBar
    private lateinit var etEmail : EditText
    private lateinit var etFullName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var tvAlert: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)
        supportActionBar?.hide()

        initComponents()
        //set pb to invisible before register
        pbRegister.visibility = View.GONE
        btnRegister.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            createUserWithEmailPassword(email,password)
        }
    }

    private fun saveUser(){
        val userRef = fireStore.collection("user")
        val userTobeAdded = User(
            fullName = etFullName.text.toString(),
            username = etUsername.text.toString(),
            profilePicture = "",
            score = 0
        )

    }

    private fun createUserWithEmailPassword(email: String,password: String){
        pbRegister.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
            if(it.isSuccessful){
                pbRegister.visibility = View.GONE
                tvAlert.setTextColor(Color.GREEN)
                tvAlert.text = "Register Success"
                //update UI
                saveUser()
                startActivity(Intent(this,Login::class.java))
            }else {
                Toast.makeText(this,"Auth failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initComponents(){
        btnRegister = findViewById(R.id.register_btn_register)
        pbRegister = findViewById(R.id.register_pb_register)
        etEmail = findViewById(R.id.register_et_email)
        etFullName = findViewById(R.id.register_et_fullName)
        etUsername = findViewById(R.id.register_et_username)
        etPassword = findViewById(R.id.register_et_password)
        etConfirmPassword = findViewById(R.id.register_et_confirm_password)
        tvAlert = findViewById(R.id.register_et_alert)
    }

    override fun onStart() {
        super.onStart()
        currentUser = auth.currentUser

    }

}