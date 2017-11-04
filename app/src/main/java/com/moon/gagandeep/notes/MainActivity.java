package com.moon.gagandeep.notes;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.moon.gagandeep.notes.data.ItemContract.ItemEntry;

import com.moon.gagandeep.notes.data.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    public static final String TAG = "RECYCLER DATABASE";
    RecyclerView recyclerView;
    ItemAdapter adapter;
    DbHelper helper;
    public List<Item> datamodel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askPermission();
        recyclerView = findViewById(R.id.recyclerView);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InsertActivity.class));
            }
        });


        datamodel = new ArrayList<>();
        helper = new DbHelper(this);
        datamodel=  helper.getItemData();
        adapter = new ItemAdapter(this, datamodel);


        Log.i("Test",""+datamodel);
        RecyclerView.LayoutManager reLayoutManager =new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(reLayoutManager);
        recyclerView.setAdapter(adapter);

    }


    private void askPermission() {
        if (Build.VERSION.SDK_INT >= 23
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }

    private void insertDummyData() {
        ContentValues values = new ContentValues();
        values.put(ItemEntry.ITEM_NAME, "Item Name");
        values.put(ItemEntry.ITEM_DESCRIPTION, "This is the description of item");
        values.put(ItemEntry.ITEM_IMAGE_URI, "content://media/external/file/9001");
        getContentResolver().insert(ItemEntry.CONTENT_URI, values);
        datamodel = helper.getItemData();
        adapter.list = helper.getItemData();
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.insertDummy:
                insertDummyData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        helper.getItemData();
        adapter.notifyDataSetChanged();
    }
}
