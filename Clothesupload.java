package com.example.tommyscloset;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Clothesupload extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothesupload);


        Button buttonTop = findViewById(R.id.buttonTop);
        Button buttonBottom = findViewById(R.id.buttonBottom);
        Button buttonShoe = findViewById(R.id.buttonShoe);
        Button buttonAcc = findViewById(R.id.buttonAcc);

        buttonTop.setOnClickListener(this);
        buttonBottom.setOnClickListener(this);
        buttonShoe.setOnClickListener(this);
        buttonAcc.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonTop:
                selectTop();
                break;
            case R.id.buttonBottom:
                selectBottom();
                break;
            case R.id.buttonShoe:
                selectShoe();
                break;
            case R.id.buttonAcc:
                selectAcc();
                break;
        }
    }

    // set itemType to top, bottom, shoe, or accessory
    public  void selectTop(){
        Intent intent = new Intent(Clothesupload.this, uploadImage.class);
        intent.putExtra("itemType", "top");
        startActivity(intent);
        finish();
    }

    public  void selectBottom(){
        Intent intent = new Intent(Clothesupload.this, uploadImage.class);
        intent.putExtra("itemType", "bottom");
        startActivity(intent);
        finish();
    }

    public  void selectShoe(){
        Intent intent = new Intent(Clothesupload.this, uploadImage.class);
        intent.putExtra("itemType", "shoe");
        startActivity(intent);
        finish();
    }

    public  void selectAcc(){
        Intent intent = new Intent(Clothesupload.this, uploadImage.class);
        intent.putExtra("itemType", "accessory");
        startActivity(intent);
        finish();
    }
}

