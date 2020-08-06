package com.example.vaxtrax;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class VaccinesDetail extends AppCompatActivity {
    TextView tv_name;
    TextView tv_type;
    TextView tv_stage;
    TextView tv_info;
    ImageButton ib_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccines_detail);

        ib_help = findViewById(R.id.ib_help);
        ib_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VaccinesDetail.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String type = intent.getStringExtra("type");
        String stage = intent.getStringExtra("stage");
        String info = intent.getStringExtra("info");
        Vaccines vac = new Vaccines(name, type, stage, info);

        tv_name = findViewById(R.id.tv_name);
        tv_type = findViewById(R.id.tv_type);
        tv_stage = findViewById(R.id.tv_stage);
        tv_info = findViewById(R.id.tv_info);


        tv_name.setText(vac.getName());
        tv_type.setText(vac.getType());
        tv_stage.setText(vac.getStage());
        tv_info.setText(vac.getInfo());

    }
}