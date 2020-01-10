package com.example.android.fueltracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.fueltracker.data.UserContract;

public class UserDbHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Users.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA = ", ";
    private static final String NOT_NULL = " NOT NULL";

    private static final String CREATE_DETAILS_TABLE =
            "CREATE TABLE " + UserContract.GasEntry.TABLE_NAME + "("
            + UserContract.GasEntry.COLUMN_ID + INT_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA
            + UserContract.GasEntry.COLUMN_STATION + TEXT_TYPE + NOT_NULL + COMMA
            + UserContract.GasEntry.COLUMN_PRICE + REAL_TYPE + NOT_NULL +COMMA
            + UserContract.GasEntry.COLUMN_GALLONS + REAL_TYPE + NOT_NULL +COMMA
            + UserContract.GasEntry.COLUMN_SPENT + REAL_TYPE + NOT_NULL +COMMA
            + UserContract.GasEntry.COLUMN_DATE + TEXT_TYPE + NOT_NULL + ")";

    private static final String CREATE_FAVORITES_TABLE =
            "CREATE TABLE " + UserContract.FavoriteEntry.TABLE_NAME + "("
            + UserContract.FavoriteEntry.COLUMN_ID + INT_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA
            + UserContract.FavoriteEntry.COLUMN_STATION + TEXT_TYPE + NOT_NULL + COMMA
            + UserContract.FavoriteEntry.COLUMN_ADDRESS + TEXT_TYPE + NOT_NULL + COMMA
            + UserContract.FavoriteEntry.COLUMN_CITY + TEXT_TYPE + NOT_NULL + COMMA
            + UserContract.FavoriteEntry.COLUMN_STATION_ID + TEXT_TYPE + NOT_NULL + ")";

    private static final String DROP_DETAILS_TABLE = "DROP TABLE IF EXISTS " + UserContract.GasEntry.TABLE_NAME;
    private static final String DROP_FAVORITES_TABLE = "DROP TABLE IF EXISTS " + UserContract.FavoriteEntry.TABLE_NAME;

    public UserDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_DETAILS_TABLE);
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DROP_DETAILS_TABLE);
        db.execSQL(DROP_FAVORITES_TABLE);
        onCreate(db);
    }
}
