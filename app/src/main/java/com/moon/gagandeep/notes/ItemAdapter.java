package com.moon.gagandeep.notes;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moon.gagandeep.notes.data.DbHelper;
import com.moon.gagandeep.notes.data.ItemContract.ItemEntry;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by gagandeep on 5/11/17.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    public List<Item> list;
    DbBitmapUtility utility;
    DbHelper helper;
    private int itemRemoved;
    private Context context;
    public ItemAdapter(Context context, List<Item> list) {
        this.context = context;
        this.list = list;
    }

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


        holder.textViewDate.setText(currentItem.getItemDate());
        holder.textViewMonth.setText(currentItem.getItemMonth());
//        holder.textViewID.setText(String.valueOf(id));
        holder.textViewID.setVisibility(View.GONE);
        if (currentItem.getItemName().length() == 0)
            holder.textViewName.setVisibility(View.GONE);
        else {
            holder.textViewName.setVisibility(View.VISIBLE);
            holder.textViewName.setText(currentItem.getItemName());
        }
        if (currentItem.getItemDescription().length() == 0)
            holder.textViewDescription.setVisibility(View.GONE);
        else {
            holder.textViewDescription.setVisibility(View.VISIBLE);
            holder.textViewDescription.setText(currentItem.getItemDescription());
        }
        String imageString = currentItem.getItemImage();
        Uri imageUri = Uri.parse(imageString);
        Picasso.with(context)
                .load(imageUri)
                .resize(1080, 1920)
                .onlyScaleDown()
                .centerInside()
                .into(holder.imageView);
        holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                alertDialogue(id);
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


    // Display Alert Box
    private void alertDialogue(final int id) {
        new AlertDialog.Builder(context)
                .setMessage("Are you Sure you want to Delete")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Are you sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteItem(id);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteItem(int id) {
        Uri uri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);
        context.getContentResolver().delete(uri, null, null);
        helper.datamodel = helper.getItemData();
        list = helper.getItemData();
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDescription, textViewID, textViewMonth, textViewDate;
        ImageView imageView;
        ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.itemName);
            textViewDescription = itemView.findViewById(R.id.itemDescription);
            textViewID = itemView.findViewById(R.id.textViewId);
            imageView = itemView.findViewById(R.id.imageView);
            constraintLayout = itemView.findViewById(R.id.constraintInner);
            textViewMonth = itemView.findViewById(R.id.textViewMonth);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }
    }



}
