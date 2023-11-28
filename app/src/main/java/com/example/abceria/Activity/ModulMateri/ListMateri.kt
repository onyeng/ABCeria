package com.example.abceria.Activity.ModulMateri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.abceria.R
import com.example.abceria.adapter.ListMateriAdapter
import com.example.abceria.db.DB
import com.example.abceria.model.materi.Materi
import com.example.abceria.utility.IntentModulUtils

class ListMateri : AppCompatActivity() {

    private val firestore = DB.getFirestoreInstance()
    private lateinit var rvListMateri: RecyclerView
    private lateinit var listMateriAdapter: ListMateriAdapter
    private val materiList:ArrayList<Materi> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_materi)
        val modulId: String? = intent.getStringExtra(IntentModulUtils.MODUL_ID)
        initComponents()
        if (modulId != null) {
            getMateriList(modulId)
        }
    }

    private fun getMateriList(modulId: String){
        if (modulId != null) {
            firestore.collection("modul").document(modulId).collection("materi").get().addOnSuccessListener {
                it.documents.forEach { it ->
                    val materi = Materi()
                    materi.id = it.id
                    materi.title = it.get("title") as String
                    materi.pdfUrl = it.get("pdfUrl") as String
                    if (materi != null) {
                        materiList.add(materi)
                    }
                }
                listMateriAdapter = ListMateriAdapter(materiList,this)
                rvListMateri.adapter = listMateriAdapter
            }
        }
    }

    private fun initComponents(){
        rvListMateri = findViewById(R.id.list_rv_materi)
        rvListMateri.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
    }
}