package new10.example.com.movieshub.Repository;

import android.content.Context;
import android.example.com.movieshub.R;
import android.widget.Toast;
import androidx.lifecycle.MutableLiveData;
import new10.example.com.movieshub.AppExecutors;
import new10.example.com.movieshub.Database.AppDatabase;
import new10.example.com.movieshub.Model.Movie;
import android.util.Log;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavMoviesRepository {

    private static AppDatabase appDatabase;
    private static Movie currMovie;

    private static final String TAG = "FavMoviesRepository";

    public static LiveData<List<Movie>> getFavMovies(Context context){
        appDatabase = AppDatabase.getInstance(context);
        LiveData<List<Movie>> movies = appDatabase.movieDao().loadFavouriteMovies();
        Log.i(TAG, "getFavMovies: ");
        return movies;
    }

    public static MutableLiveData<Boolean> isFavMov(Context context,int movieId){
        appDatabase = AppDatabase.getInstance(context);
        MutableLiveData<Boolean> isFav = new MutableLiveData<>();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                currMovie = appDatabase.movieDao().loadMovieById(movieId);
                boolean state = currMovie == null;
                Log.i(TAG, "run: " +  state);
                if(currMovie == null){
                    Log.i(TAG, "run: currMovie is null");
                    isFav.postValue(false);
                }else {
                    Log.i(TAG, "run: currMovie is not null");
                    isFav.postValue(true);
                }
            }
        });

        return isFav;
    }

    public static void insertMovieIntoFav(Movie movie){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.movieDao().insertMovie(movie);
            }
        });
    }

    public static void removeMovieFromFav(Movie movie){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.movieDao().removeMovie(movie);
            }
        });
    }
}
