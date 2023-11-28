package com.example.abceria.db

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import io.grpc.Context.Storage

object DB {
    fun getRealtimeDatabaseInstance(): FirebaseDatabase {
        return Firebase.database
    }

    fun getFirestoreInstance(): FirebaseFirestore {
        return Firebase.firestore
    }

    fun getStorageInstance(): FirebaseStorage {
        return Firebase.storage
    }

}