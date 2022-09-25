package com.example.foodhub.database

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun generateDate(): String {
    val date = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return date.format(formatter).toString()
}