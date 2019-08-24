package new10.example.com.movieshub.ViewModel;

import android.app.Application;
import new10.example.com.movieshub.Model.Movie;
import new10.example.com.movieshub.Repository.FavMoviesRepository;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavouritesViewModel extends AndroidViewModel {

    LiveData<List<Movie>> favMovies ;

    public FavouritesViewModel(@NonNull Application application) {
        super(application);
        favMovies = FavMoviesRepository.getFavMovies(application.getApplicationContext());
    }

    public LiveData<List<Movie>> getFavMovies() {
        return favMovies;
    }
}
