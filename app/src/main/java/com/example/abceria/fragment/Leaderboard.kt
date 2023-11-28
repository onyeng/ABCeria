package com.example.abceria.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.abceria.R
import com.example.abceria.adapter.LeaderboardAdapter
import com.example.abceria.db.DB
import com.example.abceria.model.user.User
import com.google.firebase.firestore.Query

class Leaderboard : Fragment() {

    private val firestore = DB.getFirestoreInstance()
    private val firebaseStorage = DB.getStorageInstance()
    private lateinit var rvLeaderboard: RecyclerView
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private val userList:ArrayList<User> = ArrayList()

    //1st
    private lateinit var top1Imv : ImageView
    private lateinit var top1Username: TextView
    private lateinit var top1Score: TextView
    //2nd
    private lateinit var top2Imv : ImageView
    private lateinit var top2Username: TextView
    private lateinit var top2Score: TextView
    //3rd
    private lateinit var top3Imv : ImageView
    private lateinit var top3Username: TextView
    private lateinit var top3Score: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun initLeadboardPodium(view: View){
        top1Imv = view.findViewById(R.id.leaderboard_imv_top1)
        top1Username = view.findViewById(R.id.leaderboard_tv_top1_username)
        top1Score = view.findViewById(R.id.leaderboard_tv_top1_points)

        top2Imv = view.findViewById(R.id.leaderboard_imv_top2)
        top2Username = view.findViewById(R.id.leaderboard_tv_top2_username)
        top2Score = view.findViewById(R.id.leaderboard_tv_top2_points)

        top3Imv = view.findViewById(R.id.leaderboard_imv_top3)
        top3Username = view.findViewById(R.id.leaderboard_tv_top3_username)
        top3Score = view.findViewById(R.id.leaderboard_tv_top3_points)


    }
    private fun initPodiumData(){
        top1Username.text = userList[0].username
        top1Score.text = userList[0].score.toString()
        firebaseStorage.reference.child(userList[0].profilePicture!!).getBytes(Long.MAX_VALUE).addOnSuccessListener {
            val profileBitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            top1Imv.setImageBitmap(profileBitmap)
        }
        top2Username.text = userList[1].username
        top2Score.text = userList[1].score.toString()
        firebaseStorage.reference.child(userList[1].profilePicture!!).getBytes(Long.MAX_VALUE).addOnSuccessListener {
            val profileBitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            top2Imv.setImageBitmap(profileBitmap)
        }
        top3Username.text = userList[2].username
        top3Score.text = userList[2].score.toString()
        firebaseStorage.reference.child(userList[2].profilePicture!!).getBytes(Long.MAX_VALUE).addOnSuccessListener {
            val profileBitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            top3Imv.setImageBitmap(profileBitmap)
        }
    }

    private fun getUserList() {
        firestore.collection("user").orderBy("score", Query.Direction.DESCENDING) .get().addOnSuccessListener {
            it.forEach{ document ->
                val user = User()
                user.username = document.get("username").toString()
                user.fullName = document.get("fullName").toString()
                user.profilePicture = document.get("profilePicture").toString()
                user.score = Integer.parseInt(document.get("score").toString())
                userList.add(user)
            }
            leaderboardAdapter = LeaderboardAdapter(userList)
            rvLeaderboard.adapter = leaderboardAdapter
        }.continueWith {
            initPodiumData()
        }
    }

    private fun initRecyclerView(view: View){
        rvLeaderboard = view.findViewById(R.id.leaderboard_rv_leaderboard)
        rvLeaderboard.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        initLeadboardPodium(view)
        getUserList()
        Log.d("LeaderboardFragment","onViewCreatedStart")
    }


}