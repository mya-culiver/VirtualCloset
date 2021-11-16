package com.example.tommyscloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class uploadItem extends AppCompatActivity {

    ImageView itemImageView;
    Button buttonCreate;
    TextView itemTypeTxt;
    EditText itemNameTxt;
    ChipGroup chipGroup1;
    List<String> tagArray = new ArrayList<>();
    String userID;

    FirebaseUser fUser;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);

        itemImageView = findViewById(R.id.itemImageView);
        buttonCreate = findViewById(R.id.buttonCreate);
        itemTypeTxt = findViewById(R.id.itemTypeTxt);
        itemNameTxt = findViewById(R.id.itemNameTxt);
        chipGroup1 = findViewById(R.id.chipGroup1);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();


        // get imageUri from previous activity pushed
        String imageUri = getIntent().getStringExtra("imageUri");
        // put imageUri into imageView
        Log.d("tag", "imageUri == " + imageUri );
        try {
            // if image is received then set
            Picasso.get().load(imageUri).into(itemImageView);
        } catch (Exception e) {
            // if there is any exception while getting image then default image
            Picasso.get().load(R.drawable.ic_launcher_background).into(itemImageView);
        }

        buttonCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int chipsCount = chipGroup1.getChildCount();
                chipTagsSelected(chipsCount);

                // store userID
                userID = fUser.getUid();

                // get itemNameTxt from user entry
                String itemName = itemNameTxt.getText().toString().trim();

                // generate RNG itemNum
                String userStamp = userID;
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String itemNum = userStamp + "_" + timeStamp ;

                // get itemName and imageUri from previous activity pushed
                String itemType = getIntent().getStringExtra("itemType");
                String imageUri = getIntent().getStringExtra("imageUri");

                // Using Hashmap
                HashMap<Object, String> hashMap = new HashMap<>();
                // Put info in hashmap
                hashMap.put("itemNumber", itemNum);
                hashMap.put("itemName", itemName);
                hashMap.put("photoURL", imageUri); // Add later (e.g., edit the profile)
                hashMap.put("itemType", itemType);
                //hashMap.put("tags", "");

                // tags separate w/ different type
                //HashMap<Object, List> hashMap1 = new HashMap<>();
                //hashMap1.put("tags", tagArray);

                // Firebase database instance
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                // Path to store user data named "Users/'userID'/items"
                DatabaseReference reference = database.getReference().child("Users").child(userID)
                        .child("items").child(itemNum);
                // Put data within hashmap in database
                reference.setValue(hashMap);
                // for 2nd hashmap for Tags
                reference.child("tags").setValue(tagArray);

                startActivity(new Intent(getApplicationContext(), ClothesuploadSelect.class));
                finish();
            }
        });
    }
    // Put tags into tagArray that were selected
    private void chipTagsSelected(int chipsCount) {
        String msg = "Chips checked are:";
        if (chipsCount == 0) {
            Toast.makeText(getApplicationContext(), "Select at least one tag", Toast.LENGTH_LONG).show();
        } else {
            int i = 0;
            while (i < chipsCount) {
                Chip chip = (Chip) chipGroup1.getChildAt(i);
                if (chip.isChecked() ) {
                    tagArray.add(chip.getText().toString());
                    msg += chip.getText().toString() + " " ;
                }
                i++;
            }
        }
        // show message
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }
}
