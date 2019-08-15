package android.example.com.movieshub.Database;

import android.example.com.movieshub.Model.Movie;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie")
    List<Movie> loadFavouriteMovies();

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void removeMovie(Movie movie);

}
