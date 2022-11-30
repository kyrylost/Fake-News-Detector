package com.example.fakenewsdetector.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fakenewsdetector.repository.api.Api
import com.example.fakenewsdetector.repository.database.Database
import com.example.fakenewsdetector.repository.database.model.News
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Response

class AppViewModel: ViewModel() {
    private val api = Api()
    private val database = Database()
    private val apiResponseMutableLiveData: MutableLiveData<Response>? = api.responseMutableLiveData
    private val newsTypeMutableLiveData: MutableLiveData<Boolean> = database.newsType

    val fakeNewsDetectedMutableLiveData = MutableLiveData<Boolean>()
    val trueNewsDetectedMutableLiveData = MutableLiveData<Boolean>()
    lateinit var news: News

    init {
        apiResponseMutableLiveData?.observeForever { response ->
            response.body()!!.string().apply {
                contains("true").apply {
                    when(this) {
                        true -> {
                            trueNewsDetectedMutableLiveData.value = true
                            news.newsType = "true"
                            database.addNews(news)
                            Log.d("News was checked by API","Type: True")
                        }
                        false -> {
                            fakeNewsDetectedMutableLiveData.value = true
                            news.newsType = "false"
                            database.addNews(news)
                            Log.d("News was checked by API","Type: Fake")
                        }
                    }
                }
            }
        }

        newsTypeMutableLiveData.observeForever { newsType ->
            if (newsType) {
                trueNewsDetectedMutableLiveData.value = true
                Log.d("News founded in database","Type: True")
            }
            else {
                fakeNewsDetectedMutableLiveData.value = true
                Log.d("News founded in database","Type: Fake")
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun check(newsText: String) {

        database.checkIfNewsInDatabase(newsText)

        GlobalScope.launch {
            kotlin.runCatching {
                api.checkNews(newsText)
            }

            news = News(newsText)
        }
    }
}
























