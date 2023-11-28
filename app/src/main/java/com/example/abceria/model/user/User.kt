package com.example.abceria.model.user

class User(
    var fullName: String?,
    var username: String?,
    var profilePicture:String? = "",
    var score: Int? = 0,
){
    constructor() : this(null,null,null,null) {

    }


}
