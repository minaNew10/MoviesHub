package new10.example.com.movieshub.Repository;

import android.content.Context;
import new10.example.com.movieshub.Database.AppDatabase;
import new10.example.com.movieshub.Model.Movie;
import android.util.Log;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavMoviesRepository {

    private static AppDatabase appDatabase;

    private static final String TAG = "FavMoviesRepository";

    public static LiveData<List<Movie>> getFavMovies(Context context){
        appDatabase = AppDatabase.getInstance(context);
        LiveData<List<Movie>> movies = appDatabase.movieDao().loadFavouriteMovies();
        Log.i(TAG, "getFavMovies: ");
        return movies;
    }
}
