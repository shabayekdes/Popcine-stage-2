package com.shabayekdes.popcine.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.shabayekdes.popcine.DetailsActivity;
import com.shabayekdes.popcine.models.Trailer;
import com.shabayekdes.popcine.utilities.NetworkUtils;
import com.shabayekdes.popcine.utilities.OpenJsonUtils;

import java.net.URL;
import java.util.List;

public class TrailersLoader extends AsyncTaskLoader<List<Trailer>> {

    List<Trailer> trailers = null;
    Bundle args;

    public TrailersLoader(Context context, Bundle args) {
        super(context);
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (args == null) {
            return;
        }
        if (trailers != null) {
            deliverResult(trailers);
        } else {
            forceLoad();
        }

    }

    @Override
    public List<Trailer> loadInBackground() {
        String trailersPath = args.getString(DetailsActivity.TRAILERS_KEY);
        String movieId = args.getString(DetailsActivity.MOVIE_ID_KEY);
        URL url = NetworkUtils.buildUrl(movieId, trailersPath);

        try {
            String reviewsJsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
            trailers = OpenJsonUtils.getSimpleTrailersData(reviewsJsonResponse);
            return trailers;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trailers;
    }

    @Override
    public void deliverResult(List<Trailer> data) {
        trailers = data;
        super.deliverResult(data);
    }
}
