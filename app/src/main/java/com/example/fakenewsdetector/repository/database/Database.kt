package com.example.fakenewsdetector.repository.database

import androidx.lifecycle.MutableLiveData
import com.example.fakenewsdetector.repository.database.model.News
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Database {
    private val database = Firebase.database
    private val reference = database.getReference("data")
    val newsType = MutableLiveData<Boolean>()

    fun addNews(news: News) {
        reference.child(news.newsText).setValue(news.newsType)
    }

    fun checkIfNewsInDatabase(newsText: String) {
        CoroutineScope(Dispatchers.Default).launch {
            reference.get().addOnSuccessListener { allNews ->
                if (allNews.value != "") {
                    val newsHashMap = allNews.value as HashMap<*, *>
                    for (key in newsHashMap.keys) {
                        if (key == newsText) {
                            if (newsHashMap[key].toString() == "true")
                                newsType.postValue(true)
                            else
                                newsType.postValue(false)
                        }
                    }
                }
            }
        }
    }
}