package com.example.tommyscloset;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ClothesuploadSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothesupload_select);

        Button buttonItem = findViewById(R.id.buttonItem);
        Button buttonOutfit = findViewById(R.id.buttonOutfit);
        Button buttonDone = findViewById(R.id.buttonDone);

        buttonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Clothesupload.class));
                finish();
            }
        });
        // change to upload outfit
        buttonOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change to go to
                startActivity(new Intent(getApplicationContext(), Createoutfit.class));
                finish();
            }
        });
        // go back to main menu
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change to go to
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });


    }
}

