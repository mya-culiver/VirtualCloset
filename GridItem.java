package com.example.tommyscloset;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GridItem extends AppCompatActivity {

    TextView gridData;
    ImageView imageViewTop, imageViewBottom, imageViewAccessory, imageViewShoe;

    ChipGroup chipGroup1;

    List<String> outfitTagArray = new ArrayList<>();
    Chip chip1, chip2, chip3, chip4, chip5, chip6;

    Button wearBtn;


    // Firebase
    FirebaseAuth fAuth;
    FirebaseUser fUser;

    String userID;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_item);

        gridData = findViewById(R.id.griddata);
        imageViewTop = findViewById(R.id.imageViewTop);
        imageViewBottom = findViewById(R.id.imageViewBottom);
        imageViewShoe = findViewById(R.id.imageViewShoe);
        imageViewAccessory = findViewById(R.id.imageViewAccessory);
        chipGroup1 = findViewById(R.id.chipGroup1);
        wearBtn = findViewById(R.id.wearBtn);
        chip1 = findViewById(R.id.chip1);
        chip2 = findViewById(R.id.chip2);
        chip3 = findViewById(R.id.chip3);
        chip4 = findViewById(R.id.chip4);
        chip5 = findViewById(R.id.chip5);
        chip6 = findViewById(R.id.chip6);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String receivedName =  intent.getStringExtra("name");
        String receivedImageTop = intent.getStringExtra("imageTop");
        String receivedImageBottom = intent.getStringExtra("imageBottom");
        String receivedImageShoe = intent.getStringExtra("imageShoe");
        String receivedImageAccessory = intent.getStringExtra("imageAccessory");

        final String topNumber = intent.getStringExtra("topNumber");
        final String bottomNumber = intent.getStringExtra("bottomNumber");
        final String shoeNumber = intent.getStringExtra("shoeNumber");
        final String accessoryNumber = intent.getStringExtra("accessoryNumber");


        // init Firebase
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        assert fUser != null;
        userID = fUser.getUid();



        assert bundle != null;
        outfitTagArray =  (List<String>) bundle.getSerializable("outfitTagsArray");
        try {
            Log.d("outfitTag", " (2) outfitTag = " + outfitTagArray.size());

        } catch(Exception e) {
            Log.d("outfitTag", "outfitTag ERROR = IT'S EMPTY " );
        }


        wearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = fUser.getUid();

                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference reference = database.getReference().child("Users").child(userID)
                        .child("items");

                reference.child(topNumber).child("Laundry").setValue("dirty");

                reference.child(bottomNumber).child("Laundry").setValue("dirty");

                reference.child(shoeNumber).child("Laundry").setValue("dirty");

                reference.child(accessoryNumber).child("Laundry").setValue("dirty");

                Toast.makeText(getApplicationContext(), "Items tagged as dirty", Toast.LENGTH_LONG).show();

            }
        });



        gridData.setText(receivedName);
        Picasso.get().load(receivedImageTop).into(imageViewTop);
        Picasso.get().load(receivedImageBottom).into(imageViewBottom);
        Picasso.get().load(receivedImageShoe).into(imageViewShoe);
        Picasso.get().load(receivedImageAccessory).into(imageViewAccessory);

        try {

        if (outfitTagArray.size() != 0) {
            Log.d("tagitemAdapter", "tagArraysize = " + outfitTagArray.size() );

            switch ( outfitTagArray.size() ) {
                case 1:
                    chip1.setText(outfitTagArray.get(0));
                    chip1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    chip1.setText(outfitTagArray.get(0));
                    chip1.setVisibility(View.VISIBLE);
                    chip2.setText(outfitTagArray.get(1));
                    chip2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    chip1.setText(outfitTagArray.get(0));
                    chip1.setVisibility(View.VISIBLE);
                    chip2.setText(outfitTagArray.get(1));
                    chip2.setVisibility(View.VISIBLE);
                    chip3.setText(outfitTagArray.get(2));
                    chip3.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    chip1.setText(outfitTagArray.get(0));
                    chip1.setVisibility(View.VISIBLE);
                    chip2.setText(outfitTagArray.get(1));
                    chip2.setVisibility(View.VISIBLE);
                    chip3.setText(outfitTagArray.get(2));
                    chip3.setVisibility(View.VISIBLE);
                    chip4.setText(outfitTagArray.get(3));
                    chip4.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    chip1.setText(outfitTagArray.get(0));
                    chip1.setVisibility(View.VISIBLE);
                    chip2.setText(outfitTagArray.get(1));
                    chip2.setVisibility(View.VISIBLE);
                    chip3.setText(outfitTagArray.get(2));
                    chip3.setVisibility(View.VISIBLE);
                    chip4.setText(outfitTagArray.get(3));
                    chip4.setVisibility(View.VISIBLE);
                    chip5.setText(outfitTagArray.get(4));
                    chip5.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    chip1.setText(outfitTagArray.get(0));
                    chip1.setVisibility(View.VISIBLE);
                    chip2.setText(outfitTagArray.get(1));
                    chip2.setVisibility(View.VISIBLE);
                    chip3.setText(outfitTagArray.get(2));
                    chip3.setVisibility(View.VISIBLE);
                    chip4.setText(outfitTagArray.get(3));
                    chip4.setVisibility(View.VISIBLE);
                    chip5.setText(outfitTagArray.get(4));
                    chip5.setVisibility(View.VISIBLE);
                    chip6.setText(outfitTagArray.get(5));
                    chip6.setVisibility(View.VISIBLE);
                    break;
            }
        }
        } catch (Exception e ){

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
