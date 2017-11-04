package com.moon.gagandeep.notes;

/**
 * Created by gagandeep on 5/11/17.
 */


public class Item {

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }
    public String getItemImage() {
        return itemImage;
    }


    public Item(String itemName, String itemDescription, int itemId, String itemImage) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
        this.itemId = itemId;
    }

    private String itemName;
    private String itemDescription;

    public int getItemId() {
        return itemId;
    }

    private int itemId;



    private String itemImage;
}
