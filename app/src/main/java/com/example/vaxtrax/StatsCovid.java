package com.example.vaxtrax;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StatsCovid extends AppCompatActivity {

    TextView tvTC;
    TextView tvNC;
    TextView tvND;
    TextView tvTD;
    TextView tvNR;
    TextView tvTR;
    RecyclerView rv;
    CountriesAdapter adapter;

    RequestQueue reqQ;
    String url = "https://api.covid19api.com/summary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_covid);

        tvTC = findViewById(R.id.tv_tc);
        tvNC = findViewById(R.id.tv_nc);
        tvND = findViewById(R.id.tv_nd);
        tvTD = findViewById(R.id.tv_td);
        tvNR = findViewById(R.id.tv_nr);
        tvTR = findViewById(R.id.tv_tr);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        reqQ = Volley.newRequestQueue(this);
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("TAG", "onResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject gStatObj = object.getJSONObject("Global");
                    int newConfirmed = gStatObj.getInt("NewConfirmed");
                    int totalConfirmed = gStatObj.getInt("TotalConfirmed");
                    int newDeaths = gStatObj.getInt("NewDeaths");
                    int totalDeaths = gStatObj.getInt("TotalDeaths");
                    int newRecovered = gStatObj.getInt("NewRecovered");
                    int totalRecovered = gStatObj.getInt("TotalRecovered");

                    JSONArray countryArr = object.getJSONArray("Countries");
                    ArrayList<CountriesModel> cList = new ArrayList<>();
                    for (int i = 0; i < countryArr.length(); i++) {
                        JSONObject country = countryArr.getJSONObject(i);
                        String name = country.getString("Country");
                        int nc = country.getInt("NewConfirmed");
                        int tc = country.getInt("TotalConfirmed");
                        int nd = country.getInt("NewDeaths");
                        int td = country.getInt("TotalDeaths");
                        int nr = country.getInt("NewRecovered");
                        int tr = country.getInt("TotalRecovered");
                        StatsModel countryStats = new StatsModel(nc, tc, nd, td, nr, tr);
                        CountriesModel c = new CountriesModel(name, countryStats);
                        cList.add(c);
                    }
                    StatsModel globalStats = new StatsModel(newConfirmed, totalConfirmed,
                            newDeaths, totalDeaths, newRecovered, totalRecovered);
                    tvNC.setText(String.valueOf(globalStats.getNewConfirmed()));
                    tvTC.setText(String.valueOf(globalStats.getTotalConfirmed()));
                    tvND.setText(String.valueOf(globalStats.getNewDeaths()));
                    tvTD.setText(String.valueOf(globalStats.getTotalDeaths()));
                    tvNR.setText(String.valueOf(globalStats.getNewRecovered()));
                    tvTR.setText(String.valueOf(globalStats.getTotalRecovered()));

                    adapter = new CountriesAdapter(cList, getApplicationContext());
                    rv.setAdapter(adapter);

                } catch (JSONException e) {
                    Log.i("TAG", "onResponse: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG", "onErrorResponse: " + error.getMessage());
            }
        });
        reqQ.add(req);

    }
}