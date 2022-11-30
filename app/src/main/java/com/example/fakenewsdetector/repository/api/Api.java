package com.example.fakenewsdetector.repository.api;

import androidx.lifecycle.MutableLiveData;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {
    public MutableLiveData<Response> responseMutableLiveData = new MutableLiveData<>();

    public void checkNews(String newsText) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String s = "{\"inputs\": " + "\"" + newsText + "\"}=";
        RequestBody body = RequestBody.create(mediaType, s);
        Request request = new Request.Builder()
                .url("https://api-inference.huggingface.co/models/cometrain/fake-news-detector-t5")
                .method("POST", body)
                .addHeader("Authorization", "Bearer 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")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cookie", "AWSALB=UiNNrF8m4bmLXg+/NJuez2BoiQRPYbc5CAs92jE2DB6LNJOsqUk2BV4xPfDgX4hX4A87alqUDmbKno/J3E0lJyiB1hVPExmkzM2ySjvFzKXXDKzl3CpGlKAsos8V; AWSALBCORS=UiNNrF8m4bmLXg+/NJuez2BoiQRPYbc5CAs92jE2DB6LNJOsqUk2BV4xPfDgX4hX4A87alqUDmbKno/J3E0lJyiB1hVPExmkzM2ySjvFzKXXDKzl3CpGlKAsos8V")
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                responseMutableLiveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}














