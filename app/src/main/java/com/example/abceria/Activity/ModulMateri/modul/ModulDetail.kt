package com.example.abceria.Activity.ModulMateri.modul

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.abceria.Activity.ModulMateri.ListMateri
import com.example.abceria.Activity.quiz.ListQuiz
import com.example.abceria.R
import com.example.abceria.utility.IntentModulUtils

class ModulDetail : AppCompatActivity() {
    private lateinit var modulTitle: TextView
    private lateinit var materiCard : CardView
    private lateinit var quizCard : CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modul_detail)
        initComponents()
        val id: String? = intent.getStringExtra(IntentModulUtils.MODUL_ID)
        val title: String? = intent.getStringExtra(IntentModulUtils.MODUL_TITLE)
        modulTitle.text = title

        materiCard.setOnClickListener {
            val intent = Intent(this,ListMateri::class.java)
            intent.putExtra(IntentModulUtils.MODUL_ID,id)
            startActivity(intent)
        }
        quizCard.setOnClickListener{
            val intent = Intent(this,ListQuiz::class.java)
            intent.putExtra(IntentModulUtils.MODUL_ID,id)
            startActivity(intent)
        }
    }


    private fun initComponents(){
        modulTitle = findViewById(R.id.modul_detail_tv_modul_title)
        materiCard = findViewById(R.id.modul_detail_cv_materi)
        quizCard = findViewById(R.id.modul_detail_cv_quiz)
    }
}