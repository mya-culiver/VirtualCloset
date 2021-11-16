package com.example.tommyscloset;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Clothesselect extends AppCompatActivity {

    ChipGroup chipGroup1;
    ChipGroup chipGroup2;
    ChipGroup chipGroup3;

    List<String> outfitTagArray = new ArrayList<>();
    Button buttonNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothesselect);

        chipGroup1 = findViewById(R.id.chipGroup1);
        chipGroup2 = findViewById(R.id.chipGroup2);
        chipGroup3 = findViewById(R.id.chipGroup3);


        buttonNext = findViewById(R.id.buttonNext);


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int chipsCount = chipGroup1.getChildCount() + chipGroup2.getChildCount() + chipGroup3.getChildCount();
                Log.d("tag ClothesSeelect", " chipscount total = " + chipsCount );
                chipTagsSelected(chipsCount);


                Log.d("tag ClothesSeelect", " Tag Array= " + outfitTagArray.size() );


                Bundle bundle = new Bundle();
                bundle.putSerializable("outfitTagsArray", (Serializable) outfitTagArray);

                Log.d("tagCustomAdapter", "pushed as intent =  " + outfitTagArray);

                Intent intent = new Intent(Clothesselect.this, OutfitDisplay.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    // Put tags into tagArray that were selected
    private void chipTagsSelected(int chipsCount) {
        String msg = "Chips checked are:";
        if (chipsCount == 0) {
            Log.d("tag ClothesSeelect", " chipscount total " );
        } else {
            int i = 0;
            while (i < chipsCount) {
                Chip chip1 = (Chip) chipGroup1.getChildAt(i);
                Chip chip2 = (Chip) chipGroup2.getChildAt(i);
                Chip chip3 = (Chip) chipGroup3.getChildAt(i);

                try {

                    if (chip1.isChecked()) {
                        outfitTagArray.add(chip1.getText().toString());
                        msg += chip1.getText().toString() + " ";
                    }
                } catch (Exception e ) {
                    Log.d("tag ClothesSelect", " no chip1 selected " );
                }

                try {
                    if (chip2.isChecked()) {
                        outfitTagArray.add(chip2.getText().toString());
                        msg += chip2.getText().toString() + " ";
                    }
                } catch (Exception e ) {
                    Log.d("tag ClothesSelect", " no chip2 selected " );
                }

                try {
                    if (chip3.isChecked()) {
                        outfitTagArray.add(chip3.getText().toString());
                        msg += chip3.getText().toString() + " ";
                    }
                } catch (Exception e ) {

                }
                i++;
            }
        }
        // show message
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }




}
