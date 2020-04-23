package com.example.cararchives.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cararchives.DataItemAdapter;
import com.example.cararchives.Model.DataItem;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

   // DataItem dataItem = new DataItem();
    private Context mContext;
    private SQLiteDatabase mDatabase;
    SQLiteOpenHelper mDbHelper;

    public DataSource(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void open() {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public DataItem createItem(DataItem item) {
        ContentValues values = item.toValues();
        mDatabase.insert(DBHelper.TABLE_ITEMS, null, values);
        return item;
    }

    public long getDataItemsCount() {
        return DatabaseUtils.queryNumEntries(mDatabase, DBHelper.TABLE_ITEMS);
    }

    public void seedDatabase(List<DataItem> dataItemList) {
       // long numItems = getDataItemsCount();
            for (DataItem item :
                    dataItemList) {
                try {
                    createItem(item);
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
            }

    }

    public List<DataItem> getAllItems() {
        List<DataItem> dataItems = new ArrayList<>();
        Cursor cursor = mDatabase.query(DBHelper.TABLE_ITEMS, DBHelper.ALL_COLUMNS,
                null, null, null, null, null);
    DataItemAdapter.mcursor =  getCursor(cursor);
        while (cursor.moveToNext()) {
            DataItem item = new DataItem();
            item.setItemId(cursor.getString(
                    cursor.getColumnIndex(DBHelper.COLUMN_ID)));
            item.setItemName(cursor.getString(
                    cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
            item.setDescription(cursor.getString(
                    cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION)));
            item.setCategory(cursor.getString(
                    cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY)));
            item.setSortPosition(cursor.getInt(
                    cursor.getColumnIndex(DBHelper.COLUMN_ID)));
            item.setPrice(cursor.getDouble(
                    cursor.getColumnIndex(DBHelper.COLUMN_PRICE)));
            item.setImage(cursor.getString(
                    cursor.getColumnIndex(DBHelper.COLUMN_IMAGE)));
            item.setColor(cursor.getString(
                    cursor.getColumnIndex(DBHelper.COLOR)));
            item.setYear(cursor.getString(
                    cursor.getColumnIndex(DBHelper.YEAR)));
            item.setVin(cursor.getString(
                    cursor.getColumnIndex(DBHelper.VIN)));
            dataItems.add(item);
        }

        return dataItems;
    }


    public int update(String itemId, String itemName, String description, String category, int sortPosition, double price, String image, String color, String year, String vin ) {
        DataItem item = new DataItem(itemId, itemName, description,  category, sortPosition, price,  image, color, year,  vin);
        ContentValues values = item.toValues();
        return mDatabase.update(DBHelper.TABLE_ITEMS, values, DBHelper.COLUMN_ID + " = " + itemId, null);
    }
   public Cursor getCursor(Cursor cursor){
        return cursor;
    }

    public void delete(long _id) {
        mDatabase.delete(DBHelper.TABLE_ITEMS, DBHelper.COLUMN_ID + "=" + _id, null);
    }
}
