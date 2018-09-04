package com.shabayekdes.popcine.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.shabayekdes.popcine.MainActivity;
import com.shabayekdes.popcine.models.Movie;
import com.shabayekdes.popcine.utilities.NetworkUtils;
import com.shabayekdes.popcine.utilities.OpenJsonUtils;

import java.net.URL;
import java.util.List;

public class MoviesLoader extends AsyncTaskLoader<List<Movie>> {

    List<Movie> movies = null;
    Bundle args;

    public MoviesLoader(Context context, Bundle args) {
        super(context);
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (args == null) {
            return;
        }
        if (movies != null) {
            deliverResult(movies);
        } else {
            forceLoad();
        }

    }

    @Override
    public List<Movie> loadInBackground() {
        String sortType = args.getString(MainActivity.SORT_TYPE_KEY);
        URL url = NetworkUtils.buildUrl(sortType);

        try {
            String moviesJsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
            movies = OpenJsonUtils.getSimpleMoviesData(moviesJsonResponse);
            return movies;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    @Override
    public void deliverResult(List<Movie> data) {
        movies = data;
        super.deliverResult(data);
    }
}
