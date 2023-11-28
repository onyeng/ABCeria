package com.example.abceria.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.abceria.Activity.ModulMateri.modul.ModulDetail
import com.example.abceria.R
import com.example.abceria.model.modul.Modul
import com.example.abceria.utility.IntentModulUtils
import java.util.ArrayList

class LanguageAdapter(var dataSet: List<Modul>, val context: Context?) :
    RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {
    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaModul: TextView = itemView.findViewById(R.id.list_modul_tv_modul)
        val cvModul: CardView = itemView.findViewById(R.id.list_modul_cv_modul)
    }

    fun setFilteredList(dataSet: ArrayList<Modul>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_rv_modul, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.tvNamaModul.text = dataSet[position].title
        holder.cvModul.setOnClickListener {
            val intent = Intent(context, ModulDetail::class.java)
            intent.putExtra(IntentModulUtils.MODUL_ID, dataSet[position].id)
            intent.putExtra(IntentModulUtils.MODUL_TITLE, dataSet[position].title)
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}