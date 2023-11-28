package com.example.abceria.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.abceria.Activity.auth.Auth
import com.example.abceria.Activity.settings.Settings
import com.example.abceria.R
import com.example.abceria.adapter.LanguageAdapter
import com.example.abceria.adapter.ListModulAdapter
import com.example.abceria.db.DB
import com.example.abceria.model.modul.Modul
import com.example.abceria.model.user.User
import com.example.abceria.state.StateFactory
import java.util.*
import kotlin.collections.ArrayList

class Home : Fragment() {

    private lateinit var tvUsername: TextView
    private val fireStore = DB.getFirestoreInstance()
    private val currentUser = Auth.getAuthInstance().currentUser
    private val firebaseStorage = DB.getStorageInstance()
    //components
    private lateinit var imvProfile: ImageView

    //userState
    private val userState = StateFactory.getUserStateInstance()

    //recyclerview
    private lateinit var rvListModul: RecyclerView
    private lateinit var listModulAdapter: ListModulAdapter
    private val modulList:ArrayList<Modul> = ArrayList()

    //search
    private lateinit var searchView: SearchView
    private var dataSet = ArrayList<Modul>()
    private lateinit var adapter: LanguageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = view.findViewById(R.id.searchView)
        adapter = LanguageAdapter(dataSet, context)

        tvUsername = view.findViewById(R.id.home_tv_username)
        imvProfile = view.findViewById(R.id.home_imv_profile)
        setProfileUsername()

        imvProfile.setOnClickListener{
            val intent = Intent(this.context, Settings::class.java)
            this.startActivity(intent)
        }
        setProfileImage()
        initRecyclerView(view)
        getModulList()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterList(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
    }

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = java.util.ArrayList<Modul>()
            for (i in dataSet) {
                if (i.title.lowercase().contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
//                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, "No Data found", Toast.LENGTH_SHORT).show()
                rvListModul.adapter = listModulAdapter
            } else {
                adapter.setFilteredList(filteredList)
                rvListModul.adapter = adapter
            }
        }
    }

    private fun getModulList() {
        fireStore.collection("modul").get().addOnSuccessListener {
            it.forEach{ document ->
                val modul = Modul()
                modul.id = document.id
                modul.title = document.get("title").toString()
                modul.description = document.get("descriptionn").toString()
                modulList.add(modul)
                dataSet.add(modul)
            }
            listModulAdapter = ListModulAdapter(modulList,this.context)
            rvListModul.adapter = listModulAdapter
        }


    }

    private fun initRecyclerView(view: View){
        rvListModul = view.findViewById(R.id.list_rv_modul_home)
        rvListModul.layoutManager = GridLayoutManager(this.context,2)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun setProfileUsername(){
        Log.d("profile-username",userState.username)
        if(userState.username == null || userState.username.isEmpty()){
            val userNameRef = fireStore.collection("user").document(currentUser!!.uid);
            userNameRef.addSnapshotListener{ snapshot, e ->
                if (e != null) {
                    //
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(User::class.java)
                    userState.username = user?.username
                    userState.fullName = user?.fullName
                    userState.email = currentUser.email
//                tvUsername.text = userState.username
                    tvUsername.text = user?.username
                } else {
                    //current data null
                }
            }
        }else{
            tvUsername.text = userState?.username
        }
    }

    private fun setProfileImage(){
        if(userState.profilePicture == null){
            fireStore.collection("user").document(currentUser?.uid!!).get().addOnSuccessListener {
                firebaseStorage.reference.child(it.get("profilePicture").toString()).getBytes(Long.MAX_VALUE).addOnSuccessListener { it ->
                    userState.profilePicture = BitmapFactory.decodeByteArray(it,0,it.size)
                    imvProfile.setImageBitmap(userState.profilePicture)
                }
            }
        }else{
            imvProfile.setImageBitmap(userState.profilePicture)
        }
    }

    override fun onStart() {
        super.onStart()
        setProfileImage()
    }
}