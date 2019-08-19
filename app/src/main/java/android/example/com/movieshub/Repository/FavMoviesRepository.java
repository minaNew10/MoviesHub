package android.example.com.movieshub.Repository;

import android.content.Context;
import android.example.com.movieshub.Database.AppDatabase;
import android.example.com.movieshub.Model.Movie;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavMoviesRepository {
    private static AppDatabase appDatabase;
    private Context context;


    public static LiveData<List<Movie>> getFavMovies(Context context){
        appDatabase = AppDatabase.getInstance(context);
        LiveData<List<Movie>> movies = appDatabase.movieDao().loadFavouriteMovies();
        return movies;
    }
}
