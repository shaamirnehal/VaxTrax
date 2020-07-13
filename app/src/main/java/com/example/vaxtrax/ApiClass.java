package com.example.vaxtrax;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClass {
    private static Retrofit retrofit;
    private static final String baseUrl = "https://api.covid19api.com/";

    public static Retrofit getRetrofit() {
        if (retrofit == null)   {
            retrofit = new retrofit2.Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
