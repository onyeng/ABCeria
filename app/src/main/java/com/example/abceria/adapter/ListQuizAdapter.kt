package com.example.abceria.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.abceria.Activity.quiz.QuizDetail
import com.example.abceria.R
import com.example.abceria.model.quiz.Quiz
import com.example.abceria.utility.IntentModulUtils
import com.example.abceria.utility.IntentQuizUtils

class ListQuizAdapter(private val dataset: ArrayList<Quiz>,val context: Context): RecyclerView.Adapter<ListQuizAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(quiz: Quiz){
            val quizImage: ImageView = itemView.findViewById(R.id.list_rv_quiz_imv)
            val quizTitle: TextView = itemView.findViewById(R.id.list_rv_quiz_tv_title)
            val quizDesc: TextView = itemView.findViewById(R.id.list_rv_quiz_tv_description)

            val quizCard: CardView = itemView.findViewById(R.id.list_rv_quiz_cv_quiz)

            quizTitle.text = quiz.title
            quizDesc.text = quiz.description

            quizCard.setOnClickListener{
                val intent = Intent(context,QuizDetail::class.java)
                //this for put extra
                intent.putExtra(IntentQuizUtils.QUIZ_ID,quiz.id)
                intent.putExtra(IntentModulUtils.MODUL_ID,quiz.modulId)
                context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_rv_quiz,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}