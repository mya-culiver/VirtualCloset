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

public class Laundry extends AppCompatActivity {


    GridView gridView;

    // List of outfit names and images
    List<String> itemNames;
    List<String> itemImages;


    List<item> itemList;


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
        setContentView(R.layout.activity_laundry);


        itemNames = null;
        itemImages = null;
        itemList = null;

        //finding listview
        gridView = findViewById(R.id.gridview);


        itemList = new ArrayList<>();
        itemNames = new ArrayList<>();
        itemImages = new ArrayList<>();


        // init Firebase
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users/" + userID);
        storageReference = getInstance().getReference();

        // store userID
        userID = fUser.getUid();



        Query query1 = FirebaseDatabase.getInstance().getReference("Users/" + userID + "/items")
                .orderByChild("Laundry")
                .equalTo("dirty");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // check until required data is found

                if(dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        item item = ds.getValue(item.class);
                        assert item != null;
                        itemList.add(item);
                    }

                    if (itemList.size() >= 1) {
                        for (int i = 0; i < itemList.size(); i++) {
                            itemNames.add(itemList.get(i).itemName);
                            itemImages.add(itemList.get(i).photoURL);
                        }
                        // Log.d("tagCustomAdapter", "outfit TAGS SIZE=  " + outfitTagsDisp.size());
                    }
                }
                Laundry.CustomAdapter customAdapter = new Laundry.CustomAdapter();
                gridView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference().child("Users").child(userID)
                        .child("items");


                reference.child(itemList.get(i).itemNumber).child("Laundry").setValue("clean");
                Toast.makeText(getApplicationContext(), "Item set clean = " + itemList.get(i).itemName, Toast.LENGTH_LONG).show();

                startActivity(intent);

            }
        });

    }

    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            int itemImagesCount = 0;
            try {
                itemImagesCount = itemImages.size();
            } catch (Exception e) {
            }
            return itemImagesCount;
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

            name.setText(itemNames.get(i));
            Picasso.get().load(itemImages.get(i)).into(image);

            Log.d("tagCustomAdapter", "outfit SET IMAGE =" + itemImages.get(0));
            Log.d("tagCustomAdapter", "outfit SET NAME =" + itemNames.get(0));

            return view1;

        }
    }






}
