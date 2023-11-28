package com.example.abceria.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.abceria.R
import com.example.abceria.adapter.LeaderboardAdapter
import com.example.abceria.adapter.ListMateriAdapter
import com.example.abceria.adapter.ListModulAdapter
import com.example.abceria.db.DB
import com.example.abceria.model.materi.Materi
import com.example.abceria.model.modul.Modul
import com.example.abceria.model.user.User


class ListModul : Fragment() {

    private val firestore = DB.getFirestoreInstance()
    private lateinit var rvListModul: RecyclerView
    private lateinit var listModulAdapter: ListModulAdapter
    private val modulList:ArrayList<Modul> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private fun getModulList() {
        firestore.collection("modul").get().addOnSuccessListener {
            it.forEach{ document ->
                val modul = Modul()
                modul.id = document.id
                modul.title = document.get("title").toString()
                modul.description = document.get("descriptionn").toString()
                modulList.add(modul)
            }
            listModulAdapter = ListModulAdapter(modulList,this.context)
            rvListModul.adapter = listModulAdapter
        }


    }

    private fun initRecyclerView(view: View){
        rvListModul = view.findViewById(R.id.list_rv_modul)
        rvListModul.layoutManager = GridLayoutManager(this.context,2)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_modul, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        getModulList()

    }


}