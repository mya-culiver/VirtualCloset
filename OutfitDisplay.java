package com.example.tommyscloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class OutfitDisplay extends AppCompatActivity {

    GridView gridView;

    // List of outfit names and images
    List<String> outfitNames;
    List<String> outfitImages;
    // List of lists of tags
    List<List<String>> outfitTagsDisp;

    List<outfit> outfitList;

    List<String> outfitTagsSelected;


    // Firebase
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_display);

        outfitList = null;
        outfitTagsSelected = null;
        outfitTagsDisp = null;
        outfitImages = null;
        outfitNames = null;

        //finding listview
        gridView = findViewById(R.id.gridview);

        outfitList = new ArrayList<>();
        outfitNames = new ArrayList<>();
        outfitImages = new ArrayList<>();
        outfitTagsDisp = new ArrayList<>();


        // init Firebase
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users/" + userID);
        storageReference = getInstance().getReference();

        // store userID
        userID = fUser.getUid();


        Intent intent1 = this.getIntent();
        Bundle bundle = intent1.getExtras();
        outfitTagsSelected =  (List<String>) bundle.getSerializable("outfitTagsArray");
        Log.d("tagDisplay", "outDisplay tags selected =  " + outfitTagsSelected);


          /* We have to get info of currently signed in user. We can get it using the user's email or uid
            I'm gonna retrieve user detail using email
          By Using orderbyChild query we will show the detail from a node
          whose key named email has a value equal == to currently signed in email.
          It will search all nodes, where the key matches it will get it's detail.
         */


        Query query1 = FirebaseDatabase.getInstance().getReference("Users/" + userID + "/outfits")
                .orderByChild("outfitName");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // check until required data is found

                if(dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        outfit outfit = ds.getValue(outfit.class);
                        assert outfit != null;
                        outfitList.add(outfit);
                    }

                    for (int k = 0; k < outfitList.size(); k++) {

                        for(int i = 0; i < outfitTagsSelected.size(); i++) {

                            String outfitTagIndex;
                            outfitTagIndex = outfitTagsSelected.get(i);

                            try {
                                Log.d("tagCustomAdapter", "outfitTagsSelected =  " + outfitTagsSelected.get(i));
                            } catch (Exception e) {
                                Log.d("tagCustomAdapter", "CAUGHT AT QUERY   ");
                            }

                            if (outfitList.get(k).outfitTags.contains(outfitTagIndex)) {

                                Log.d("outfitDisplay", "outfitList.outfitTags list are  =  " + outfitList.get(k).outfitTags  );
                                Log.d("outfitDisplay", "outfitTagIndex we are checking for in that list =  " + outfitTagIndex  );
                                Log.d("outfitDisplay", "outfitName  =  " + outfitList.get(k).outfitName  );


                                    if ( !outfitNames.contains(outfitList.get(k).outfitName) ){
                                        outfitNames.add(outfitList.get(k).outfitName);
                                        outfitImages.add(outfitList.get(k).top.photoURL);
                                        outfitTagsDisp.add(outfitList.get(k).outfitTags);
                                        Log.d("tagCustomAdapter", "outfit ADDED=  " + outfitList.get(k).outfitName);
                                    }
                                    else {
                                        Log.d("tagCustomAdapter", "NO OUTFITED SKIPPED " + outfitList.get(k).outfitName);

                                    }

                                // Log.d("tagCustomAdapter", "outfit TAGS SIZE=  " + outfitTagsDisp.size());
                            }
                        }
                }
                }

                OutfitDisplay.CustomAdapter customAdapter = new OutfitDisplay.CustomAdapter();
                gridView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






/*
        Query query1 = FirebaseDatabase.getInstance().getReference("Users/" + userID + "/outfits")
                .orderByChild("outfitName");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // check until required data is found

                if(dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        outfit outfit = ds.getValue(outfit.class);
                        assert outfit != null;
                        outfitList.add(outfit);
                    }

                    if (outfitList.size() >= 1) {
                        for (int i = 0; i < outfitList.size(); i++) {
                            outfitNames.add(outfitList.get(i).outfitName);
                            outfitImages.add(outfitList.get(i).top.photoURL);
                            outfitTagsDisp.add(outfitList.get(i).outfitTags);
                            Log.d("tagCustomAdapter", "outfit TAGS SIZE=  " + outfitList.get(i).outfitTags.size());


                        }
                        // Log.d("tagCustomAdapter", "outfit TAGS SIZE=  " + outfitTagsDisp.size());
                    }
                }
                OutfitDisplay.CustomAdapter customAdapter = new OutfitDisplay.CustomAdapter();
                gridView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

 */





        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // how to use bundles https://zocada.com/using-intents-extras-pass-data-activities-android-beginners-guide/ to pass
                Bundle bundle = new Bundle();
                bundle.putSerializable("outfitTagsArray", (Serializable) outfitTagsDisp.get(i));

                Intent intent = new Intent(getApplicationContext(),GridItem.class);
                intent.putExtra("name",outfitNames.get(i));
                intent.putExtra("imageTop",outfitList.get(i).top.photoURL);
                intent.putExtra("imageBottom",outfitList.get(i).bottom.photoURL);
                intent.putExtra("imageShoe",outfitList.get(i).shoe.photoURL);
                intent.putExtra("imageAccessory",outfitList.get(i).accessory.photoURL);

                intent.putExtra("topNumber", outfitList.get(i).top.itemNumber);
                intent.putExtra("bottomNumber", outfitList.get(i).bottom.itemNumber);
                intent.putExtra("shoeNumber", outfitList.get(i).shoe.itemNumber);
                intent.putExtra("accessoryNumber", outfitList.get(i).accessory.itemNumber);

                intent.putExtras(bundle);

                // send string list
                startActivity(intent);

            }
        });

    }


    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            int outfitImagesCount = 0;
            try {
                outfitImagesCount = outfitImages.size();
            } catch (Exception e) {
            }
            return outfitImagesCount;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.row_data,null);

            //getting view in row_data
            TextView name = view1.findViewById(R.id.outfits);
            ImageView image = view1.findViewById(R.id.images);

            name.setText(outfitNames.get(i));
            Picasso.get().load(outfitImages.get(i)).into(image);

            Log.d("tagCustomAdapter", "outfit SET IMAGE =" + outfitImages.get(0));
            Log.d("tagCustomAdapter", "outfit SET NAME =" + outfitNames.get(0));

            return view1;

        }
    }


}












