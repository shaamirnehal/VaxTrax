package com.example.vaxtrax;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class VaccinePage extends AppCompatActivity implements RVadapter.ItemClickListener {

    RecyclerView rv;
    RVadapter adapter;
    ArrayList<Vaccines> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_page);
        Vaccines vac1 = new Vaccines("Gilead Sciences","DNA","Pre-clinical",
                "Loremipsum asjdasndjasndjasnd");

        data.add(vac1);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RVadapter(data, this);
        adapter.setClickListener(this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
