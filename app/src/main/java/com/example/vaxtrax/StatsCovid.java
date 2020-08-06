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

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
    SearchView sv_stats;
    RecyclerView rv;
    CountriesAdapter adapter;
    ArrayList<CountriesModel> cList;
    RequestQueue reqQ;
    String url = "https://api.covid19api.com/summary";
    // create pref variables
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_covid);

        // instantiate pref vars
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        tvTC = findViewById(R.id.tv_tc);
        tvNC = findViewById(R.id.tv_nc);
        tvND = findViewById(R.id.tv_nd);
        tvTD = findViewById(R.id.tv_td);
        tvNR = findViewById(R.id.tv_nr);
        tvTR = findViewById(R.id.tv_tr);
        rv = findViewById(R.id.rv);
        cList = new ArrayList<>();
        adapter = new CountriesAdapter(cList, this);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setAdapter(adapter);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        sv_stats = findViewById(R.id.sv_stats);
//        assert searchManager != null;
        sv_stats.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        sv_stats.setMaxWidth(Integer.MAX_VALUE);

        sv_stats.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        reqQ = Volley.newRequestQueue(this);
        if (isNetworkAvailable()) fetchStats();
        else getSharedPref();
    }

    public void fetchStats() {
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("TAG", "onResponse: " + response);
                // save response to sharedPref
                editor.putString("StatsData", response);
                editor.commit();

                populateRV(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG", "onErrorResponse: " + error.getMessage());
            }
        });
        reqQ.add(req);
    }

    // get data from shared pref
    private void getSharedPref() {
        String list = pref.getString("StatsData", null);
        if (list == null) {
            Toast.makeText(getApplicationContext(), "Internet connectivity not found", Toast.LENGTH_LONG).show();
        } else {
            populateRV(list);
        }
    }

    private void populateRV(String response) {
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
            cList.clear();

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

            adapter.notifyDataSetChanged();
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}