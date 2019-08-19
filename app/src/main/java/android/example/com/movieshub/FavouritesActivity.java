package android.example.com.movieshub;

import android.content.Intent;
import android.example.com.movieshub.Database.AppDatabase;
import android.example.com.movieshub.Model.Movie;
import android.example.com.movieshub.ViewHolder.MainMoviesAdapter;
import android.example.com.movieshub.ViewModel.FavouritesViewModel;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity implements MainMoviesAdapter.MainMoviesAdapterOnClickHandler{
    RecyclerView recyclerView;
    AppDatabase appDatabase;
    List<Movie> movies;
    MainMoviesAdapter moviesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        recyclerView = findViewById(R.id.recycler_view_favourites);
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        setupViewModel();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        moviesAdapter = new MainMoviesAdapter(movies,this);

        recyclerView.setAdapter(moviesAdapter);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void setupViewModel() {
        //live data runs by default off the main thread so you don't need executors

        FavouritesViewModel favouritesViewModel = ViewModelProviders.of(this).get(FavouritesViewModel.class);
        favouritesViewModel.getFavMovies().observe(this, new Observer<List<Movie>>() {
            @Override//this method can access the views so you don't need to allow queries on main thread
            public void onChanged(List<Movie> favMovies) {
                movies = favMovies;
                moviesAdapter.setMovies(movies);
            }

        });
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this,MovieDetailActivity.class);
        intent.putExtra("movie",movie);
        startActivity(intent);
    }
}
