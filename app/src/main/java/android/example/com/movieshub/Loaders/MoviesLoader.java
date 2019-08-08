package android.example.com.movieshub.Loaders;

import android.content.Context;
import android.example.com.movieshub.Model.Movie;
import android.example.com.movieshub.Utils.QueryUtils;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;


import java.util.List;

public class MoviesLoader extends AsyncTaskLoader<List<Movie>>{

    String mUrl;

    public MoviesLoader(@NonNull Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
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
}
