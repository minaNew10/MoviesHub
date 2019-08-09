package android.example.com.movieshub.Loaders;

import android.content.Context;
import android.example.com.movieshub.Model.Movie;
import android.example.com.movieshub.Utils.QueryUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;
import android.util.Log;


import java.util.List;

public class MoviesLoader extends AsyncTaskLoader<List<Movie>>{

    String mUrl;
    private static final String TAG = "MoviesLoader";
    public MoviesLoader(@NonNull Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(TAG, "onStartLoading: ");
        if(isOnline())
            forceLoad();
    }

    @Nullable
    @Override
    public List<Movie> loadInBackground() {
        if(mUrl == null)
            return null;
        List<Movie> movies = QueryUtils.fetchMoviesList(mUrl);
        return movies;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
