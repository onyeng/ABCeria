package com.example.abceria.Activity.ModulMateri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.abceria.R
import com.example.abceria.db.DB
import com.example.abceria.utility.IntentMateriUtils
import com.github.barteksc.pdfviewer.PDFView

class DetailMateri : AppCompatActivity() {

    private lateinit var pdfViewMateri: PDFView
    private lateinit var tvTitle: TextView
    private val firebaseStorage = DB.getStorageInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_materi)
        val pdfUrl = intent.getStringExtra(IntentMateriUtils.PDF_URL)
        val title = intent.getStringExtra(IntentMateriUtils.MATERI_TITLE)
        initComponents()

        tvTitle.text = title
        getMateriFromDatabase(pdfUrl)
    }


    private fun getMateriFromDatabase(pdfUrl: String?) {
        if (pdfUrl != null) {
            firebaseStorage.reference.child(pdfUrl).getBytes(
                Long.MAX_VALUE).addOnSuccessListener {
                pdfViewMateri.fromBytes(it).load()
            }
        }

    }

    private fun initComponents(){
        pdfViewMateri = findViewById(R.id.detail_materi_PDFView_materi)
        tvTitle = findViewById(R.id.detail_materi_tv_title)
    }
}