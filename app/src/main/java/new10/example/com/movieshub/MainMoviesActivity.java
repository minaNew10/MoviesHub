package new10.example.com.movieshub;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import new10.example.com.movieshub.Database.AppDatabase;
import new10.example.com.movieshub.Model.Movie;
import new10.example.com.movieshub.Model.MoviesList;
import android.example.com.movieshub.R;
import new10.example.com.movieshub.Utils.QueryUtils;
import new10.example.com.movieshub.ViewHolder.MainMoviesAdapter;
import new10.example.com.movieshub.ViewModel.MainMoviesViewModel;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.*;


import androidx.annotation.*;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static androidx.appcompat.widget.LinearLayoutCompat.VERTICAL;


public class MainMoviesActivity extends AppCompatActivity implements MainMoviesAdapter.MainMoviesAdapterOnClickHandler {
    private static final String TAG = "MainMoviesList";
    @BindView(R.id.recycler_view_movies)
    RecyclerView recyclerView;
    List<Movie> movies;
    MainMoviesAdapter moviesAdapter;
    AppDatabase appDatabase;
    MainMoviesViewModel viewModel;
    Toast toast ;
    ActionBar actionBar;
    String title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        actionBar = getSupportActionBar();
        title = actionBar.getTitle().toString();
        if(savedInstanceState != null){
            title = savedInstanceState.getString(getString(R.string.key_for_title));
            actionBar.setTitle(title);
        }
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        toast = Toast.makeText(this,getString(R.string.network_err),Toast.LENGTH_LONG);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.photo_list_preview_columns));
        moviesAdapter = new MainMoviesAdapter(movies,this);
        setupViewModel();
        recyclerView.setAdapter(moviesAdapter);
        recyclerView.setLayoutManager(layoutManager);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int noOfCol = calculateNoOfColumns(this);
            RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(this,noOfCol);
            recyclerView.setLayoutManager(layoutManager1);
            recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(
                    getResources().getDimensionPixelSize(R.dimen.photos_list_spacing),
                    noOfCol));
        }else {
            recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(
                    getResources().getDimensionPixelSize(R.dimen.photos_list_spacing),
                    getResources().getInteger(R.integer.photo_list_preview_columns)));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        toast.cancel();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        title = actionBar.getTitle().toString();
        outState.putString(getString(R.string.key_for_title),title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_sort){

            //check sorting method
            if(item.getTitle().toString().equals(getString(R.string.sort_by_rating))) {
                if (QueryUtils.isOnline(this)) {
                    actionBar.setTitle(getString(R.string.app_name));
                    item.setTitle(getString(R.string.sort_by_popularity));
                    //set multable live data to null to push the viewmodel to query again as in getRatedMovies() is doesn't query unless the list is null
                    viewModel.setMutableLiveData(null);
                    viewModel.getRatedMovies().observe(this, new Observer<MoviesList>() {
                        @Override
                        public void onChanged(MoviesList moviesList) {
                            movies = moviesList.getResults();
                            moviesAdapter.setMovies(movies);
                        }
                    });
                }else {
                    toast.show();
                }
            }else {
                if(QueryUtils.isOnline(this)) {
                    item.setTitle(getString(R.string.sort_by_rating));
                    actionBar.setTitle(getString(R.string.app_name));

                    viewModel.setMutableLiveData(null);
                    viewModel.getPopularMovies().observe(this, new Observer<MoviesList>() {
                        @Override
                        public void onChanged(MoviesList moviesList) {
                            movies = moviesList.getResults();
                            moviesAdapter.setMovies(movies);
                        }
                    });
                }else {
                    toast.show();
                }

            }
        }else if(id == R.id.action_fav){
                actionBar.setTitle(getString(R.string.favourites));
                viewModel.getFavMovies(this).observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> favMovies) {
                        if(favMovies == null || favMovies.size() == 0){
                            toast.setText(getString(R.string.no_fav_movies));
                            toast.show();
                        }
                        //this check to prevent getting back to favourites when choosing up after adding movie to favourites
                        else if(actionBar.getTitle().equals(getString(R.string.favourites))) {
                            movies = favMovies;
                            moviesAdapter.setMovies(movies);
                        }
                    }
                });
        }
        return true;
    }

    public void setupViewModel(){
        viewModel =  ViewModelProviders.of(this).get(MainMoviesViewModel.class);
        if(title.equals(getString(R.string.app_name))) {
            MutableLiveData<MoviesList> moviesList = viewModel.getPopularMovies();
            moviesList.observe(this, new Observer<MoviesList>() {
                @Override
                public void onChanged(MoviesList moviesList) {
                    if(!QueryUtils.isOnline(MainMoviesActivity.this) && moviesList == null)
                        toast.show();
                    else {
                        moviesAdapter.setMovies(moviesList.getResults());
                    }
                }
            });
        }else if( title.equals(getString(R.string.favourites))){
            viewModel.getFavMovies(this).observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(List<Movie> favMovies) {
                    if(favMovies == null || favMovies.size() == 0){
                       toast.setText(getString(R.string.no_fav_movies));
                       toast.show();
                    }else {
                        movies = favMovies;
                        moviesAdapter.setMovies(movies);
                    }
                }
            });

        }else{
            toast.setText(getString(R.string.network_err));
            toast.show();
        }
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }



}
