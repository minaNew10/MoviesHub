package android.example.com.movieshub;

import android.app.LoaderManager;
import android.content.Loader;
import android.content.SharedPreferences;
import android.example.com.movieshub.Loaders.MoviesLoader;
import android.example.com.movieshub.Model.Movie;
import android.example.com.movieshub.Utils.QueryUtils;
import android.example.com.movieshub.ViewHolder.MainMoviesAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainMoviesList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {
    private static final String TAG = "MainMoviesList";
    RecyclerView recyclerView;
    List<Movie> movies = new ArrayList<>();
    MainMoviesAdapter moviesAdapter;
    SharedPreferences.Editor editor;
    public static final String BASE_URI = "https://api.themoviedb.org/3/";
    public static final String KEY_SORTING = "sortingMode";
    public static final String QUERY_SORT_BY_POPULARITY = "popularity.desc";
    public static final String QUERY_SORT_BY_RATING = "vote_average.desc";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sortingMode = getApplicationContext().getSharedPreferences("sortingMode",0);
        editor = sortingMode.edit();
        recyclerView = findViewById(R.id.recycler_view_movies);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        moviesAdapter = new MainMoviesAdapter(this, movies);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SORTING,QUERY_SORT_BY_POPULARITY);
        getLoaderManager().initLoader(0, bundle, this);
        recyclerView.setAdapter(moviesAdapter);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_movies,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_sort){
            if(item.getTitle().toString().equals(getString(R.string.sort_by_rating))){
                item.setTitle(getString(R.string.sort_by_popularity));
                Bundle bundle = new Bundle();
                bundle.putString(KEY_SORTING,QUERY_SORT_BY_RATING);
                getLoaderManager().restartLoader(0,bundle,this);


            }else {
                item.setTitle(getString(R.string.sort_by_rating));
                Bundle bundle = new Bundle();
                bundle.putString(KEY_SORTING,QUERY_SORT_BY_POPULARITY);
                getLoaderManager().restartLoader(0,bundle,this);

            }
        }
        return true;
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
//        "https://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=c9d25d621f35c3b1a17033c8e2658853"
        String sortMode = bundle.getString(KEY_SORTING);
        String uri = buildUri(sortMode);
        Log.i(TAG, "onCreateLoader: " + uri);
        return new MoviesLoader(this,uri);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        moviesAdapter.setMovies(movies);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
    public String buildUri(String sortingBy){
        Uri baseUrl = Uri.parse(BASE_URI);
        Uri.Builder uriBuilder = baseUrl.buildUpon();
        uriBuilder.appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("sort_by",sortingBy)
                .appendQueryParameter("api_key","c9d25d621f35c3b1a17033c8e2658853");
        return  uriBuilder.toString();
    }

}

