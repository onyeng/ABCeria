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

class ListModulAdapter(private val dataSet: java.util.ArrayList<Modul>,val context: Context?):
    RecyclerView.Adapter<ListModulAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(modul: Modul){
            val tvNamaModul : TextView = itemView.findViewById(R.id.list_modul_tv_modul)
            val cvModul : CardView = itemView.findViewById(R.id.list_modul_cv_modul)
            //bakal ditambahin desc modul kalau fragmen detail modul nya udah ada
            tvNamaModul.text = modul.title
            cvModul.setOnClickListener{
               val intent = Intent(context, ModulDetail::class.java)
                intent.putExtra(IntentModulUtils.MODUL_ID,modul.id)
                intent.putExtra(IntentModulUtils.MODUL_TITLE,modul.title)
                context?.startActivity(intent)
            }
            //bakal ditambahin desc modul kalau fragmen detail modul nya udah ada

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_rv_modul,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}