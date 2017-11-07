package com.moon.gagandeep.notes;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.moon.gagandeep.notes.data.DbHelper;
import com.moon.gagandeep.notes.data.ItemContract.ItemEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    public static final String TAG = "RECYCLER DATABASE";
    public List<Item> datamodel;
    RecyclerView recyclerView;
    ItemAdapter adapter;
    DbHelper helper;


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
//        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);

    }


    private void askPermission() {
        if (Build.VERSION.SDK_INT >= 23
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
        if (Build.VERSION.SDK_INT >= 23
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }
    }

    private void insertDummyData() {

        Calendar c = Calendar.getInstance();
        String monthDate = "" + new SimpleDateFormat("MMM").format(c.getTime());
        String dateDate = "" + new SimpleDateFormat("dd").format(c.getTime());
        ContentValues values = new ContentValues();
        values.put(ItemEntry.ITEM_NAME, "Item Name");
        values.put(ItemEntry.ITEM_DESCRIPTION, "This is the description of item");
        values.put(ItemEntry.ITEM_IMAGE_URI, "android.resource://com.moon.gagandeep.notes/drawable/fire");
        values.put(ItemEntry.ITEM_DATE, dateDate);
        values.put(ItemEntry.ITEM_MONTH, monthDate);

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
