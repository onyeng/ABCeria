package com.example.abceria.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.abceria.R
import com.example.abceria.db.DB
import com.example.abceria.model.user.User
import com.google.firebase.storage.FirebaseStorage

class LeaderboardAdapter(private val dataSet: java.util.ArrayList<User>):
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val firebaseStorage: FirebaseStorage = DB.getStorageInstance()

        fun bind(user: User){
            val tvFullname : TextView = itemView.findViewById(R.id.leaderboard_tv_fullName)
            val tvUsername : TextView = itemView.findViewById(R.id.leaderboard_tv_userName)
            val tvPoints: TextView = itemView.findViewById(R.id.leaderboard_tv_points)
            val imvProfile: ImageView = itemView.findViewById(R.id.leaderboard_imv_profile)

            tvFullname.text = user.fullName
            tvUsername.text = user.username
            tvPoints.text = user.score?.toString()
            setImageProfile(user,imvProfile)
        }

        private fun setImageProfile(user: User,imvProfile: ImageView){
            val reference = firebaseStorage.reference
            reference.child(user.profilePicture.toString()).getBytes(Long.MAX_VALUE).addOnSuccessListener {
                val profileBitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                imvProfile.setImageBitmap(profileBitmap)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.leaderboard_rv_user,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}