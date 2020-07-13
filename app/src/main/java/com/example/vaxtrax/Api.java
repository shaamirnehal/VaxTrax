package com.example.vaxtrax;

import com.example.vaxtrax.CovidModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Api {
    @GET("summary")
    Call<CovidModel> getAllData();
}
