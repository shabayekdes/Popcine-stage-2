package com.shabayekdes.popcine.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.shabayekdes.popcine.models.Trailer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static final String postersUrlAuthority = "http://image.tmdb.org/t/p/";


    private static final String POPCINE_URL =
            "http://api.themoviedb.org/3/movie";

    private static final String API_KEY = "api_key";

    private final static String api_key = "3fc8642732ddc232ce76bae2e5e9b038";

    private static final String YOUTUBE_VIDEO_BASE_URL = "https://www.youtube.com";
    private static final String YOUTUBE_IMAGE_BASE_URL = "https://img.youtube.com";
    private static final String YOUTUBE_VIDEO_PATH = "watch";
    private static final String YOUTUBE_VIDEO_Query_Parameter = "v";
    private static final String YOUTUBE_IMAGE_PATH = "vi";
    private static final String YOUTUBE_IMAGE_POSTFIX = "0.jpg";


    public static URL buildUrl(String... URL_SEGMENTS) {
        Uri.Builder uriBuilder = Uri.parse(POPCINE_URL).buildUpon();
        for (String URL_SEGMENT : URL_SEGMENTS){
                uriBuilder.appendPath(URL_SEGMENT);
        }
        uriBuilder.appendQueryParameter(API_KEY, api_key);

        URL url = null;
        try {
            url = new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Uri youTubeVideoUrlBuilder(Trailer trailer) {
        return Uri.parse(YOUTUBE_VIDEO_BASE_URL)
                .buildUpon()
                .appendEncodedPath(YOUTUBE_VIDEO_PATH)
                .appendQueryParameter(YOUTUBE_VIDEO_Query_Parameter, trailer.getKey())
                .build();
    }

    public static Uri youTubeImageUrlBuilder(Trailer trailer) {
        return Uri.parse(YOUTUBE_IMAGE_BASE_URL)
                .buildUpon()
                .appendEncodedPath(YOUTUBE_IMAGE_PATH)
                .appendEncodedPath(trailer.getKey())
                .appendEncodedPath(YOUTUBE_IMAGE_POSTFIX)
                .build();
    }
}
