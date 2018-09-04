package com.shabayekdes.popcine.data;

import android.net.Uri;

public class PopCineContract {
    public static final String CONTENT_AUTHORITY = "com.shabayekdes.popcine";
    public static final String PATH_FAVORITES = "favorites";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static class FavoriteMovieEntry {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();
        public static final String ID_COLUMN = "id";
        public static final String TABLE_NAME = PATH_FAVORITES;
        public static final String TITLE_COLUMN = "title";
        public static final String VOTE_AVERAGE_COLUMN = "vote_average";
        public static final String OVERVIEW_COLUMN = "overview";
        public static final String RELEASE_DATE_COLUMN = "release_date";
        public static final String POSTER_PATH_COLUMN = "poster_path";

    }
}
