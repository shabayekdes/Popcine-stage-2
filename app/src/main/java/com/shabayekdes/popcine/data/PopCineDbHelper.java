package com.shabayekdes.popcine.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shabayekdes.popcine.data.PopCineContract.FavoriteMovieEntry;

public class PopCineDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Popcine.db";

    private static final int DATABASE_VERSION = 1;

    public PopCineDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_FAVORITES_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS " + FavoriteMovieEntry.TABLE_NAME
                + "("
                + FavoriteMovieEntry.ID_COLUMN + " INTEGER  PRIMARY KEY, "
                + FavoriteMovieEntry.TITLE_COLUMN + " TEXT, "
                + FavoriteMovieEntry.VOTE_AVERAGE_COLUMN + " TEXT, "
                + FavoriteMovieEntry.OVERVIEW_COLUMN + " TEXT, "
                + FavoriteMovieEntry.RELEASE_DATE_COLUMN + " TEXT, "
                + FavoriteMovieEntry.POSTER_PATH_COLUMN + " TEXT"
                + ");";
        sqLiteDatabase.execSQL(CREATE_FAVORITES_TABLE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
