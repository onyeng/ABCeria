package com.example.abceria.Activity.quiz.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.abceria.Activity.ModulMateri.QuizResult
import com.example.abceria.Activity.quiz.QuizDetail

class EndQuizDialog(private val quizResult: com.example.abceria.model.quiz.QuizResult,private val activity: Activity) : DialogFragment() {

    companion object {
        const val TAG = "PurchaseEndQuizDialog"
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Kamu yakin untuk mengakhiri kuis?")
                .setPositiveButton("YA"
                ) { dialog, id ->
                    //Start
                    val intent = Intent(this.context, QuizResult::class.java)
                    intent.putExtra("answer",quizResult)
                    this.startActivity(intent)
                    activity.finish()
                }
                .setNegativeButton("TIDAK"
                ) { dialog, which ->
                    //cancel
                    dialog.dismiss()
                }
                .create()
        }?: throw IllegalStateException("Activity shouldnt be null")
    }
}