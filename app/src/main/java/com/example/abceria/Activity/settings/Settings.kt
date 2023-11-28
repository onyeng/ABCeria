package com.example.abceria.Activity.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.abceria.Activity.auth.Auth
import com.example.abceria.Activity.auth.Login
import com.example.abceria.R
import com.example.abceria.db.DB
import com.example.abceria.state.StateFactory
import com.google.firebase.auth.FirebaseUser

class Settings : AppCompatActivity() {

    private val auth = Auth.getAuthInstance()
    private lateinit var btnLogout: Button
    private var currentUser: FirebaseUser? = null
    private val firestore = DB.getFirestoreInstance()
    private val firebaseStorage = DB.getStorageInstance()
    //components
    private lateinit var btnEditProfile: Button
    private lateinit var tvFullName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var imvProfile: ImageView

    private lateinit var toolbar: Toolbar
    //userState
    private val userState = StateFactory.getUserStateInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initComponents() ;setProfileImage()

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        btnLogout.setOnClickListener {
            auth.signOut()
            StateFactory.destroy()
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }
        btnEditProfile.setOnClickListener {
            val intent = Intent(this,editProfile::class.java)
            this.startActivity(intent)
        }

//        auth.addAuthStateListener {
//            if(currentUser == null){
//                val intent = Intent(this, Login::class.java)
//                startActivity(intent)
//            }
//
//        }

    }

    private fun initComponents(){
        btnLogout = findViewById(R.id.settings_btn_logout)
        btnEditProfile = findViewById(R.id.settings_btn_edit_profile)
        tvFullName = findViewById(R.id.settings_tv_fullName)
        tvEmail = findViewById(R.id.settings_tv_email)
        imvProfile = findViewById(R.id.settings_imv_profileImage)
        toolbar = findViewById(R.id.settings_toolbar)
    }

    private fun setProfileImage(){
        imvProfile.setImageBitmap(userState.profilePicture)

    }

    private fun setProfileDetail(){
        tvFullName.text = userState.fullName
        tvEmail.text = userState.email
    }



    override fun onStart() {
        super.onStart()
        currentUser = auth.currentUser
       if(auth.currentUser == null){
           startActivity(Intent(this,Login::class.java))
       }
        setProfileDetail()
        setProfileImage()
    }

}

