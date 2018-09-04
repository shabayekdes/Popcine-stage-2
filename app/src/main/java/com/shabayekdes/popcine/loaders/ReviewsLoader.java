package com.shabayekdes.popcine.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.shabayekdes.popcine.DetailsActivity;
import com.shabayekdes.popcine.models.Review;
import com.shabayekdes.popcine.utilities.NetworkUtils;
import com.shabayekdes.popcine.utilities.OpenJsonUtils;

import java.net.URL;
import java.util.List;

public class ReviewsLoader extends AsyncTaskLoader<List<Review>> {

    List<Review> reviews = null;
    Bundle args;

    public ReviewsLoader(Context context, Bundle args) {
        super(context);
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (args == null) {
            return;
        }
        if (reviews != null) {
            deliverResult(reviews);
        } else {
            forceLoad();
        }

    }

    @Override
    public List<Review> loadInBackground() {
        String reviewsPath = args.getString(DetailsActivity.REVIEWS_KEY);
        String movieId = args.getString(DetailsActivity.MOVIE_ID_KEY);
        URL url = NetworkUtils.buildUrl(movieId, reviewsPath);

        try {
            String reviewsJsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
            reviews = OpenJsonUtils.getSimpleReviewsData(reviewsJsonResponse);
            return reviews;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public void deliverResult(List<Review> data) {
        reviews = data;
        super.deliverResult(data);
    }
}
