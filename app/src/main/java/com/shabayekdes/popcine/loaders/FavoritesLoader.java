package com.shabayekdes.popcine.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.shabayekdes.popcine.data.PopCineContract;

public class FavoritesLoader extends CursorLoader {
    Cursor cursor;

    public FavoritesLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        cursor = getContext().getContentResolver().query(PopCineContract.FavoriteMovieEntry.CONTENT_URI, null, null, null, null);
        return cursor;
    }

    @Override
    public void deliverResult(Cursor cursor) {
        this.cursor = cursor;
        super.deliverResult(cursor);
    }
}
