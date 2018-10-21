package com.example.roopanc.wiki;

import com.example.roopanc.wiki.data.JsonData;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface RetrofitAPI {

    String BASE_URL = "https://en.wikipedia.org/";

    @GET("/w/api.php")
    Call<JsonData> getSearchSuggestions(@QueryMap Map<String, String> options);
}
