package android.example.com.movieshub;


import android.content.Intent;
import android.example.com.movieshub.Database.AppDatabase;
import android.example.com.movieshub.Model.Movie;
import android.example.com.movieshub.Model.MoviesList;
import android.example.com.movieshub.Utils.MoviesService;
import android.example.com.movieshub.Utils.QueryUtils;
import android.example.com.movieshub.ViewHolder.MainMoviesAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HTTP;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainMoviesActivity extends AppCompatActivity implements MainMoviesAdapter.MainMoviesAdapterOnClickHandler {
    private static final String TAG = "MainMoviesList";
    RecyclerView recyclerView;
    List<Movie> movies = new ArrayList<>();
    MainMoviesAdapter moviesAdapter;


    public static final String QUERY_SORT_BY_POPULARITY = "popularity.desc";
    public static final String QUERY_SORT_BY_RATING = "vote_average.desc";

    AppDatabase appDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        appDatabase = AppDatabase.getInstance(getApplicationContext());
        recyclerView = findViewById(R.id.recycler_view_movies);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        moviesAdapter = new MainMoviesAdapter(movies,this);

        recyclerView.setAdapter(moviesAdapter);
        recyclerView.setLayoutManager(layoutManager);

        requestMovies(QUERY_SORT_BY_POPULARITY);

    }

    @Override
    public void onClick(int pos) {
        Intent intent = new Intent(this,MovieDetailActivity.class);
        Log.i(TAG, "onClick: pos: " + pos + " Size: " + movies.size());
        intent.putExtra("movie",movies.get(pos));
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
                requestMovies(QUERY_SORT_BY_RATING);
            }else {
                item.setTitle(getString(R.string.sort_by_rating));
                requestMovies(QUERY_SORT_BY_POPULARITY);
            }
        }else if(id == R.id.action_fav){
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    movies = appDatabase.movieDao().loadFavouriteMovies();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            moviesAdapter.setMovies(movies);
                        }
                    });
                }
            });


        }
        return true;
    }
    public void requestMovies(String sortingMode){
        if(QueryUtils.isOnline(this)) {

            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl(QueryUtils.BASE_URI_MOVIES)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MoviesService moviesService = retrofit.create(MoviesService.class);
            moviesService.getMoviesList(sortingMode).enqueue(new Callback<MoviesList>() {
                @Override
                public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                    MoviesList moviesList = response.body();
                    movies = moviesList.getResults();
                    moviesAdapter.setMovies(movies);
                }

                @Override
                public void onFailure(Call<MoviesList> call, Throwable t) {
                    Log.i(TAG, "onFailure: " + t.getMessage());

                }
            });
        }else {
            Toast.makeText(this,getString(R.string.network_err),Toast.LENGTH_LONG).show();
        }
    }


}
