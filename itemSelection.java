package com.example.tommyscloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class itemSelection extends AppCompatActivity {

    private RecyclerView recyclerView;
    private itemAdapter adapter;
    private List<item> itemList;

    // Firebase
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    String userID;
    DatabaseReference dbItems;
    String itemClickedGBL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selection);

        // get what type of item was clicked
        String itemClicked = getIntent().getStringExtra("itemClicked");
        itemClickedGBL = itemClicked;

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();

        // give adapter the parameters itemAdapter(Context mCtx, List<item> itemList, String itemClicked)
        adapter = new itemAdapter(this, itemList, itemClicked);
        recyclerView.setAdapter(adapter);

        // init Firebase
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        assert fUser != null;
        userID = fUser.getUid();


        //1. SELECT * FROM Artists
        dbItems = FirebaseDatabase.getInstance().getReference("Users/" + userID + "/items");
        dbItems.addListenerForSingleValueEvent(valueEventListener);
/*
        //2. SELECT * FROM Artists WHERE id = "-LAJ7xKNj4UdBjaYr8Ju"
        Query query = FirebaseDatabase.getInstance().getReference("Artists")
                .orderByChild("id")
                .equalTo("-LAJ7xKNj4UdBjaYr8Ju");

        //3. SELECT * FROM Artists WHERE country = "India"
        Query query3 = FirebaseDatabase.getInstance().getReference("Artists")
                .orderByChild("country")
                .equalTo("India");

        //4. SELECT * FROM Artists LIMIT 2
        Query query4 = FirebaseDatabase.getInstance().getReference("Artists").limitToFirst(2);


        //5. SELECT * FROM Artists WHERE age < 30
        Query query5 = FirebaseDatabase.getInstance().getReference("Artists")
                .orderByChild("age")
                .endAt(29);


        //6. SELECT * FROM Artists WHERE name = "A%"
        Query query6 = FirebaseDatabase.getInstance().getReference("Artists")
                .orderByChild("name")
                .startAt("A")
                .endAt("A\uf8ff");

 */

        ;
        /*
         * You just need to attach the value event listener to read the values
         * for example
         * query6.addListenerForSingleValueEvent(valueEventListener)
         * */

    }
// Only add to itemList the items which have been clicked by user so that we only display those items
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            itemList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    item item = snapshot.getValue(item.class);

                    assert item != null;
                    Log.d("tagitemSelecttion", "item SnapShot running for =  " + item.itemName );

                    try {
                        if (item.itemType.equals(itemClickedGBL)) {
                            itemList.add(item);
                            Log.d("tagitemSelecttion", "item added to itemList= " + item.itemName);
                        }
                    }catch (Exception e ){

                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };
}
