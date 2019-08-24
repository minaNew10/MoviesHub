package new10.example.com.movieshub.Database;

import new10.example.com.movieshub.Model.Movie;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> loadFavouriteMovies();

    @Query("SELECT * FROM movie WHERE id  = :id")
    Movie loadMovieById(int id);

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void removeMovie(Movie movie);

}
