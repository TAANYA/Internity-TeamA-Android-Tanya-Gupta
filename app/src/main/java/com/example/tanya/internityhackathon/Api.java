package com.example.tanya.internityhackathon;


import java.lang.annotation.Target;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api
{
    String BASE_URL = "http://api.openweathermap.org/";

    @GET("data/2.5/weather")
    Call<Data> getData(@Query("appid") String appid , @Query("q") String query);

    @GET("data/2.5/weather")
    Call<Data> getData(@Query("appid") String appid , @Query("lat") String lat,@Query("lon") String lon);


}
