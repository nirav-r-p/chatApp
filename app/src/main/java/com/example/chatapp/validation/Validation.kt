package com.example.chatapp.validation

import android.annotation.SuppressLint
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
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
            return false
        return pat.matcher(input).matches()
    }
}
fun getHhMM(inputDateString:String):String{
    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputDateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val date = inputDateFormat.parse(inputDateString)
    return outputDateFormat.format(date!!)
}
fun formatChatTimestamp(timestamp: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    try {
        val inputDate = inputFormat.parse(timestamp)

        val calendar = Calendar.getInstance()
        calendar.time = inputDate

        val today = Calendar.getInstance()
        today.time = Date()

        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_YEAR, -1)

        if (
            calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
        ) {
            // Today
            val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            return timeFormat.format(inputDate)
        } else if (
            calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
            calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)
        ) {
            // Yesterday

            return "Yesterday "
        } else {
            // Other days
            return outputFormat.format(inputDate)
        }
    } catch (e: ParseException) {
        // Handle parsing exception if the input timestamp is not in the expected format
        return "Invalid Timestamp"
    }
}



