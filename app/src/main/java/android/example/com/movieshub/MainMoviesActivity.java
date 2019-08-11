package android.example.com.movieshub;

import android.content.Context;
import android.content.Intent;
import android.example.com.movieshub.Model.Movie;
import android.example.com.movieshub.Utils.QueryUtils;
import android.example.com.movieshub.ViewHolder.MainMoviesAdapter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class MainMoviesActivity extends AppCompatActivity implements MainMoviesAdapter.MainMoviesAdapterOnClickHandler {
    private static final String TAG = "MainMoviesList";
    RecyclerView recyclerView;
    List<Movie> movies = new ArrayList<>();
    MainMoviesAdapter moviesAdapter;

    public static final String BASE_URI = "https://api.themoviedb.org/3/";
    public static final String KEY_SORTING = "sortingMode";
    public static final String QUERY_SORT_BY_POPULARITY = "popularity.desc";
    public static final String QUERY_SORT_BY_RATING = "vote_average.desc";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view_movies);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        moviesAdapter = new MainMoviesAdapter(this, movies,this);

        recyclerView.setAdapter(moviesAdapter);
        recyclerView.setLayoutManager(layoutManager);
        String url = buildUri(QUERY_SORT_BY_POPULARITY);
        if (isOnline()) {
            makeRequestByVolley(url);
        } else {
            Toast.makeText(this,getString(R.string.network_err),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this,MovieDetailActivity.class);
        intent.putExtra("movie",movie);
        startActivity(intent);
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
            //check sorting method

            if(item.getTitle().toString().equals(getString(R.string.sort_by_rating))){
                item.setTitle(getString(R.string.sort_by_popularity));
                ///check network connectivity
                if(isOnline()) {
                    makeRequestByVolley(buildUri(QUERY_SORT_BY_RATING));
                }else {
                    Toast.makeText(this,getString(R.string.network_err),Toast.LENGTH_LONG).show();
                }

            }else {
                item.setTitle(getString(R.string.sort_by_rating));
                if(isOnline()) {
                    makeRequestByVolley(buildUri(QUERY_SORT_BY_POPULARITY));
                }else {
                    Toast.makeText(this,getString(R.string.network_err),Toast.LENGTH_LONG).show();
                }

            }
        }
        return true;
    }

    public void makeRequestByVolley(String url){
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        movies = QueryUtils.extractMoviesFromJson(response);
                        moviesAdapter.setMovies(movies);
                        Log.i(TAG, "onResponse: " + response);
                        for(int i = 0 ; i < movies.size();i++){
                            Log.i(TAG, "onResponse: " + movies.get(i).getTitle());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: "+error.getLocalizedMessage() );
                    }
                });
        Volley.newRequestQueue(this).add(request);

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public String buildUri(String sortingBy){
        Uri baseUrl = Uri.parse(BASE_URI);
        Uri.Builder uriBuilder = baseUrl.buildUpon();
        uriBuilder.appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("sort_by",sortingBy)
                .appendQueryParameter("api_key",BuildConfig.API_KEY);
        return  uriBuilder.toString();
    }
}
