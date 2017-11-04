package com.moon.gagandeep.notes.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by gagandeep on 5/11/17.
 */


public class ItemContract {
    public static final String CONTENT_AUTHORITY = "com.moon.gagandeep.notes"; //make sure you don't write .data here while copying the import statement
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String ITEM_PATH = "items";
    public static final class ItemEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, ITEM_PATH);
        public static final String TABLE_NAME = "items";
        public static final String _ID = BaseColumns._ID;
        public static final String ITEM_NAME = "name";
        public static final String ITEM_DESCRIPTION = "description";
        public static final String ITEM_IMAGE_URI = "imagestring";
    }
}
