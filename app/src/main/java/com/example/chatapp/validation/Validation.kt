package com.example.chatapp.validation

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.regex.Pattern

class Validation {
    fun isValidatePassword(input:String):Boolean{
        if(input.isBlank()) return false
        if(input.length < 8) return false
        return true
    }
    fun isValidateEmail(input: String):Boolean{
        val emailRegex =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"

        val pat: Pattern = Pattern.compile(emailRegex)
        if (input.isBlank())
            return false;
        return pat.matcher(input).matches();
    }
}
fun getHhMM(inputDateString:String):String{
    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputDateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val date = inputDateFormat.parse(inputDateString)
    return outputDateFormat.format(date!!)
}



