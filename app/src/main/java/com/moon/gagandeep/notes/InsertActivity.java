package com.moon.gagandeep.notes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.moon.gagandeep.notes.data.DbHelper;
import com.moon.gagandeep.notes.data.ItemContract.ItemEntry;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InsertActivity extends AppCompatActivity {
    private static final int IMAGE_PICKER_CODE = 7;
    int counter = 0;
    TextInputEditText editTextName, editTextDescription;
    ImageView imageView;
    Uri currentItemUri;
    String imageStringToSave;//to to save a new image uri
    DbHelper helper;
    String updatedImageString = null;//used to update a row
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViews();
        Intent intent = getIntent();
        currentItemUri = intent.getData();
        if (currentItemUri != null) {
            counter = 1;
            modifyData();
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImage = new Intent(Intent.ACTION_PICK);
                getImage.setType("image/*");
                startActivityForResult(getImage, 1);
            }
        });
    }

    private void modifyData() {

        String[] projection = {
                ItemEntry._ID,
                ItemEntry.ITEM_NAME,
                ItemEntry.ITEM_DESCRIPTION,
                ItemEntry.ITEM_IMAGE_URI };
        Cursor cursor = getContentResolver().query(currentItemUri, projection, null, null, null);
        if (cursor.moveToNext()){
            do{
                int idColumnIndex = cursor.getColumnIndex(ItemEntry._ID);
                int nameColumnIndex = cursor.getColumnIndex(ItemEntry.ITEM_NAME);
                int descriptionColumnIndex = cursor.getColumnIndex(ItemEntry.ITEM_DESCRIPTION);
                int imageColumnIndex = cursor.getColumnIndex(ItemEntry.ITEM_IMAGE_URI);

                int itemId = cursor.getInt(idColumnIndex);
                String nameString = cursor.getString(nameColumnIndex);
                String descriptionString = cursor.getString(descriptionColumnIndex);
                String imageString = cursor.getString(imageColumnIndex);
                updatedImageString = imageString;

                Uri imageUri = Uri.parse(imageString);

                editTextName.setText(nameString);
                editTextDescription.setText(descriptionString);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());

        }
        cursor.close();


    }

    private void updateData() {
        Calendar c = Calendar.getInstance();
        String monthDate = "" + new SimpleDateFormat("MMM").format(c.getTime());
        String dateDate = "" + new SimpleDateFormat("dd").format(c.getTime());
        String itemName = editTextName.getText().toString();
        String itemDescription = editTextDescription.getText().toString();
        ContentValues values = new ContentValues();
        values.put(ItemEntry.ITEM_NAME, itemName);
        values.put(ItemEntry.ITEM_DESCRIPTION, itemDescription);
        values.put(ItemEntry.ITEM_IMAGE_URI, updatedImageString);
        values.put(ItemEntry.ITEM_DATE, dateDate);
        values.put(ItemEntry.ITEM_MONTH, monthDate);
        getContentResolver().update(currentItemUri, values, null, null);
        finish();

    }


    private void insertData() {
        Calendar c = Calendar.getInstance();
        String monthDate = "" + new SimpleDateFormat("MMM").format(c.getTime());
        String dateDate = "" + new SimpleDateFormat("dd").format(c.getTime());
        String itemName = editTextName.getText().toString();
        String itemDescription = editTextDescription.getText().toString();
        ContentValues values = new ContentValues();
        values.put(ItemEntry.ITEM_NAME, itemName);
        values.put(ItemEntry.ITEM_DESCRIPTION, itemDescription);
        values.put(ItemEntry.ITEM_IMAGE_URI, imageStringToSave);
        values.put(ItemEntry.ITEM_DATE, dateDate);
        values.put(ItemEntry.ITEM_MONTH, monthDate);

        if (itemDescription.length() > 0 && TextUtils.isEmpty(itemName)) {
            Toast.makeText(this, "Insert Title", Toast.LENGTH_SHORT).show();
        } else {
            getContentResolver().insert(ItemEntry.CONTENT_URI, values);
            helper = new DbHelper(this);
            helper.getItemData();
            finish();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        switch (requestCode){
            case 1:
                Uri getImage = data.getData();
                imageStringToSave = getImage.toString();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), getImage);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.done:
                if (counter == 0)
                    insertData();
                else
                    updateData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        imageView = findViewById(R.id.frameLayout);
    }

}
