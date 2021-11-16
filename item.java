package com.example.tommyscloset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class item implements Serializable {

    public String itemName;
    public String itemNumber;
    public String itemType;
    public String photoURL;
    public List<String> tags;

    public item() {
    }

}
