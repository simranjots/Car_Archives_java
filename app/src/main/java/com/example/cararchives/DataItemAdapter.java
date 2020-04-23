package com.example.cararchives;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cararchives.Interface.ItemTouchHelperAdapter;
import com.example.cararchives.Model.DataItem;
import com.example.cararchives.database.DBHelper;
import com.example.cararchives.database.DataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder>  {

    public static final String ITEM_ID_KEY = "item_id_key";
    public static final String ITEM_KEY = "item_key";
    private List<DataItem> mItems;
    private Context mContext;
    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener;
    boolean Imagetype ;
    public static Cursor mcursor ;

    public DataItemAdapter(Context context, List<DataItem> items,boolean imageType) {
        this.mContext = context;
        this.mItems = items;
        this.Imagetype = imageType;
    }

    @NonNull
    @Override
    public DataItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(mContext);
        prefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                  String key) {
                Log.i("preferences", "onSharedPreferenceChanged: " + key);
            }
        };
        settings.registerOnSharedPreferenceChangeListener(prefsListener);

        boolean grid = settings.getBoolean(
                mContext.getString(R.string.pref_display_grid), false);
        int layoutId = grid ? R.layout.grid_item : R.layout.list_item;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DataItemAdapter.ViewHolder holder, int position) {
        final DataItem item = mItems.get(position);
        if (!mcursor.moveToPosition(position)) {
            return;
        }
        long id = mcursor.getLong(mcursor.getColumnIndex(DBHelper.COLUMN_ID));

        try {
                  holder.tvName.setText(item.getItemName());

                      String imageFile = item.getImage();
                      InputStream inputStream = mContext.getAssets().open(imageFile);
                      Drawable d = Drawable.createFromStream(inputStream, null);
                      holder.imageView.setImageDrawable(d);
                      holder.mView.setTag(id);

              } catch (IOException e) {
                  e.printStackTrace();
              }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(ITEM_KEY, item);
                mContext.startActivity(intent);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "You long clicked " + item.getItemName(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

         TextView tvName;
        ImageView imageView;
        View mView;
         ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.itemNameText);
            imageView = itemView.findViewById(R.id.imageView);
            mView = itemView;
        }
    }
}