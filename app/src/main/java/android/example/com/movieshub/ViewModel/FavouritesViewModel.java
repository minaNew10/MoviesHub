package android.example.com.movieshub.ViewModel;

import android.app.Application;
import android.example.com.movieshub.Database.AppDatabase;
import android.example.com.movieshub.Model.Movie;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavouritesViewModel extends AndroidViewModel {

    LiveData<List<Movie>> favMovies ;

    public FavouritesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(getApplication());
        favMovies = appDatabase.movieDao().loadFavouriteMovies();
    }

    public LiveData<List<Movie>> getFavMovies() {
        return favMovies;
    }
}
