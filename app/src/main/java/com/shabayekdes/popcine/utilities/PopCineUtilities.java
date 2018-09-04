package com.shabayekdes.popcine.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.DisplayMetrics;

import com.shabayekdes.popcine.data.PopCineContract.FavoriteMovieEntry;
import com.shabayekdes.popcine.models.Movie;

import java.util.ArrayList;
import java.util.List;


public class PopCineUtilities {
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
    public static List<Movie> getMoviesFromCursor(Cursor cursor){
        List<Movie> movies = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(FavoriteMovieEntry.ID_COLUMN));
             String title = cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.TITLE_COLUMN));
             String vote_average = cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.VOTE_AVERAGE_COLUMN));
             String overview = cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.OVERVIEW_COLUMN));
             String release_date = cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.RELEASE_DATE_COLUMN));
             String poster_path = cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.POSTER_PATH_COLUMN));

             Movie movie = new Movie();
             movie.setId(id);
             movie.setTitle(title);
             movie.setVoteAverage(vote_average);
             movie.setOverview(overview);
             movie.setReleaseDate(release_date);
             movie.setPosterPath(poster_path);
             movies.add(movie);
        }

        return movies;
    }

    public static ContentValues movieToContentValues(Movie movie){
        ContentValues values = new ContentValues();
        values.put(FavoriteMovieEntry.ID_COLUMN,movie.getId());
        values.put(FavoriteMovieEntry.TITLE_COLUMN, movie.getTitle());
        values.put(FavoriteMovieEntry.VOTE_AVERAGE_COLUMN, movie.getVoteAverage());
        values.put(FavoriteMovieEntry.OVERVIEW_COLUMN, movie.getOverview());
        values.put(FavoriteMovieEntry.RELEASE_DATE_COLUMN, movie.getReleaseDate());
        values.put(FavoriteMovieEntry.POSTER_PATH_COLUMN, movie.getPosterPath());
        return values;
    }
}
