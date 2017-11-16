package com.moon.gagandeep.notes;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.widget.ImageView;

import com.moon.gagandeep.notes.data.DbHelper;
import com.moon.gagandeep.notes.data.ItemContract.ItemEntry;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    public static final String TAG = "RECYCLER DATABASE";
    public List<Item> datamodel;
    RecyclerView recyclerView;
    ItemAdapter adapter;
    DbHelper helper;
    ImageView imageView;
    AppBarLayout mAppBarLayout;
    CollapsingToolbarLayout collapsingLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askPermission();
        findViews();


        final Toolbar toolbar = findViewById(R.id.toolbar);
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
        layout();
        adapter = new ItemAdapter(this, datamodel);


        Log.i("Test",""+datamodel);
        RecyclerView.LayoutManager reLayoutManager =new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(reLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                }

            }
        });

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
        helper.getItemData();
        layout();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        helper.getItemData();
        layout();
        adapter.notifyDataSetChanged();
    }

    void layout() {
        Uri randomUri;
        Random random = new Random();
        int arraySize = datamodel.size();
        if (arraySize == 0) {
            arraySize = 1;
            randomUri = Uri.parse("android.resource://com.moon.gagandeep.notes/drawable/fire");
        } else {

            int uriNumber = random.nextInt(arraySize);
            Item item = datamodel.get(uriNumber);
            randomUri = Uri.parse(item.getItemImage());
        }
        Log.i(TAG, "layout: " + arraySize);

        Picasso.with(this)
                .load(randomUri)
                .resize(720, 1280)
                .onlyScaleDown()
                .centerInside()
                .into(imageView);


    }

    void findViews() {
        mAppBarLayout = findViewById(R.id.app_bar);
        recyclerView = findViewById(R.id.recyclerView);
        imageView = findViewById(R.id.imageView);
        collapsingLayout = findViewById(R.id.collapsingLayout);
    }
}
