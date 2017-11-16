package com.moon.gagandeep.notes;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moon.gagandeep.notes.data.DbHelper;
import com.moon.gagandeep.notes.data.ItemContract.ItemEntry;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

/**
 * Created by gagandeep on 5/11/17.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    public List<Item> list;
    DbHelper helper;
    CardView cardView;
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
        holder.textViewID.setVisibility(View.GONE);


        if (currentItem.getItemName().length() == 0) {
            holder.linearLayout.setVisibility(View.GONE);
            holder.textViewName.setVisibility(View.GONE);
        }
        else {
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.textViewName.setVisibility(View.VISIBLE);
            String name = String.valueOf(currentItem.getItemName().charAt(0)).toUpperCase() + currentItem.getItemName().subSequence(1, currentItem.getItemName().length());
            holder.textViewName.setText(name);
        }
        if (currentItem.getItemDescription().length() == 0) {
            holder.textViewDescription.setVisibility(View.GONE);
        }
        else {
            holder.textViewDescription.setVisibility(View.VISIBLE);
            String desc = String.valueOf(currentItem.getItemDescription().charAt(0)).toUpperCase() + currentItem.getItemDescription().subSequence(1, currentItem.getItemDescription().length());
            holder.textViewDescription.setText(desc);
        }
        String imageString = currentItem.getItemImage();
        Uri imageUri = Uri.parse(imageString);
        Picasso.with(context)
                .load(imageUri)
                .resize(480, 640)
                .onlyScaleDown()
                .centerInside()
                .into(holder.imageView);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                    Palette.Swatch dominant = palette.getDominantSwatch();
                    if (vibrant != null)
                        holder.cardView.setCardBackgroundColor(vibrant.getRgb());
                    else
                        holder.cardView.setCardBackgroundColor(dominant.getRgb());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        CardView cardView;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.itemName);
            textViewDescription = itemView.findViewById(R.id.itemDescription);
            textViewID = itemView.findViewById(R.id.textViewId);
            imageView = itemView.findViewById(R.id.imageView);
            constraintLayout = itemView.findViewById(R.id.constraintInner);
            textViewMonth = itemView.findViewById(R.id.textViewMonth);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            cardView = itemView.findViewById(R.id.cardView);
            linearLayout = itemView.findViewById(R.id.linearLayout);

        }
    }




}


