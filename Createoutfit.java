package com.example.tommyscloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.app.Application;

import static com.example.tommyscloset.uploadImage.GALLERY_REQUEST_CODE;

public class Createoutfit extends AppCompatActivity {

    // Buttons and ImageView
    ImageView itemShoeView, itemBottomView, itemAccView, itemTopView;
    Button buttonBottom, buttonShoe, buttonTop, buttonAcc, buttonNext, buttonReset;
    ChipGroup chipGroup1;
    private List<item> outfitList;
    EditText outfitNameTxt;

    List<String> outfitTagArray = new ArrayList<>();

    // Firebase
    FirebaseAuth fAuth;
    FirebaseUser fUser;

    String userID;

    // init items
    item topItem;
    item bottomItem;
    item shoeItem;
    item accessoryItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createoutfit);

        // init imageViews
        itemShoeView = findViewById(R.id.itemShoeView);
        itemBottomView = findViewById(R.id.itemBottomView);
        itemAccView = findViewById(R.id.itemAccView);
        itemTopView = findViewById(R.id.itemTopView);
        chipGroup1 = findViewById(R.id.chipGroup1);
        outfitNameTxt = findViewById(R.id.outfitNameText);

        // init buttons
        buttonBottom = findViewById(R.id.buttonBottom);
        buttonShoe = findViewById(R.id.buttonShoe);
        buttonTop = findViewById(R.id.buttonTop);
        buttonAcc = findViewById(R.id.buttonAcc);
        buttonNext = findViewById(R.id.buttonNext);
        buttonReset = findViewById(R.id.buttonReset);

        // init Firebase
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        assert fUser != null;
        userID = fUser.getUid();

        //https://stackoverflow.com/questions/1944656/android-global-variable for application help
        final Application application1 = this.getApplication();

        Intent intent1 = this.getIntent();
        Bundle bundle = intent1.getExtras();


        item selectedItem = null;
        boolean choseItem = false;
        choseItem = getIntent().getBooleanExtra("chosen", false);

        if (choseItem){
            //Type object = (Type) bundle.getSerializable("KEY");
            //we can send ArrayList, HashMaps or any serializable objects
            //Use the Type of the object instead of DataClass
            //in the above example
            assert bundle != null;
            selectedItem = (item) bundle.getSerializable("item");
        }


        // check what the itemClicked was to update the item for it
        if (selectedItem != null){
            Log.d("tagCreateOutfit", "selectedItem intent = " + selectedItem.itemName );
            Log.d("tagCreateOutfit", "selectedItem intent = " + selectedItem.photoURL );

            switch ( selectedItem.itemType ) {
                case "top":
                    Log.d("tagCreateOutfit", "Select case1 = " + selectedItem.itemName);

                    ((itemOutfitList) this.getApplication()).setTopItem(selectedItem);
                    topItem = ((itemOutfitList) this.getApplication()).getTopItem();

                    break;
                case "bottom":
                    Log.d("tagCreateOutfit", "Select case2 = " + selectedItem.itemName);

                    ((itemOutfitList) this.getApplication()).setBottomItem(selectedItem);
                    bottomItem = ((itemOutfitList) this.getApplication()).getBottomItem();

                    break;
                case "shoe":
                    Log.d("tagCreateOutfit", "Select case3 = " + selectedItem.itemName);

                    ((itemOutfitList) this.getApplication()).setShoeItem(selectedItem);
                    shoeItem = ((itemOutfitList) this.getApplication()).getShoeItem();

                    break;
                case "accessory":
                    Log.d("tagCreateOutfit", "Select case4 = " + selectedItem.itemName);

                    ((itemOutfitList) this.getApplication()).setAccessoryItem(selectedItem);
                    accessoryItem = ((itemOutfitList) this.getApplication()).getAccessoryItem();

                    break;
            }

            try {
                topItem = ((itemOutfitList) this.getApplication()).getTopItem();
                Picasso.get()
                        .load(topItem.photoURL)
                        .fit()
                        .centerCrop()
                        .into(itemTopView);
            } catch (Exception e) {
                Log.d("tagCreateOutfit", "catch @ Top item = " );
            }

            try {
                bottomItem = ((itemOutfitList) this.getApplication()).getBottomItem();
                Picasso.get()
                        .load(bottomItem.photoURL)
                        .fit()
                        .centerCrop()
                        .into(itemBottomView);
            } catch (Exception e) {
                Log.d("tagCreateOutfit", "catch @ Bottom item = " );
            }

            try {
                shoeItem = ((itemOutfitList) this.getApplication()).getShoeItem();
                Picasso.get()
                        .load(shoeItem.photoURL)
                        .fit()
                        .centerCrop()
                        .into(itemShoeView);
            } catch (Exception e) {
                Log.d("tagCreateOutfit", "catch @ Shoe item = " );
            }

            try {
                accessoryItem = ((itemOutfitList) this.getApplication()).getAccessoryItem();
                Picasso.get()
                        .load(accessoryItem.photoURL)
                        .fit()
                        .centerCrop()
                        .into(itemAccView);
            } catch (Exception e) {
                Log.d("tagCreateOutfit", "catch @ accessory item = " );
            }

        }



        buttonBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Createoutfit.this, itemSelection.class);
                intent.putExtra("itemClicked", "bottom");
                startActivity(intent);
                finish();
            }
        });

        buttonShoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Createoutfit.this, itemSelection.class);
                intent.putExtra("itemClicked", "shoe");
                startActivity(intent);
                finish();
            }
        });

        buttonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Createoutfit.this, itemSelection.class);
                intent.putExtra("itemClicked", "top");
                startActivity(intent);
                finish();
            }
        });

        buttonAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent = new Intent(Createoutfit.this, itemSelection.class);
                intent.putExtra("itemClicked", "accessory");
                    startActivity(intent);
                    finish();
            }
        });


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int chipsCount = chipGroup1.getChildCount();
                chipTagsSelected(chipsCount);


                // store userID
                userID = fUser.getUid();

                // get itemNameTxt from user entry
                String outfitName = outfitNameTxt.getText().toString().trim();

                // generate RNG outfitNumber
                String userStamp = userID;
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String outfitNumber = userStamp + "_" + timeStamp ;

                //-------------------------------------------------------------------------------------
                // Adding values to the outfit to create

                // Using Hashmap
                HashMap<Object, String> hashMap = new HashMap<>();
                // Put info in hashmap
                hashMap.put("outfitName", outfitName);
                hashMap.put("outfitNumber", outfitNumber);

                FirebaseDatabase database = FirebaseDatabase.getInstance();

                // Path to store user data named "Users/'userID'/items"
                DatabaseReference reference = database.getReference().child("Users").child(userID)
                        .child("outfits").child(outfitNumber);

                // Put data within hashmap in database
                reference.setValue(hashMap);
                // for 2nd hashmap for Tags
                reference.child("outfitTags").setValue(outfitTagArray);

                Log.d("tagCreateOutfit", "topItem IS HERE += " + topItem.itemName );
                Log.d("tagCreateOutfit", "bottomItem IS HERE += " + bottomItem.itemName );
                Log.d("tagCreateOutfit", "shoeItem IS HERE += " + shoeItem.itemName );
                Log.d("tagCreateOutfit", "accessoryItem IS HERE += " + accessoryItem.itemName );

                // For adding each item

                //-------------------------------------------------------------------------------------
                // "topItem"

                // Using Hashmap
                HashMap<Object, String> hashMapTOP = new HashMap<>();
                // Put info in hashmap
                hashMapTOP.put("itemNumber", topItem.itemNumber);
                hashMapTOP.put("itemName", topItem.itemName);
                hashMapTOP.put("photoURL", topItem.photoURL);
                hashMapTOP.put("itemType", topItem.itemType);

                // Puta into outfitNumber/top +/tags
                reference.child("top").setValue(hashMapTOP);
                reference.child("top").child("tags").setValue(topItem.tags);

                //-------------------------------------------------------------------------------------
                // "bottomItem"

                // Using Hashmap
                HashMap<Object, String> hashMapBOT = new HashMap<>();
                // Put info in hashmap
                hashMapBOT.put("itemNumber", bottomItem.itemNumber);
                hashMapBOT.put("itemName", bottomItem.itemName);
                hashMapBOT.put("photoURL", bottomItem.photoURL);
                hashMapBOT.put("itemType", bottomItem.itemType);

                // Puta into outfitNumber/top +/tags
                reference.child("bottom").setValue(hashMapBOT);
                reference.child("bottom").child("tags").setValue(bottomItem.tags);

                //-------------------------------------------------------------------------------------
                // "shoeItem"

                // Using Hashmap
                HashMap<Object, String> hashMapSHOE = new HashMap<>();
                // Put info in hashmap
                hashMapSHOE.put("itemNumber", shoeItem.itemNumber);
                hashMapSHOE.put("itemName", shoeItem.itemName);
                hashMapSHOE.put("photoURL", shoeItem.photoURL);
                hashMapSHOE.put("itemType", shoeItem.itemType);

                // Puta into outfitNumber/top +/tags
                reference.child("shoe").setValue(hashMapSHOE);
                reference.child("shoe").child("tags").setValue(shoeItem.tags);

                //-------------------------------------------------------------------------------------
                // "shoeItem"

                // Using Hashmap
                HashMap<Object, String> hashMapACC = new HashMap<>();
                // Put info in hashmap
                hashMapACC.put("itemNumber", accessoryItem.itemNumber);
                hashMapACC.put("itemName", accessoryItem.itemName);
                hashMapACC.put("photoURL", accessoryItem.photoURL);
                hashMapACC.put("itemType", accessoryItem.itemType);

                // Puta into outfitNumber/top +/tags
                reference.child("accessory").setValue(hashMapACC);
                reference.child("accessory").child("tags").setValue(accessoryItem.tags);

                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 topItem = null;
                 bottomItem = null;
                 shoeItem = null;
                 accessoryItem = null;

                ((itemOutfitList) application1).setTopItem(null);
                ((itemOutfitList) application1).setBottomItem(null);
                ((itemOutfitList) application1).setShoeItem(null);
                ((itemOutfitList) application1).setAccessoryItem(null);

                startActivity(new Intent(getApplicationContext(), Createoutfit.class));

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
                    outfitTagArray.add(chip.getText().toString());
                    msg += chip.getText().toString() + " " ;
                }
                i++;
            }
        }
        // show message
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }

}
