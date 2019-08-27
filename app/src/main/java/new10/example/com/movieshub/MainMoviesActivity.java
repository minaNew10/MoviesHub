package new10.example.com.movieshub;


import android.content.Intent;
import android.content.res.Configuration;
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
    List<Movie> movies = new ArrayList<>();
    MainMoviesAdapter moviesAdapter;
    AppDatabase appDatabase;
    MainMoviesViewModel viewModel;
    Toast toast ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        toast = Toast.makeText(this,getString(R.string.network_err),Toast.LENGTH_LONG);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.photo_list_preview_columns));
        moviesAdapter = new MainMoviesAdapter(movies,this);
        recyclerView.setAdapter(moviesAdapter);
        recyclerView.setLayoutManager(layoutManager);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(this,getResources().getInteger(R.integer.photo_list_preview_columns_landscape));
            recyclerView.setLayoutManager(layoutManager1);
            recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(
                    getResources().getDimensionPixelSize(R.dimen.photos_list_spacing),
                    getResources().getInteger(R.integer.photo_list_preview_columns_landscape)));
        }else {
            recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(
                    getResources().getDimensionPixelSize(R.dimen.photos_list_spacing),
                    getResources().getInteger(R.integer.photo_list_preview_columns)));
        }

        setupViewModel();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_sort){
            //check sorting method
            if(item.getTitle().toString().equals(getString(R.string.sort_by_rating))) {
                item.setTitle(getString(R.string.sort_by_popularity));
                if (QueryUtils.isOnline(this)) {
                    //set multable live data to null to push the viewmodel to query again as in getRatedMovies() is doesn't query unless the list is null
                    viewModel.setMutableLiveData(null);
                    viewModel.getRatedMovies().observe(this, new Observer<MoviesList>() {
                        @Override
                        public void onChanged(MoviesList moviesList) {
                            moviesAdapter.setMovies(moviesList.getResults());
                        }
                    });
                }else {
                    toast.show();
                }
            }else {
                item.setTitle(getString(R.string.sort_by_rating));
                if(QueryUtils.isOnline(this)) {
                    viewModel.setMutableLiveData(null);
                    viewModel.getPopularMovies().observe(this, new Observer<MoviesList>() {
                        @Override
                        public void onChanged(MoviesList moviesList) {
                            moviesAdapter.setMovies(moviesList.getResults());
                        }
                    });
                }else {
                    toast.show();
                }

            }
        }else if(id == R.id.action_fav){
            Intent intent = new Intent(MainMoviesActivity.this,FavouritesActivity.class);
            startActivity(intent);
        }
        return true;
    }
    public void setupViewModel(){
        viewModel =  ViewModelProviders.of(this).get(MainMoviesViewModel.class);
        if(QueryUtils.isOnline(this)) {
            MutableLiveData<MoviesList> moviesList = viewModel.getPopularMovies();
            moviesList.observe(this, new Observer<MoviesList>() {
                @Override
                public void onChanged(MoviesList moviesList) {
                    moviesAdapter.setMovies(moviesList.getResults());
                }
            });
        }else {
            moviesAdapter.setMovies(movies);
            toast.show();
        }
    }



}
