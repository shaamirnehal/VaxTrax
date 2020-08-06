package com.example.vaxtrax;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VaccinePage extends AppCompatActivity implements RVadapter.ItemClickListener {

    RecyclerView rv;
    RVadapter adapter;
    ArrayList<Vaccines> data = new ArrayList<>();
    RequestQueue reqQ;
    //android emulator maps local host ip to 10.0.2.2 and server is hosted locally for this project
    String baseUrl = "http://10.0.2.2:8080";
    String listVac = "/vaccine";
    ArrayList<Vaccines> vList;
    ImageButton help;
    SearchView sv;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_page);

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.layout_loading_dialog);
        alertDialog = builder.create();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        // onclick listener for help page
        help = findViewById(R.id.ib_help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VaccinePage.this, HelpActivity.class);
                startActivity(intent);
            }
        });
        rv = findViewById(R.id.rv);
        vList = new ArrayList<>();
        adapter = new RVadapter(vList, this);
        adapter.setClickListener(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        // search manager for searchview
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        sv = findViewById(R.id.sv_vac);
//        assert searchManager != null;
        sv.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        sv.setMaxWidth(Integer.MAX_VALUE);

        // search view query listener
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        if (isNetworkAvailable()) fetchVac();
        else getSharedPref();


    }


    /**
     * get data from API
     */
    private void fetchVac() {
        alertDialog.show();
        StringRequest req = new StringRequest(Request.Method.GET, baseUrl + listVac,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // save response to sharedPref
                        editor.putString("VacsData", response);
                        editor.commit();

                        populateRV(response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG", "onErrorResponse: " + error.getMessage());
                alertDialog.dismiss();
            }
        });
        reqQ.add(req);
    }

    /**
     * populates recyclerview cards with vaccines
     * @param response list of vaccines
     */
    private void populateRV(String response) {
        try {
            JSONArray vaccinesArray = new JSONArray(response);
            for (int i = 0; i < vaccinesArray.length(); i++) {
                JSONObject vaccines = vaccinesArray.getJSONObject(i);
                String name = vaccines.getString("name");
                String type = vaccines.getString("type");
                String stage = vaccines.getString("stage");
                String info = vaccines.getString("info");
                Vaccines vac = new Vaccines(name, type, stage, info);
                vList.add(vac);
            }
            rv.setAdapter(adapter);
        } catch (JSONException e) {
            Log.i("TAG", "onResponse: " + e.getMessage());
        }
        if (alertDialog.isShowing()) alertDialog.dismiss();
    }

    @Override
    public void onItemClick(View view, int position) {
        // pass extras to vaccine details page to populate based on adapter position
        Intent intent = new Intent(this, VaccinesDetail.class);
        intent.putExtra("name", vList.get(position).getName());
        intent.putExtra("type", vList.get(position).getType());
        intent.putExtra("stage", vList.get(position).getStage());
        intent.putExtra("info", vList.get(position).getInfo());
        startActivity(intent);
    }

    /**
     * get data from shared pref and toasts to display
     */
    private void getSharedPref() {
        Toast.makeText(VaccinePage.this, "Internet connectivity not found, showing last saved results", Toast.LENGTH_LONG).show();
        String list = pref.getString("VacsData", null);
        if (list == null) {
            Toast.makeText(getApplicationContext(), "Internet connectivity not found", Toast.LENGTH_LONG).show();
        } else {
            populateRV(list);
        }
    }


    /**
     * checks for internet connection
     * @return true if internet available; false otherwise
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
