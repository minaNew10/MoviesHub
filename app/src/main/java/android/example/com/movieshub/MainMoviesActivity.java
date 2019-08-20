package android.example.com.movieshub;


import android.content.Intent;
import android.example.com.movieshub.Database.AppDatabase;
import android.example.com.movieshub.Model.Movie;
import android.example.com.movieshub.Model.MoviesList;
import android.example.com.movieshub.Utils.MoviesService;
import android.example.com.movieshub.Utils.QueryUtils;
import android.example.com.movieshub.ViewHolder.MainMoviesAdapter;
import android.example.com.movieshub.ViewModel.MainMoviesViewModel;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.*;


import androidx.annotation.*;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MainMoviesActivity extends AppCompatActivity implements MainMoviesAdapter.MainMoviesAdapterOnClickHandler {
    private static final String TAG = "MainMoviesList";
    RecyclerView recyclerView;
    List<Movie> movies = new ArrayList<>();
    MainMoviesAdapter moviesAdapter;
    AppDatabase appDatabase;
    MainMoviesViewModel viewModel;

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
        setupViewModel();
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this,MovieDetailActivity.class);
        intent.putExtra(getString(R.string.key_for_passing_movie),movie);
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
                //set multable live data to null to push the viewmodel to query again as in getRatedMovies() is doesn't query unless the list is null
                viewModel.setMutableLiveData(null);
                viewModel.getRatedMovies().observe(this, new Observer<MoviesList>() {
                    @Override
                    public void onChanged(MoviesList moviesList) {
                        moviesAdapter.setMovies(moviesList.getResults());
                    }
                });
            }else {
                item.setTitle(getString(R.string.sort_by_rating));
                viewModel.setMutableLiveData(null);
                viewModel.getPopularMovies().observe(this, new Observer<MoviesList>() {
                    @Override
                    public void onChanged(MoviesList moviesList) {
                        moviesAdapter.setMovies(moviesList.getResults());
                    }
                });

            }
        }else if(id == R.id.action_fav){
            Intent intent = new Intent(MainMoviesActivity.this,FavouritesActivity.class);
            startActivity(intent);
        }
        return true;
    }
    public void setupViewModel(){
        viewModel =  ViewModelProviders.of(this).get(MainMoviesViewModel.class);

        MutableLiveData<MoviesList> moviesList = viewModel.getPopularMovies();
        moviesList.observe(this, new Observer<MoviesList>() {
            @Override
            public void onChanged(MoviesList moviesList) {
                moviesAdapter.setMovies(moviesList.getResults());
            }
        });
    }



}
