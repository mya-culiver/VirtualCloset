package com.example.tommyscloset;

import android.app.Application;

public class itemOutfitList extends Application {

    private item topItem;
    private item bottomItem;
    private item shoeItem;
    private item accessoryItem;


    public item getTopItem() {
        return topItem;
    }

    public void setTopItem(item topItem) {
        this.topItem = topItem;
    }

    public item getBottomItem() {
        return bottomItem;
    }

    public void setBottomItem(item bottomItem) {
        this.bottomItem = bottomItem;
    }

    public item getShoeItem() {
        return shoeItem;
    }

    public void setShoeItem(item shoeItem) {
        this.shoeItem = shoeItem;
    }

    public item getAccessoryItem() {
        return accessoryItem;
    }

    public void setAccessoryItem(item accessoryItem) {
        this.accessoryItem = accessoryItem;
    }

}