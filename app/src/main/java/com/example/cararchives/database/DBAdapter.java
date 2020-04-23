package com.example.cararchives.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
    Context c;
    SQLiteDatabase db;
    DBHelper helper;

    public DBAdapter(Context ctx)
    {
        this.c=ctx;
        helper=new DBHelper(c);
    }

    //OPEN DB
    public DBAdapter openDB()
    {
        try
        {
            db=helper.getWritableDatabase();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return this;
    }

    //CLOSE
    public void close()
    {
        try
        {
            helper.close();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    //INSERT DATA TO DB
    public long add(String itemId, String itemName, String description, String category, int sortPosition, double price, String image, String color, String year, String vin)
    {
        try
        {
            ContentValues values=new ContentValues();
            values.put(DBHelper.COLUMN_NAME, itemName);
            values.put(DBHelper.COLUMN_CATEGORY, category);
            values.put(DBHelper.COLUMN_DESCRIPTION, description);
            values.put(DBHelper.COLUMN_POSITION, sortPosition);
            values.put(DBHelper.COLUMN_PRICE, price);
            values.put(DBHelper.COLUMN_IMAGE, image);
            values.put(DBHelper.COLOR, color);
            values.put(DBHelper.YEAR, year);
            values.put(DBHelper.VIN, vin);

            return db.insert(DBHelper.TABLE_ITEMS,itemId,values);

        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    //RETRIEVE ALL PLAYERS
    public Cursor getAllPlayers()
    {
       // String[] columns={Contants.ROW_ID,Contants.NAME,Contants.POSITION};

        return db.query(DBHelper.TABLE_ITEMS,DBHelper.ALL_COLUMNS,null,null,null,null,null);
    }

    //UPDATE
    public long UPDATE(String itemId, String itemName, String description, String category, int sortPosition, double price, String image, String color, String year, String vin)
    {
        try
        {
            ContentValues values=new ContentValues();
            values.put(DBHelper.COLUMN_NAME, itemName);
            values.put(DBHelper.COLUMN_CATEGORY, category);
            values.put(DBHelper.COLUMN_DESCRIPTION, description);
            values.put(DBHelper.COLUMN_POSITION, sortPosition);
            values.put(DBHelper.COLUMN_PRICE, price);
            values.put(DBHelper.COLUMN_IMAGE, image);
            values.put(DBHelper.COLOR, color);
            values.put(DBHelper.YEAR, year);
            values.put(DBHelper.VIN, vin);

            return db.update(DBHelper.TABLE_ITEMS, values,DBHelper.COLUMN_ID+" =?",new String[]{String.valueOf(itemId)});

        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    //DELETE
    public long Delete(int id)
    {
        try
        {

            return db.delete(DBHelper.TABLE_ITEMS,DBHelper.COLUMN_ID+" =?",new String[]{String.valueOf(id)});

        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

}
