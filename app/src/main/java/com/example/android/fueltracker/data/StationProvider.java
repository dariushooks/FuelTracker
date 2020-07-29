package com.example.android.fueltracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

public class StationProvider extends ContentProvider
{
    private static final int DETAILS = 0;
    private static final int DETAILS_ID = 1;
    private static final int FAVORITES = 2;
    private static final int FAVORITES_ID = 3;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private UserDbHelper dbHelper;

    static
    {
        uriMatcher.addURI(UserContract.CONTENT_AUTHORITY, UserContract.PATH_DETAILS, DETAILS);
        uriMatcher.addURI(UserContract.CONTENT_AUTHORITY, UserContract.PATH_DETAILS + "/#", DETAILS_ID);
        uriMatcher.addURI(UserContract.CONTENT_AUTHORITY, UserContract.PATH_FAVORITES, FAVORITES);
        uriMatcher.addURI(UserContract.CONTENT_AUTHORITY, UserContract.PATH_FAVORITES + "/*", FAVORITES_ID);
    }


    @Override
    public boolean onCreate()
    {
        dbHelper = new UserDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);

        switch(match)
        {
            case DETAILS:
                cursor = database.query(UserContract.GasEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case DETAILS_ID:
                selection = UserContract.GasEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(UserContract.GasEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case FAVORITES:
                cursor = database.query(UserContract.FavoriteEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case FAVORITES_ID:
                selection = UserContract.FavoriteEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(UserContract.FavoriteEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default: throw new IllegalArgumentException("CANNOT QUERY UNKNOWN URI " + uri);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri)
    {
        final int match = uriMatcher.match(uri);
        switch (match)
        {
            case DETAILS: return UserContract.GasEntry.CONTENT_LIST_TYPE;
            case DETAILS_ID: return UserContract.GasEntry.CONTENT_ITEM_TYPE;
            case FAVORITES: return UserContract.FavoriteEntry.CONTENT_LIST_TYPE;
            case FAVORITES_ID: return UserContract.FavoriteEntry.CONTENT_ITEM_TYPE;
            default: throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        long newRowID;

        switch(match)
        {
            case DETAILS:
                newRowID = database.insert(UserContract.GasEntry.TABLE_NAME, null, contentValues);
                break;

            case FAVORITES:
                newRowID = database.insert(UserContract.FavoriteEntry.TABLE_NAME, null, contentValues);
                break;

            default: throw new IllegalArgumentException("INSERTION IS NOT SUPPORTED FOR " + uri);
        }
        return ContentUris.withAppendedId(uri, newRowID);
    }

    @Override
    public int delete(Uri uri, String where, String[] strings)
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        switch (match)
        {
            case DETAILS:
                return database.delete(UserContract.GasEntry.TABLE_NAME, where, strings);
            case DETAILS_ID:
                where = UserContract.GasEntry.COLUMN_ID + "=?";
                return database.delete(UserContract.GasEntry.TABLE_NAME, where, strings);
            case FAVORITES:
                return database.delete(UserContract.FavoriteEntry.TABLE_NAME, where, strings);
            case FAVORITES_ID:
                where = UserContract.FavoriteEntry.COLUMN_ADDRESS + "=?";
                return database.delete(UserContract.FavoriteEntry.TABLE_NAME, where, strings);
        }

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String where, String[] strings)
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        switch (match)
        {
            case DETAILS:
                return database.update(UserContract.GasEntry.TABLE_NAME, contentValues, where, strings);
            case DETAILS_ID:
                where = UserContract.GasEntry.COLUMN_ID + "=?";
                return database.update(UserContract.GasEntry.TABLE_NAME, contentValues, where, strings);
            case FAVORITES:
                return database.update(UserContract.FavoriteEntry.TABLE_NAME, contentValues, where, strings);
            case FAVORITES_ID:
                where = UserContract.FavoriteEntry.COLUMN_ID + "=?";
                return database.update(UserContract.FavoriteEntry.TABLE_NAME, contentValues, where, strings);
        }

        return 0;
    }
}
