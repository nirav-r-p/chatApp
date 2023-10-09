package com.example.chatapp.validation

import java.util.regex.Pattern

class Validation {
    fun isValidatePassword(input:String):Boolean{
        if(input.isBlank()) return false
        if(input.length < 8) return false
        return true
    }
//    fun isValidateEmail(input: String):Boolean{
//        val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
//                "[a-zA-Z0-9_+&*-]+)*@" +
//                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
//                "A-Z]{2,7}$"
//
//        val pat: Pattern = Pattern.compile(emailRegex)
//        if (input.isBlank())
//            return false;
//        return pat.matcher(input).matches();
//    }
}