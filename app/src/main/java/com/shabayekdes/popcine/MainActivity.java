package com.shabayekdes.popcine;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shabayekdes.popcine.adapters.MoviesAdapter;
import com.shabayekdes.popcine.loaders.FavoritesLoader;
import com.shabayekdes.popcine.loaders.MoviesLoader;
import com.shabayekdes.popcine.models.Movie;
import com.shabayekdes.popcine.utilities.NetworkUtils;
import com.shabayekdes.popcine.utilities.PopCineUtilities;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements
        MoviesAdapter.MoviesAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks,
        TabLayout.OnTabSelectedListener {

    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private TabLayout sort_type_tab_layout;

    public final static String SORT_TYPE_KEY = "sort_type";
    public final static String MOVIE_KEY = "movie";
    public final static String MOVIE_BUNDLE_KEY = "movie_bundle";
    private static final String CURRENT_TAB_POSITION_KEY = "currentTabPosition";
    private static final String CURRENT_TAB_MOVIES_KEY = "currentTabMovies";
    private static int currentTabPosition;

    private static int CURRENT_LOADER_ID;

    private final static String IS_SAVED_INSTANCE_STATE_Key = "isSavingInstanceState";
    private static boolean isSavingInstanceState;

    private final static String MOVIES_RECYCLER_STATE_KEY = "savedRecyclerPosition";
    private static Parcelable moviesRecyclerState;

    private static int favoritesTabPosition;
    private static List<Movie> movies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mRecyclerView = findViewById(R.id.movies_rv);

        int gridColumns = PopCineUtilities.calculateNoOfColumns(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, gridColumns);


        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this, this);

        mRecyclerView.setAdapter(mMoviesAdapter);

        sort_type_tab_layout = findViewById(R.id.sort_type_tab_layout);
        favoritesTabPosition = sort_type_tab_layout.getTabAt(sort_type_tab_layout.getTabCount() - 1).getPosition();
        sort_type_tab_layout.addOnTabSelectedListener(this);
        if (savedInstanceState == null) {
            isSavingInstanceState = false;
            if (!NetworkUtils.isConnected(this)) {
                currentTabPosition = favoritesTabPosition;
            }
            sort_type_tab_layout.getTabAt(currentTabPosition).select();
        }
    }

    private void loadMovies(String sortType) {
        showMoviesDataView();

        Bundle sortTypeBundle = new Bundle();
        sortTypeBundle.putString(SORT_TYPE_KEY, sortType);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader loader = loaderManager.getLoader(CURRENT_LOADER_ID);
        if (loader == null) {
            loaderManager.initLoader(CURRENT_LOADER_ID, sortTypeBundle, this);
        } else {
            loaderManager.restartLoader(CURRENT_LOADER_ID, sortTypeBundle, this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_SAVED_INSTANCE_STATE_Key, true);
        currentTabPosition = sort_type_tab_layout.getSelectedTabPosition();
        outState.putInt(CURRENT_TAB_POSITION_KEY, currentTabPosition);
        outState.putParcelableArrayList(CURRENT_TAB_MOVIES_KEY, (ArrayList<? extends Parcelable>) movies);
        moviesRecyclerState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(MOVIES_RECYCLER_STATE_KEY, moviesRecyclerState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isSavingInstanceState = savedInstanceState.getBoolean(IS_SAVED_INSTANCE_STATE_Key);
        currentTabPosition = savedInstanceState.getInt(CURRENT_TAB_POSITION_KEY);
        sort_type_tab_layout.getTabAt(currentTabPosition).select();
        movies = savedInstanceState.getParcelableArrayList(CURRENT_TAB_MOVIES_KEY);
        mMoviesAdapter.setMovies(movies);
        moviesRecyclerState = savedInstanceState.getParcelable(MOVIES_RECYCLER_STATE_KEY);
        mRecyclerView.getLayoutManager().onRestoreInstanceState(moviesRecyclerState);
    }

    private void showMoviesDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movies data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClickHandler(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(MOVIE_KEY, movie);
        intent.putExtra(MOVIE_BUNDLE_KEY, bundle);
        startActivity(intent);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        if (id == favoritesTabPosition) {
            return new FavoritesLoader(this);
        } else {
            return new MoviesLoader(this, args);
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null) {
            showMoviesDataView();
            if (loader instanceof MoviesLoader) {
                movies = (List<Movie>) data;
            } else if (loader instanceof FavoritesLoader) {
                movies = PopCineUtilities.getMoviesFromCursor((Cursor) data);
                if (movies.size() == 0) {
                    showErrorMessage();
                    mErrorMessageDisplay.setText(R.string.no_favorite_movies);
                }
            }
            mMoviesAdapter.setMovies(movies);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mMoviesAdapter.setMovies(null);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        setParameters(tab);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        setParameters(tab);
    }

    private void setParameters(TabLayout.Tab tab) {
        if (!isSavingInstanceState) {
            String sortType = tab.getText().toString().replace(" ", "_").toLowerCase();
            mMoviesAdapter.setMovies(null);
            CURRENT_LOADER_ID = tab.getPosition();
            loadMovies(sortType);
        }
        isSavingInstanceState = false;
    }
}