package com.example.vaxtrax;

import androidx.appcompat.app.AppCompatActivity;
import com.example.vaxtrax.CovidModel;
import com.example.vaxtrax.Api;
import com.example.vaxtrax.ApiClass;
import com.example.vaxtrax.R;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatsCovid extends AppCompatActivity {

    TextView one;
    TextView two;
    TextView three;
    TextView four;
    TextView five;
    TextView six;
    TextView seven;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_covid);

        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);

        Api api = ApiClass.getRetrofit().create(Api.class);

        Call<CovidModel> covidModel =api.getAllData();

        covidModel.enqueue(new Callback<CovidModel>() {
            @Override
            public void onResponse(Call<CovidModel> call, Response<CovidModel> response) {
                one.setText(response.body().getCountries());
                two.setText(response.body().getNewConfirmed());
                three.setText(response.body().getTotalConfirmed());
                four.setText(response.body().getNewDeaths());
                five.setText(response.body().getTotalDeaths());
                six.setText(response.body().getNewRecovered());
                seven.setText(response.body().getTotalRecovered());
            }

            @Override
            public void onFailure(Call<CovidModel> call, Throwable t) {
                Toast.makeText(StatsCovid.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
}}