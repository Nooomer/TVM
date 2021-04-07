package com.tvmedicine

class API {
    fun getUsers():String{
        return "https://1515714571567515.000webhostapp.com/getUsers.php"
    }
    fun doctorAuth(number: String, password:String):String{
        return "https://1515714571567515.000webhostapp.com/doctorAuth.php?number=$number&password=$password"
    }
}