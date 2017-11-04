package com.moon.gagandeep.notes.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.moon.gagandeep.notes.data.ItemContract.ItemEntry;

/**
 * Created by gagandeep on 5/11/17.
 */

public class ItemProvider extends ContentProvider {
    DbHelper helper;
    public static final int ITEM = 100;
    public static final int ITEM_ID = 101;
    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.ITEM_PATH, ITEM);
        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.ITEM_PATH + "/#", ITEM_ID);
    }
    @Override
    public boolean onCreate() {
        helper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match){
            case ITEM:
                cursor = database.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ITEM_ID:
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default : throw new IllegalArgumentException("Error in Query");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);
        switch (match){
            case ITEM:
                return insertItem(uri, values);
            default: throw new IllegalArgumentException("Error while inserting");
        }
    }

    private Uri insertItem(Uri uri, ContentValues values) {
        SQLiteDatabase database = helper.getWritableDatabase();
        long id = database.insert(ItemEntry.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        int rowsDeleted;
        SQLiteDatabase database = helper.getWritableDatabase();
        switch (match){
            case ITEM:
                rowsDeleted = database.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM_ID:
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default: throw new IllegalArgumentException("Error while deleted");

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        switch (match){
            case ITEM:
                return updateItems(uri, values, selection, selectionArgs);
            case ITEM_ID:
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItems(uri, values, selection, selectionArgs);

        }
        return 0;
    }

    private int updateItems(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = helper.getWritableDatabase();
        int id = database.update(ItemEntry.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }
}
