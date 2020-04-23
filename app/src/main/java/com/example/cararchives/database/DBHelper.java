package com.example.cararchives.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.Year;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ID = "itemId";
    public static final String COLUMN_NAME = "itemName";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_POSITION = "sortPosition";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE = "image";
    public static final String YEAR = "year";
    public static final String COLOR = "color";
    public static final String VIN = "vin";

    public static final String[] ALL_COLUMNS =
            {COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION,
                    COLUMN_CATEGORY, COLUMN_POSITION, COLUMN_PRICE, COLUMN_IMAGE, YEAR,COLOR,VIN};

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_ITEMS + "(" +
                    COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_CATEGORY + " TEXT," +
                    COLUMN_POSITION + " INTEGER," +
                    COLUMN_PRICE + " REAL," +
                    COLUMN_IMAGE + " TEXT," +
                    YEAR + " TEXT," +
                    COLOR + " TEXT," +
                    VIN + " TEXT );";

    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_ITEMS;


    public static final String DB_FILE_NAME = "cars.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {

        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( SQL_DELETE);
        onCreate(db);
    }
}
