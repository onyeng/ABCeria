package com.example.abceria

import android.content.Intent
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.abceria.Activity.auth.Auth
import com.example.abceria.Activity.auth.Login
import com.example.abceria.fragment.Home
import com.example.abceria.fragment.Leaderboard
import com.example.abceria.fragment.ListModul
import com.example.abceria.fragment.ProfileFrag
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private val auth = Auth.getAuthInstance()
    private var currentUser: FirebaseUser? = null

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.home_action_bar)
        initComponents()
        createComponentListener()
    }

    private fun initComponents(){
        bottomNavigation = findViewById(R.id.home_bottom_navigation)
        toolbar = findViewById(R.id.home_action_bar)
    }

    private fun createComponentListener(){
        bottomNavigation.setOnItemSelectedListener {
            var selectedFragment: Fragment? = null
            when (it.itemId) {
                R.id.leaderboard_icon -> {
                    toolbar.title = "Leaderboard"
                    selectedFragment = Leaderboard()
                    true
                }
                R.id.home_icon -> {
                    toolbar.title = "Home"
                    selectedFragment = Home()
                    true
                }
                R.id.materi_icon -> {
                    //not yet implemented
                    toolbar.title = "Modul"
                    supportActionBar?.title = "Materi"
                    selectedFragment = ListModul()
                    true
                }
                R.id.profil_icon -> {
                    toolbar.title = "Profile"
                    selectedFragment = ProfileFrag()
                    supportActionBar?.title = "Kuis"
                    true
                }
                else -> false
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.home_fragment_container,selectedFragment).commit()
            }
            true
        }
        bottomNavigation.selectedItemId = R.id.home_icon
    }

    override fun onStart() {
        super.onStart()
        currentUser = auth.currentUser
        if(currentUser == null){
            startActivity(Intent(this,Login::class.java))
        }
    }

}