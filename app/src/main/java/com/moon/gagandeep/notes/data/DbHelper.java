package com.moon.gagandeep.notes.data;

import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import com.moon.gagandeep.notes.Item;
import com.moon.gagandeep.notes.data.ItemContract.ItemEntry;
import java.util.List;

/**
 * Created by gagandeep on 5/11/17.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "item.db";
    private static final int DATABASE_VERSION = 1;

    public List<Item> datamodel = new ArrayList<>();

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + " ( "
                + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemEntry.ITEM_NAME + " TEXT NOT NULL, "
                + ItemEntry.ITEM_DESCRIPTION + " TEXT NOT NULL, "
                + ItemEntry.ITEM_IMAGE_URI + " TEXT NOT NULL );";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Item> getItemData(){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + ItemEntry.TABLE_NAME, null);
        int idIndex = cursor.getColumnIndex(ItemEntry._ID);
        int descriptionIndex = cursor.getColumnIndex(ItemEntry.ITEM_DESCRIPTION);
        int nameIndex = cursor.getColumnIndex(ItemEntry.ITEM_NAME);
        int imageIndex = cursor.getColumnIndex(ItemEntry.ITEM_IMAGE_URI);
        if (cursor.moveToFirst()){
            datamodel.clear();
            do {
                datamodel.add(new Item(cursor.getString(nameIndex),
                        cursor.getString(descriptionIndex),
                        cursor.getInt(idIndex),
                        cursor.getString(imageIndex)));
            }while (cursor.moveToNext());
        }
        return datamodel;

    }
    }
