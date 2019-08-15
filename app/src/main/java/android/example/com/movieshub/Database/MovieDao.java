package android.example.com.movieshub.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie")
    List<FavouriteMovie> loadFavouriteMovies();

    @Insert
    void insertMovie(FavouriteMovie movie);

    @Delete
    void removeMovie(FavouriteMovie movie);

}
