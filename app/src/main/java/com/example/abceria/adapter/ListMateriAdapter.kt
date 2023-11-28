package com.example.abceria.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.abceria.Activity.ModulMateri.DetailMateri
import com.example.abceria.R
import com.example.abceria.model.materi.Materi
import com.example.abceria.utility.IntentMateriUtils
import java.util.ArrayList

class ListMateriAdapter(private val dataSet: ArrayList<Materi>, private val listMateriContext: Context?):
    RecyclerView.Adapter<ListMateriAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(materi: Materi){
            val tvNamaMateri : TextView = itemView.findViewById(R.id.list_tv_materi)
            val tvDscMateri : TextView = itemView.findViewById(R.id.list_tv_dscmateri)
            val clMateri: ConstraintLayout = itemView.findViewById(R.id.detail_materi_cl_materi)

            tvNamaMateri.text = materi.title
            tvDscMateri.text = materi.description

            clMateri.setOnClickListener{
                val intent = Intent(listMateriContext,DetailMateri::class.java)
                intent.putExtra(IntentMateriUtils.PDF_URL,materi.pdfUrl)
                intent.putExtra(IntentMateriUtils.MATERI_TITLE,materi.title)
                listMateriContext?.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_rv_materi,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}