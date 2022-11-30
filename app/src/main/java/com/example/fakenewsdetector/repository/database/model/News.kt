package com.example.fakenewsdetector.repository.database.model

data class News(var newsText : String) {
    lateinit var newsType : String
}