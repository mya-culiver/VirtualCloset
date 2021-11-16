package com.example.tommyscloset;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Belal on 4/17/2018.
 */

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.itemViewHolder> {

    private Context mCtx;
    private List<item> itemList;
    private String itemClicked;

    public itemAdapter(Context mCtx, List<item> itemList, String itemClicked) {
        this.mCtx = mCtx;
        this.itemList = itemList;
        this.itemClicked = itemClicked;
    }


    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_items, parent, false);
        return new itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {

        if (itemList.get(position).itemType.equals(itemClicked)){

        final item item = itemList.get(position);
        List<String> tagArray1 = item.tags;

        holder.textViewName.setText(item.itemName);
        holder.textViewType.setText("Type: " + item.itemType);
        Picasso.get().load(item.photoURL).into(holder.imageView);
        holder.buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // how to use bundles https://zocada.com/using-intents-extras-pass-data-activities-android-beginners-guide/ to pass
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                Intent intent = new Intent(mCtx, Createoutfit.class);
                Log.d("tagitemAdapter", "add item clicked for = " + item.itemName );
                intent.putExtras(bundle);
                intent.putExtra("chosen", true);
                mCtx.startActivity(intent);
            }
        });

        Log.d("tagitemAdapter", "item added = " + item.itemName );

        // Switch to see how many chip Texts to update upto 6
        if (tagArray1.size() != 0) {
            Log.d("tagitemAdapter", "tagArraysize = " + tagArray1.size() );

            switch ( tagArray1.size() ) {
                case 1:
                    Log.d("tagitemAdapter", "case1 = " + tagArray1.size() );
                    holder.chip1.setText(tagArray1.get(0));
                    holder.chip1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    Log.d("tagitemAdapter", "case2 = " + tagArray1.size() );
                    holder.chip1.setText(tagArray1.get(0));
                    holder.chip1.setVisibility(View.VISIBLE);
                    holder.chip2.setText(tagArray1.get(1));
                    holder.chip2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    Log.d("tagitemAdapter", "case3 = " + tagArray1.size() );
                    holder.chip1.setText(tagArray1.get(0));
                    holder.chip1.setVisibility(View.VISIBLE);
                    holder.chip2.setText(tagArray1.get(1));
                    holder.chip2.setVisibility(View.VISIBLE);
                    holder.chip3.setText(tagArray1.get(2));
                    holder.chip3.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    Log.d("tagitemAdapter", "case4 = " + tagArray1.size() );
                    holder.chip1.setText(tagArray1.get(0));
                    holder.chip1.setVisibility(View.VISIBLE);
                    holder.chip2.setText(tagArray1.get(1));
                    holder.chip2.setVisibility(View.VISIBLE);
                    holder.chip3.setText(tagArray1.get(2));
                    holder.chip3.setVisibility(View.VISIBLE);
                    holder.chip4.setText(tagArray1.get(3));
                    holder.chip4.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    holder.chip1.setText(tagArray1.get(0));
                    holder.chip1.setVisibility(View.VISIBLE);
                    holder.chip2.setText(tagArray1.get(1));
                    holder.chip2.setVisibility(View.VISIBLE);
                    holder.chip3.setText(tagArray1.get(2));
                    holder.chip3.setVisibility(View.VISIBLE);
                    holder.chip4.setText(tagArray1.get(3));
                    holder.chip4.setVisibility(View.VISIBLE);
                    holder.chip5.setText(tagArray1.get(4));
                    holder.chip5.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    holder.chip1.setText(tagArray1.get(0));
                    holder.chip1.setVisibility(View.VISIBLE);
                    holder.chip2.setText(tagArray1.get(1));
                    holder.chip2.setVisibility(View.VISIBLE);
                    holder.chip3.setText(tagArray1.get(2));
                    holder.chip3.setVisibility(View.VISIBLE);
                    holder.chip4.setText(tagArray1.get(3));
                    holder.chip4.setVisibility(View.VISIBLE);
                    holder.chip5.setText(tagArray1.get(4));
                    holder.chip5.setVisibility(View.VISIBLE);
                    holder.chip6.setText(tagArray1.get(5));
                    holder.chip6.setVisibility(View.VISIBLE);
                    break;
            }
        }
        }

    }

    @Override
    public int getItemCount() {

        int itemCount = 0;
        int itemSize = itemList.size();

        for (  int i = 0; i < itemSize; i++ ) {
            if (itemList.get(i).itemType.equals(itemClicked)) {
                itemCount += 1;
                Log.d("tagitemAdapter", "item added = " + itemList.get(i).itemType );
            }
        }
        return itemCount;
    }

    class itemViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewType;
        ImageView imageView;
        Button  buttonSelect;
        Chip chip1, chip2, chip3, chip4, chip5, chip6;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewType = itemView.findViewById(R.id.text_view_Type);
            imageView = itemView.findViewById(R.id.imageView);
            buttonSelect = itemView.findViewById(R.id.buttonSelect);
            chip1 = itemView.findViewById(R.id.chip1);
            chip2 = itemView.findViewById(R.id.chip2);
            chip3 = itemView.findViewById(R.id.chip3);
            chip4 = itemView.findViewById(R.id.chip4);
            chip5 = itemView.findViewById(R.id.chip5);
            chip6 = itemView.findViewById(R.id.chip6);
        }
    }
}