package com.moon.gagandeep.notes;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import com.moon.gagandeep.notes.data.DbHelper;
import com.moon.gagandeep.notes.data.ItemContract.ItemEntry;

import static android.content.ContentValues.TAG;

/**
 * Created by gagandeep on 5/11/17.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private int itemRemoved;
    public ItemAdapter(Context context, List<Item> list) {
        this.context = context;
        this.list = list;
    }

    private Context context;
    public List<Item> list;
    DbHelper helper;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        itemRemoved = position;

        Item currentItem = list.get(position);
        final int id = currentItem.getItemId();
        helper = new DbHelper(context);
        final String idString = Integer.toString(id);

        holder.textViewID.setText(idString);
        holder.textViewName.setText(currentItem.getItemName());
        holder.textViewDescription.setText(currentItem.getItemDescription());
        final String imageString = currentItem.getItemImage();
        Uri imageUri = Uri.parse(imageString);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            holder.imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "onBindViewHolder: " + "ON BIND VIEW HOLDER");
        holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                alertDialogue(idString, id);
                return true;
            }
        });
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(id);
            }
        });
    }

    private void openActivity(int id) {
        Intent intent = new Intent(context, InsertActivity.class);
        Uri uri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);
        intent.putExtra("id", id );
        intent.setData(uri);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName, textViewDescription, textViewID;
        ImageView imageView;
        ConstraintLayout constraintLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.itemName);
            textViewDescription = itemView.findViewById(R.id.itemDescription);
            textViewID = itemView.findViewById(R.id.textViewId);
            imageView = itemView.findViewById(R.id.imageView);
            constraintLayout = itemView.findViewById(R.id.constraintInner);
        }
    }



    private void alertDialogue(final String idString, final int id) {
        new AlertDialog.Builder(context)
                .setMessage("Are you Sure you want to Delete")
                .setIcon(android.R.drawable.alert_light_frame)
                .setTitle("Are you sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteItem(idString, id);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteItem(String idString, int id) {
        Uri uri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);
        Toast.makeText(context, "" + idString, Toast.LENGTH_SHORT).show();
        context.getContentResolver().delete(uri, null, null);
        helper.datamodel = helper.getItemData();
        list = helper.getItemData();
        notifyDataSetChanged();

    }



}
