package com.example.tanya.internityhackathon;


import java.lang.annotation.Target;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api
{
    String city = "delhi";
    String BASE_URL = "api.openweathermap.org/data/2.5/weather/";

    @GET
    Call<Data> getdata(@Query("appid") String appid , @Query("q") String query) ;
}
