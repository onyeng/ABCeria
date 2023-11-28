package com.example.abceria.Activity.quiz

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.example.abceria.Activity.ModulMateri.QuizResult
import com.example.abceria.Activity.quiz.dialog.EndQuizDialog
import com.example.abceria.R
import com.example.abceria.db.DB
import com.example.abceria.model.quiz.Question
import com.example.abceria.model.quiz.Quiz
import com.example.abceria.utility.IntentModulUtils
import com.example.abceria.utility.IntentQuizUtils

class QuizDetail : AppCompatActivity() {
    private val firestore = DB.getFirestoreInstance()
    private val firebasestorage = DB.getStorageInstance()
    private lateinit var quizPosition: TextView
    private lateinit var tvQuizPoint: TextView
    private lateinit var btnOptionA : Button
    private lateinit var btnOptionB : Button
    private lateinit var btnOptionC : Button
    private lateinit var btnOptionD : Button

    private lateinit var quizImage : ImageView
    //navigator
    private lateinit var leftNavigatorButton: Button
    private  lateinit var rightNavigationButton: Button

    private val questions: ArrayList<Question> = ArrayList()
    private lateinit var answerState: Array<String>
    private lateinit var optionState: Array<String>

    private lateinit var questionDetail: TextView

    companion object{
        private  var questionIndex : Int = 0
        private  var isFinish = false
        private lateinit var imageState: Array<Bitmap>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_detail)
        initComponents()
        setButtonOptionListener()
        questionIndex = 0
        isFinish = false
        val quizId: String? = intent.getStringExtra(IntentQuizUtils.QUIZ_ID)
        val modulId: String? = intent.getStringExtra(IntentModulUtils.MODUL_ID)
        loadQuestionData(modulId,quizId)
    }

    private fun processAnswer(): com.example.abceria.model.quiz.QuizResult {
        var point: Long = 0
        var rightAnswer = 0
        for(i in 0 until questions.size){
            if(answerState[i] == questions[i].key){
                point += questions[i].point
                rightAnswer++
            }

        }
        var wrongAnswer = questions.size - rightAnswer
        return com.example.abceria.model.quiz.QuizResult(point,rightAnswer,wrongAnswer)
    }

    @SuppressLint("SetTextI18n")
    private fun quizActionListener(){
        rightNavigationButton.setOnClickListener {
            if(isFinish){
                val answer = answerState
                val questions = questions
                val options = optionState

                val quizResult:com.example.abceria.model.quiz.QuizResult = processAnswer()
                EndQuizDialog(quizResult,this).show(this.supportFragmentManager, EndQuizDialog.TAG)
            }
            resetButtonCheck()
            if((questionIndex+1) <= (questions.size-1)){
                questionIndex++
                quizPosition.text = "Pertanyaan ke ${questionIndex+1}"
                val question = questions[questionIndex]
                quizImage.setImageBitmap(imageState[questionIndex])
                tvQuizPoint.text = "${question.point} point"
                questionDetail.text = question.pertanyaan
                btnOptionA.text = question.optionA
                btnOptionB.text = question.optionB
                btnOptionC.text = question.optionC
                btnOptionD.text = question.optionD

            }
            when(optionState[questionIndex]){
                com.example.abceria.utility.Question.OPTION_A -> checkButtonA()
                com.example.abceria.utility.Question.OPTION_B -> checkButtonB()
                com.example.abceria.utility.Question.OPTION_C -> checkButtonC()
                com.example.abceria.utility.Question.OPTION_D -> checkButtonD()
            }
            //check if last question button text set to finish
            if(questionIndex == (questions.size-1) ){
                rightNavigationButton.text = "Selesai"
                isFinish = true
            }else{
                rightNavigationButton.text = "Selanjutnya"
            }

        }
        leftNavigatorButton.setOnClickListener {
            resetButtonCheck()
            if((questionIndex-1) >= 0){
                questionIndex--
                quizPosition.text = "Pertanyaan ke ${questionIndex+1}"
                val question = questions[questionIndex]
                quizImage.setImageBitmap(imageState[questionIndex])
                tvQuizPoint.text = "${question.point} point"
                questionDetail.text = question.pertanyaan
                btnOptionA.text = question.optionA
                btnOptionB.text = question.optionB
                btnOptionC.text = question.optionC
                btnOptionD.text = question.optionD
            }
            when(optionState[questionIndex]){
                com.example.abceria.utility.Question.OPTION_A -> checkButtonA()
                com.example.abceria.utility.Question.OPTION_B -> checkButtonB()
                com.example.abceria.utility.Question.OPTION_C -> checkButtonC()
                com.example.abceria.utility.Question.OPTION_D -> checkButtonD()
            }
            //check if last question button text set to finish
            if(questionIndex == (questions.size-1) ){
                rightNavigationButton.text = "Selesai"
            }else{
                rightNavigationButton.text = "Selanjutnya"
                isFinish = false
            }
        }
    }

    private fun loadQuestionData(modulId: String?, quizId: String?) {
        if (modulId != null && quizId != null) {
            firestore.collection("modul").document(modulId).collection("quiz").document(quizId).collection("question").get().addOnSuccessListener {
                it.documents.forEach { document ->
                    val question = Question();
                    question.imageUrl = document.get("imageUrl") as String
                    question.key = document.get("key") as String
                    question.optionA = document.get("optionA") as String
                    question.optionB = document.get("optionB") as String
                    question.optionC = document.get("optionC") as String
                    question.optionD = document.get("optionD") as String
                    question.pertanyaan = document.get("pertanyaan") as String
                    question.point = document.get("point") as Long
                    questions.add(question)

                }
            }.continueWith{
                answerState = Array(questions.size) { "" }
                optionState = Array(questions.size) { "" }
                Log.d("answerstateSize", answerState.size.toString())
                Log.d("optionsStateSize",optionState.size.toString())
                imageState = Array(questions.size){Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)}
                for(i in 0 until questions.size){
                    firebasestorage.reference.child(questions[i].imageUrl).getBytes(Long.MAX_VALUE).addOnSuccessListener { it ->
                        imageState[i] = BitmapFactory.decodeByteArray(it,0,it.size)
                    }.continueWith {
                        val question = questions[questionIndex]
                        tvQuizPoint.text = "${question.point} point"
                        quizPosition.text = "Pertanyaan ke ${questionIndex+1}"
                        questionDetail.text = question.pertanyaan
                        btnOptionA.text = question.optionA
                        btnOptionB.text = question.optionB
                        btnOptionC.text = question.optionC
                        btnOptionD.text = question.optionD
                        quizImage.setImageBitmap(imageState[questionIndex])
                    }
                }
                
            }.continueWith{
                quizActionListener()
            }
        }
    }

    private fun initComponents(){
        quizPosition = findViewById(R.id.quiz_detail_tv_question_position)
        tvQuizPoint = findViewById(R.id.quiz_detail_tv_point)
        questionDetail = findViewById(R.id.quiz_detail_tv_question)
        btnOptionA = findViewById(R.id.btn_option_A)
        btnOptionB = findViewById(R.id.btn_option_B)
        btnOptionC = findViewById(R.id.btn_option_C)
        btnOptionD = findViewById(R.id.btn_option_D)
        quizImage = findViewById(R.id.quiz_detail_imv_question)

        //navigator
        rightNavigationButton = findViewById(R.id.quiz_detail_btn_right)
        leftNavigatorButton = findViewById(R.id.quize_detail_btn_left)

        btnOptionA.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionB.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionC.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionD.setBackgroundResource(R.drawable.quiz_detail_option)
    }

    private fun setButtonOptionListener(){
        btnOptionA.setOnClickListener {
           checkButtonA()
        }
        btnOptionB.setOnClickListener {
            checkButtonB()
        }
        btnOptionC.setOnClickListener {
            checkButtonC()
        }
        btnOptionD.setOnClickListener {
            checkButtonD()
        }
    }
    private fun resetButtonCheck(){
        btnOptionA.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionB.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionC.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionD.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionA.setTextColor(Color.parseColor("#000000"))
        btnOptionB.setTextColor(Color.parseColor("#000000"))
        btnOptionC.setTextColor(Color.parseColor("#000000"))
        btnOptionD.setTextColor(Color.parseColor("#000000"))
        btnOptionA.isEnabled = true
        btnOptionB.isEnabled = true
        btnOptionC.isEnabled = true
        btnOptionD.isEnabled = true
    }
    private fun checkButtonA(){
        btnOptionA.setBackgroundResource(R.drawable.quiz_detail_option_checked)
        btnOptionA.setTextColor(Color.parseColor("#FFFFFF"))
        btnOptionA.isEnabled = false
        btnOptionB.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionC.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionD.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionB.setTextColor(Color.parseColor("#000000"))
        btnOptionC.setTextColor(Color.parseColor("#000000"))
        btnOptionD.setTextColor(Color.parseColor("#000000"))
        btnOptionB.isEnabled = true
        btnOptionC.isEnabled = true
        btnOptionD.isEnabled = true
        answerState[questionIndex] = questions[questionIndex].optionA
        optionState[questionIndex] = com.example.abceria.utility.Question.OPTION_A
    }
    private fun checkButtonB(){
        btnOptionB.setBackgroundResource(R.drawable.quiz_detail_option_checked)
        btnOptionB.setTextColor(Color.parseColor("#FFFFFF"))
        btnOptionB.isEnabled = false
        btnOptionA.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionC.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionD.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionA.setTextColor(Color.parseColor("#000000"))
        btnOptionC.setTextColor(Color.parseColor("#000000"))
        btnOptionD.setTextColor(Color.parseColor("#000000"))
        btnOptionA.isEnabled = true
        btnOptionC.isEnabled = true
        btnOptionD.isEnabled = true
        answerState[questionIndex] = questions[questionIndex].optionB
        optionState[questionIndex] = com.example.abceria.utility.Question.OPTION_B
    }
    private fun checkButtonC(){
        btnOptionC.setBackgroundResource(R.drawable.quiz_detail_option_checked)
        btnOptionC.setTextColor(Color.parseColor("#FFFFFF"))
        btnOptionC.isEnabled = false
        btnOptionA.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionB.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionD.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionA.setTextColor(Color.parseColor("#000000"))
        btnOptionB.setTextColor(Color.parseColor("#000000"))
        btnOptionD.setTextColor(Color.parseColor("#000000"))
        btnOptionA.isEnabled = true
        btnOptionB.isEnabled = true
        btnOptionD.isEnabled = true
        answerState[questionIndex] = questions[questionIndex].optionC
        optionState[questionIndex] = com.example.abceria.utility.Question.OPTION_C
    }
    private fun checkButtonD(){
        btnOptionD.setBackgroundResource(R.drawable.quiz_detail_option_checked)
        btnOptionD.setTextColor(Color.parseColor("#FFFFFF"))
        btnOptionD.isEnabled = false
        btnOptionA.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionB.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionC.setBackgroundResource(R.drawable.quiz_detail_option)
        btnOptionA.setTextColor(Color.parseColor("#000000"))
        btnOptionB.setTextColor(Color.parseColor("#000000"))
        btnOptionC.setTextColor(Color.parseColor("#000000"))
        btnOptionA.isEnabled = true
        btnOptionB.isEnabled = true
        btnOptionC.isEnabled = true
        answerState[questionIndex] = questions[questionIndex].optionD
        optionState[questionIndex] = com.example.abceria.utility.Question.OPTION_D
    }

    override fun onStart() {
        super.onStart()

    }

}