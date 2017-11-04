package com.moon.gagandeep.notes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.moon.gagandeep.notes.data.ItemContract.ItemEntry;

import com.moon.gagandeep.notes.data.DbHelper;

import java.io.IOException;

public class InsertActivity extends AppCompatActivity {
    TextInputEditText editTextName, editTextDescription;
    ImageView imageView;
    Button insertButton;
    Uri imageUri, currentItemUri;
    DbHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        findViews();
        Intent intent = getIntent();
        currentItemUri = intent.getData();
        String[] projection = {
                ItemEntry._ID,
                ItemEntry.ITEM_NAME,
                ItemEntry.ITEM_DESCRIPTION,
                ItemEntry.ITEM_IMAGE_URI };
        DbHelper helper = new DbHelper(this);
        SQLiteDatabase database = helper.getReadableDatabase();
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

                Uri imageUri = Uri.parse(imageString);
                Toast.makeText(this, "sd " + itemId, Toast.LENGTH_SHORT).show();

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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImage = new Intent(Intent.ACTION_PICK);
                getImage.setType("image/*");
                startActivityForResult(getImage, 1);
            }
        });
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }


    private void insertData() {
        String imageString = imageUri.toString();
        String itemName = editTextName.getText().toString();
        String itemDescription = editTextDescription.getText().toString();
        ContentValues values = new ContentValues();
        values.put(ItemEntry.ITEM_NAME, itemName);
        values.put(ItemEntry.ITEM_DESCRIPTION, itemDescription);
        values.put(ItemEntry.ITEM_IMAGE_URI, imageString);
        getContentResolver().insert(ItemEntry.CONTENT_URI, values);
        helper = new DbHelper(this);
        helper.getItemData();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);if (requestCode== Activity.RESULT_OK);
        switch (requestCode){
            case 1:
                Uri getImage = data.getData();
                imageUri = getImage;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), getImage);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void findViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        imageView = findViewById(R.id.frameLayout);
        insertButton = findViewById(R.id.buttonDone);
    }
}
