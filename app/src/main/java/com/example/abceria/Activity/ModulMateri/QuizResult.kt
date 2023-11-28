package com.example.abceria.Activity.ModulMateri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.abceria.Activity.auth.Auth
import com.example.abceria.R
import com.example.abceria.db.DB
import com.example.abceria.model.quiz.QuizResult

class QuizResult : AppCompatActivity() {
    private lateinit var tvQuizPoint:TextView
    private lateinit var tvRightAnswer: TextView
    private lateinit var tvWrongAnswer: TextView

    private val firestore = DB.getFirestoreInstance()
    private val auth = Auth.getAuthInstance()

    private var quizPoint: Long  = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val quizResult = intent.getSerializableExtra("answer") as QuizResult
        quizPoint = quizResult.point
        setContentView(R.layout.activity_quiz_result)
        initComponents()
        setQuizDataResult(quizResult)
    }

    private fun initComponents(){
        tvQuizPoint = findViewById(R.id.quiz_result_tv_total_point)
        tvRightAnswer = findViewById(R.id.quiz_result_tv_right_answer)
        tvWrongAnswer = findViewById(R.id.quiz_result_tv_wrong_answer)
    }
    private fun setQuizDataResult(quizResult: QuizResult) {
        tvQuizPoint.text = quizResult.point.toString()
        tvRightAnswer.text = quizResult.rightAnswer.toString()+ " soal"
        tvWrongAnswer.text = quizResult.wrongAnswer.toString()+ " soal"
    }

    private fun updateUserScore(userId: String,point: Long) {
        firestore.collection("user").document(userId).get().addOnSuccessListener {
            val currentScore = it.get("score") as Long
            firestore.collection("user").document(userId).update("score",(currentScore + point )).addOnSuccessListener {

            }
        }
    }

    override fun onStart() {
        super.onStart()
        val userId = auth.currentUser?.uid
        if(userId != null){
            updateUserScore(userId,quizPoint)
        }
    }
}