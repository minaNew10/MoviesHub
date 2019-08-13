package android.example.com.movieshub.Utils;

import android.example.com.movieshub.BuildConfig;
import android.example.com.movieshub.Model.Movie;
import android.example.com.movieshub.Model.MoviesList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.Calendar;
import java.util.List;

public interface MoviesService {
    @GET("movie/{id}/reviews?api_key=" + BuildConfig.API_KEY)
    Call<ResponseBody> getReviewsJson(@Path("id")int movieId);

    @GET("movie/{id}/videos")
    Call<ResponseBody> getVideosJson(@Path("id")int movieId,@Query("api_key") String apiKey);

    @GET("discover/movie?api_key=" + BuildConfig.API_KEY)
    Call<MoviesList> getMoviesList(@Query("sort_by") String sortBy);
}
