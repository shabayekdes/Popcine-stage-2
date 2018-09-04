package com.shabayekdes.popcine.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.shabayekdes.popcine.data.PopCineContract.FavoriteMovieEntry;

public class PopCineProvider extends ContentProvider {
    private static final String LOG_TAG = PopCineProvider.class.getSimpleName();
    public static final int CODE_FAVORITES = 100;
    public static final int CODE_SINGLE_FAVORITE = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private PopCineDbHelper dbHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopCineContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, PopCineContract.PATH_FAVORITES, CODE_FAVORITES);
        matcher.addURI(authority, PopCineContract.PATH_FAVORITES + "/#", CODE_SINGLE_FAVORITE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new PopCineDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String s1) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case CODE_FAVORITES:
                cursor = database.query(FavoriteMovieEntry.TABLE_NAME, null, null, null, null, null, null);
                break;
            case CODE_SINGLE_FAVORITE:
                selectionArgs = new String[]{uri.getLastPathSegment()};
                cursor = database.query(FavoriteMovieEntry.TABLE_NAME, null, FavoriteMovieEntry.ID_COLUMN + " = ? ", selectionArgs, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new IllegalArgumentException("getType is not supported for current version");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case CODE_FAVORITES:
                long id = database.insert(FavoriteMovieEntry.TABLE_NAME, null, contentValues);
                if (id == -1) {
                    Log.e(LOG_TAG, "Failed to insert row for :" + uri);
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowDeleted;
        switch (uriMatcher.match(uri)){
            case CODE_SINGLE_FAVORITE:
                selection = FavoriteMovieEntry.ID_COLUMN + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                rowDeleted = database.delete(FavoriteMovieEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("delete is not supported for :" + uri);
        }
        if (rowDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new IllegalArgumentException("update is not supported for current version");
    }
}
