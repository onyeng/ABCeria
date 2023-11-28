
package com.example.abceria.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.abceria.Activity.auth.Auth
import com.example.abceria.MainActivity
import com.example.abceria.R
import com.example.abceria.db.DB
import com.example.abceria.model.user.User
import com.example.abceria.state.StateFactory
import com.google.firebase.auth.FirebaseUser
import java.util.*


class ProfileFrag : Fragment() {
    private val firestore = DB.getFirestoreInstance()
    private val auth = Auth.getAuthInstance()
    private var currentUser: FirebaseUser? = null
    private val firebaseStorage = DB.getStorageInstance()
    //components
    private lateinit var etFullName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var imvProfile: ImageView
    private lateinit var btnSave: Button

    //uri
    private var profileImageUri: Uri? = null

    //userState
    private val userState = StateFactory.getUserStateInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.s, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents(view)

        val startForImageEdit = registerForActivityResult(ActivityResultContracts.GetContent()){ contentUri ->
            profileImageUri = contentUri
            userState.profilePicture = null
            imvProfile.setImageURI(profileImageUri)
        }
        imvProfile.setOnClickListener{
            startForImageEdit.launch("image/*")
        }
        btnSave.setOnClickListener {
            saveDataToUpdate()
            saveProfileImage()
            userState.profilePicture = null
        }
        getProfileData()
        setProfileImage()
    }
    private fun saveProfileImage() {
        val storageRef = firebaseStorage.reference
        val fileName: String = UUID.randomUUID().toString()

        val profileRef =  storageRef.child("/profile-image/${fileName}")

        profileImageUri?.let { profileRef.putFile(it) }

        val user = User()
        user.profilePicture =  "/profile-image/${fileName}"
        user.score = 18
        firestore.collection("user").document(currentUser?.uid!!).update("profilePicture",user.profilePicture).addOnSuccessListener {
            userState.profileImageUri = "/profile-image/${fileName}"
        }.addOnFailureListener {
            Toast.makeText(this.context,"gagal update gambar", Toast.LENGTH_SHORT).show()
        }
        val userState = StateFactory.getUserStateInstance()
        firestore.collection("user").document(currentUser?.uid!!).get().addOnSuccessListener {
            val profileImageUrl = it.data?.get("profilePicture") as String
            storageRef.child(profileImageUrl).getBytes(Long.MAX_VALUE).addOnSuccessListener { it ->
                userState.profilePicture = BitmapFactory.decodeByteArray(it,0,it.size)
            }
        }
    }


    private fun getProfileData(){
        if(auth.currentUser != null){
            val userUid = auth.currentUser!!.uid
            firestore.collection("user").document(userUid).get().addOnCompleteListener{ Task ->
                Task.addOnSuccessListener { docSnapShot ->
                    val user = docSnapShot.toObject(User::class.java)
                    etFullName.setText(user?.fullName)
                    etUsername.setText(user?.username)
                }
            }
        }

    }

    private fun setProfileImage(){
        if(userState.profilePicture != null){
            imvProfile.setImageBitmap(userState.profilePicture)
        }

    }

    private fun saveDataToUpdate(){

        if(currentUser !== null){
            val userUid = currentUser!!.uid

            val usernameToUpdate = etUsername.text.toString()
            val fullNameToUpdate = etFullName.text.toString()
            val user = User()
            user.username = usernameToUpdate
            user.fullName = fullNameToUpdate
            user.score = 0//perlu diubahhhhh

            firestore.collection("user").document(userUid).get().addOnSuccessListener {
                val userData = it.data
                if(userData != null){
                    firestore.collection("user").document(userUid).update("username",user.username,"fullName",user.fullName).addOnSuccessListener {
                        Toast.makeText(this.context,"Data profil berhasil di update", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this.context,"Data gagal di update", Toast.LENGTH_SHORT).show()
                    }
                    startActivity(Intent(this.context, MainActivity::class.java))
                }else {
                    firestore.collection("user").document(userUid).set(user).addOnSuccessListener {
                        Toast.makeText(this.context,"data profile baru ditambahakan", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener{
                        Toast.makeText(this.context,"data gagal ditambahakan", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }


    }


    private fun initComponents(view: View) {
        etFullName = view.findViewById(R.id.edit_profile_et_fullName)
        etUsername = view.findViewById(R.id.edit_profile_et_username)
        etPassword = view.findViewById(R.id.edit_profile_et_username)
        imvProfile = view.findViewById(R.id.edit_profile_imv_profileImage)
        btnSave = view.findViewById(R.id.edit_profile_btn_save)
    }

}
