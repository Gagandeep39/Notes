package com.moon.gagandeep.notes;

/**
 * Created by gagandeep on 5/11/17.
 */


public class Item {

    private String itemName;
    private String itemDescription;
    private int itemId;
    private String itemMonth;
    private String itemDate;
    private String itemImage;

    public Item(String itemName, String itemDescription, int itemId, String itemMonth, String itemDate, String itemImage) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemId = itemId;
        this.itemMonth = itemMonth;
        this.itemDate = itemDate;
        this.itemImage = itemImage;
    }

    public String getItemMonth() {
        return itemMonth;
    }

    public String getItemDate() {
        return itemDate;
    }


    public String getItemName() {
        return itemName;
    }


    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemImage() {
        return itemImage;
    }

    public int getItemId() {
        return itemId;
    }
}
