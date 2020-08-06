package com.example.vaxtrax;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button covidBtn;
    Button vaccineBtn;
    Button statsbtn_main;
    Button quizBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        covidBtn = findViewById(R.id.covidBtn);
        vaccineBtn = findViewById(R.id.vaccineBtn);
        statsbtn_main = findViewById(R.id.statsbtn_main);
        quizBtn = findViewById(R.id.quizBtn);

//      attaching buttons to their xml counterparts
//      setting on click listeners and passing intents for pages

        covidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CovidPage.class);
                startActivity(intent);
            }
        });

        vaccineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VaccinePage.class);
                startActivity(intent);
            }
        });

        statsbtn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatsCovid.class);
                startActivity(intent);
            }
        });

        quizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });
    }
}
