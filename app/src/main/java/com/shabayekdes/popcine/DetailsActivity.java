package com.shabayekdes.popcine;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.shabayekdes.popcine.adapters.ReviewsAdapter;
import com.shabayekdes.popcine.adapters.TrailersAdapter;
import com.shabayekdes.popcine.data.PopCineContract.FavoriteMovieEntry;
import com.shabayekdes.popcine.loaders.ReviewsLoader;
import com.shabayekdes.popcine.loaders.TrailersLoader;
import com.shabayekdes.popcine.models.Movie;
import com.shabayekdes.popcine.models.Review;
import com.shabayekdes.popcine.models.Trailer;
import com.shabayekdes.popcine.utilities.NetworkUtils;
import com.shabayekdes.popcine.utilities.PopCineUtilities;

import java.util.List;

public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks,
        TrailersAdapter.TrailerClickHandler {

    ImageView poster_iv;
    TextView overview_tv;
    TextView release_date_tv;
    TextView vote_average_tv;
    boolean isFavorite;
    static Movie movie;
    FloatingActionButton favoritesButton;

    RecyclerView trailers_rv;
    TrailersAdapter trailersAdapter;

    RecyclerView reviews_rv;
    ReviewsAdapter reviewsAdapter;

    public final static String TRAILERS_KEY = "trailers";
    public final static String REVIEWS_KEY = "reviews";
    public final static String MOVIE_ID_KEY = "movieId";

    public final static String TRAILERS = "videos";
    public final static String REVIEWS = "reviews";

    public final static int TRAILERS_LOADER_ID = 13;
    public final static int REVIEWS_LOADER_ID = 14;

    private final static String REVIEWS_RECYCLER_STATE_KEY = "savedReviewRecyclerPosition";
    private static Parcelable reviewsRecyclerState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        poster_iv = findViewById(R.id.poster_iv);
        overview_tv = findViewById(R.id.overview_tv);
        release_date_tv = findViewById(R.id.release_date_tv);
        vote_average_tv = findViewById(R.id.vote_average_tv);
        favoritesButton = findViewById(R.id.favorites_button);
        trailers_rv = findViewById(R.id.trailers_rv);
        reviews_rv = findViewById(R.id.reviews_rv);

        trailersAdapter = new TrailersAdapter(this, this);
        trailers_rv.setHasFixedSize(true);
        trailers_rv.setAdapter(trailersAdapter);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviews_rv.setLayoutManager(layoutManager);

        reviews_rv.setHasFixedSize(true);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle movieBundle = intent.getBundleExtra(MainActivity.MOVIE_BUNDLE_KEY);
            movie = movieBundle.getParcelable(MainActivity.MOVIE_KEY);
        }

        populateBasicInfo(movie);
        loadTrailers(movie);
        loadReviews(movie);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MainActivity.MOVIE_KEY, movie);
        reviewsRecyclerState = reviews_rv.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(REVIEWS_RECYCLER_STATE_KEY , reviewsRecyclerState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movie = savedInstanceState.getParcelable(MainActivity.MOVIE_KEY);
        populateBasicInfo(movie);
        loadTrailers(movie);


        reviewsRecyclerState = savedInstanceState.getParcelable(REVIEWS_RECYCLER_STATE_KEY);
        reviews_rv.getLayoutManager().onRestoreInstanceState(reviewsRecyclerState);

    }


    private void populateBasicInfo(Movie movie) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        isFavorite = isFavorite(movie);
        String title = movie.getTitle();
        actionBar.setTitle(title);



        if (isFavorite) {
            favoritesButton.setImageResource(R.drawable.ic_favorite_heart);
        } else {
            favoritesButton.setImageResource(R.drawable.ic_unfavorite_heart);
        }

        String voteAverage = movie.getVoteAverage();
        String posterPath = movie.getPosterPath();
        String overview = movie.getOverview();
        String releaseDate = movie.getReleaseDate();

        String screenWidth = getString(R.string.screen_width_photo);
        String posterUrl = NetworkUtils.postersUrlAuthority + screenWidth + posterPath;

        Picasso.with(this)
                .load(posterUrl)
                .into(poster_iv);

        vote_average_tv.setText(voteAverage);
        overview_tv.setText(overview);
        release_date_tv.setText(releaseDate);
    }

    private void loadTrailers(Movie movie) {
        String id = String.valueOf(movie.getId());

        Bundle trailersBundle = new Bundle();
        trailersBundle.putString(MOVIE_ID_KEY, id);
        trailersBundle.putString(TRAILERS_KEY, TRAILERS);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<Trailer>> loader = loaderManager.getLoader(TRAILERS_LOADER_ID);
        if (loader == null) {
            loaderManager.initLoader(TRAILERS_LOADER_ID, trailersBundle, this);
        } else {
            loaderManager.restartLoader(TRAILERS_LOADER_ID, trailersBundle, this);
        }
    }

    private void loadReviews(Movie movie) {
        String id = String.valueOf(movie.getId());

        Bundle reviewsBundle = new Bundle();
        reviewsBundle.putString(MOVIE_ID_KEY, id);
        reviewsBundle.putString(REVIEWS_KEY, REVIEWS);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<Review>> loader = loaderManager.getLoader(REVIEWS_LOADER_ID);
        if (loader == null) {
            loaderManager.initLoader(REVIEWS_LOADER_ID, reviewsBundle, this);
        } else {
            loaderManager.restartLoader(REVIEWS_LOADER_ID, reviewsBundle, this);
        }
    }

    public void favorite(View view) {
        if (isFavorite) {
            String id = String.valueOf(movie.getId());
            Uri uri = FavoriteMovieEntry.CONTENT_URI.buildUpon().appendEncodedPath(id).build();
            getContentResolver().delete(uri, null, null);
            isFavorite = false;
            favoritesButton.setImageResource(R.drawable.ic_unfavorite_heart);
        } else {
            ContentValues values = PopCineUtilities.movieToContentValues(movie);
            getContentResolver().insert(FavoriteMovieEntry.CONTENT_URI, values);
            isFavorite = true;
            favoritesButton.setImageResource(R.drawable.ic_favorite_heart);
        }
    }

    private boolean isFavorite(Movie movie) {
        String id = String.valueOf(movie.getId());
        Uri uri = FavoriteMovieEntry.CONTENT_URI.buildUpon().appendEncodedPath(id).build();
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case TRAILERS_LOADER_ID:
                return new TrailersLoader(this, args);
            case REVIEWS_LOADER_ID:
                return new ReviewsLoader(this, args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case TRAILERS_LOADER_ID:
                List<Trailer> trailers = (List<Trailer>) data;
                trailersAdapter.setTrailers(trailers);
                break;
            case REVIEWS_LOADER_ID:
                List<Review> reviews = (List<Review>) data;
                if (reviews != null && reviews.size() > 0) {
                    reviewsAdapter = new ReviewsAdapter();
                    reviews_rv.setAdapter(reviewsAdapter);
                    reviewsAdapter.setReviews(reviews);
                } else {
                    reviews_rv.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onClickHandler(Trailer trailer) {
        Uri videoUrl = NetworkUtils.youTubeVideoUrlBuilder(trailer);
        Intent intent = new Intent(Intent.ACTION_VIEW, videoUrl);
        startActivity(intent);
    }
}
