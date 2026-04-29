package com.codewithhiren.ekart.model

import java.io.Serializable

data class User(
    val firstName : String,
    val lastName : String,
    val email : String,
    val imagePath : String
) : Serializable{
    constructor() : this("","","","")
}

