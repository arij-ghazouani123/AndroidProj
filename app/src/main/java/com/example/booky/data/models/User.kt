package com.example.booky.data.models

data class User(
    val id:  String,
    val firstName:  String,
    val lastName:  String,
    val email:  String,
    val password:  String,
    val verified: Boolean?,
    val activationCode: String) : java.io.Serializable{

    constructor(email: String,password:String)
            : this("", "", "", email, password, null, "")
    constructor(firstName: String,lastName: String,email:String, password: String,verified: Boolean, activationCode: String)
            : this("",firstName,lastName,email,password,verified,activationCode)
    constructor(firstName: String,lastName: String,email:String, password: String)
            : this("",firstName,lastName,email,password,null,"")
    constructor(activationCode:String)
            : this("","","","","",null, activationCode)
    constructor(email:String,verified: Boolean?)
            : this("","","",email,"",null, "")
    constructor(password:String, verified: Boolean?,activationCode: String?)
            : this("","","","",password,null, "")




}