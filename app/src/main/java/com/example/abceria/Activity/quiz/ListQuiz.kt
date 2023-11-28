package com.example.abceria.Activity.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.abceria.R
import com.example.abceria.adapter.ListQuizAdapter
import com.example.abceria.db.DB
import com.example.abceria.model.quiz.Quiz
import com.example.abceria.utility.IntentModulUtils

class ListQuiz : AppCompatActivity() {
    private lateinit var rvQuiz : RecyclerView
    private val quizList = arrayListOf<Quiz>()

    private val firestore = DB.getFirestoreInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_quiz)

        val modulId = intent.getStringExtra(IntentModulUtils.MODUL_ID)
        initComponents()
        getQuizListFromDb(modulId)
    }

    private fun initComponents(){
        rvQuiz = findViewById(R.id.list_quiz_rv_quiz)
        rvQuiz.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
    }

    private fun getQuizListFromDb(modulId: String?) {
        if (modulId != null) {
            firestore.collection("modul").document(modulId).collection("quiz").get().addOnSuccessListener {
                it.documents.forEach { document ->
                    val quiz = Quiz()
                    quiz.id = document.id
                    quiz.modulId = modulId
                    quiz.title = document.get("title") as String
                    quiz.description = document.get("description") as String
                    quizList.add(quiz)
                }
                rvQuiz.adapter = ListQuizAdapter(quizList,this)
            }
        }
    }
}