package com.example.vaxtrax;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

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
    String baseUrl = "http://10.0.2.2:8080"; //android emulator maps local host ip to 10.0.2.2
    String listVac = "/vaccine";
    ArrayList<Vaccines> vList;
    ImageButton help;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_page);
        help = findViewById(R.id.ib_help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VaccinePage.this, HelpActivity.class);
                startActivity(intent);
            }
        });
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        vList = new ArrayList<>();
        adapter = new RVadapter(vList, getApplicationContext());

        reqQ = Volley.newRequestQueue(this);
        StringRequest req = new StringRequest(Request.Method.GET, baseUrl + listVac,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray vaccinesArray = new JSONArray(response);
                            for (int i = 0; i < vaccinesArray.length(); i++)    {
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
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG", "onErrorResponse: " + error.getMessage());
            }
        });
        reqQ.add(req);
        adapter.setClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, VaccinesDetail.class);
        intent.putExtra("name", vList.get(position).getName());
        intent.putExtra("type", vList.get(position).getType());
        intent.putExtra("stage", vList.get(position).getStage());
        intent.putExtra("info", vList.get(position).getInfo());
        startActivity(intent);
    }
}
