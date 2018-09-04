package com.shabayekdes.popcine.utilities;


import com.shabayekdes.popcine.models.Movie;
import com.shabayekdes.popcine.models.Review;
import com.shabayekdes.popcine.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class OpenJsonUtils {

    public static List<Movie> getSimpleMoviesData(String moviesJsonResponse)
            throws JSONException {
        final String MOVIES_RESULTS_LIST = "results";

        final String ERROR_MESSAGE_CODE = "code";
        final String POSTER_PATH = "poster_path";
        final String ID = "id";
        final String VOTE_AVERAGE = "vote_average";
        final String TITLE = "title";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";

        JSONObject moviesDataObject = new JSONObject(moviesJsonResponse);
        if (moviesDataObject.has(ERROR_MESSAGE_CODE)) {
            int errorCode = moviesDataObject.getInt(ERROR_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray moviesArray = moviesDataObject.getJSONArray(MOVIES_RESULTS_LIST);

        List<Movie> parsedMoviesData = new ArrayList<>();
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieObject = moviesArray.getJSONObject(i);

            int id = movieObject.optInt(ID);
            String title = movieObject.optString(TITLE);
            String voteAverage = movieObject.optString(VOTE_AVERAGE);
            String posterPath = movieObject.optString(POSTER_PATH);
            String overview = movieObject.optString(OVERVIEW);
            String releaseDate = movieObject.optString(RELEASE_DATE);

            Movie movie = new Movie();
            movie.setId(id);
            movie.setTitle(title);
            movie.setVoteAverage(voteAverage);
            movie.setPosterPath(posterPath);
            movie.setOverview(overview);
            movie.setReleaseDate(releaseDate);

            parsedMoviesData.add(movie);
        }


        return parsedMoviesData;
    }
    public static List<Review> getSimpleReviewsData(String reviewsJsonResponse)
            throws JSONException {
        final String REVIEWS_RESULTS_LIST = "results";

        final String ERROR_MESSAGE_CODE = "code";

        final String ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";

        JSONObject reviewsDataObject = new JSONObject(reviewsJsonResponse);
        if (reviewsDataObject.has(ERROR_MESSAGE_CODE)) {
            int errorCode = reviewsDataObject.getInt(ERROR_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray reviewsArray = reviewsDataObject.getJSONArray(REVIEWS_RESULTS_LIST);

        List<Review> parsedReviewsData = new ArrayList<>();
        for (int i = 0; i < reviewsArray.length(); i++) {
            JSONObject reviewObject = reviewsArray.getJSONObject(i);

            int id = reviewObject.optInt(ID);
            String content = reviewObject.optString(CONTENT);
            String author = reviewObject.optString(AUTHOR);

            Review review = new Review();
            review.setId(id);
            review.setAuthor(author);
            review.setContent(content);

            parsedReviewsData.add(review);
        }


        return parsedReviewsData;
    }
    public static List<Trailer> getSimpleTrailersData(String reviewsJsonResponse)
            throws JSONException {
        final String REVIEWS_RESULTS_LIST = "results";

        final String ERROR_MESSAGE_CODE = "code";

        final String ID = "id";
        final String KEY = "key";
        final String NAME = "name";

        JSONObject trailersDataObject = new JSONObject(reviewsJsonResponse);
        if (trailersDataObject.has(ERROR_MESSAGE_CODE)) {
            int errorCode = trailersDataObject.getInt(ERROR_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray trailersArray = trailersDataObject.getJSONArray(REVIEWS_RESULTS_LIST);

        List<Trailer> parsedTrailersData = new ArrayList<>();
        for (int i = 0; i < trailersArray.length(); i++) {
            JSONObject trailerObject = trailersArray.getJSONObject(i);

            int id = trailerObject.optInt(ID);
            String name = trailerObject.optString(NAME);
            String key = trailerObject.optString(KEY);

            Trailer trailer = new Trailer();
            trailer.setId(id);
            trailer.setKey(key);
            trailer.setName(name);

            parsedTrailersData.add(trailer);
        }


        return parsedTrailersData;
    }

}
