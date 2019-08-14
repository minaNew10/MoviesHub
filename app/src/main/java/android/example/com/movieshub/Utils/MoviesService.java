package android.example.com.movieshub.Utils;

import android.example.com.movieshub.BuildConfig;
import android.example.com.movieshub.Model.Movie;
import android.example.com.movieshub.Model.MoviesList;
import android.example.com.movieshub.Model.ReviewsList;
import android.example.com.movieshub.Model.VideosList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.Calendar;
import java.util.List;

public interface MoviesService {
    @GET("movie/{id}/reviews?api_key=" + BuildConfig.API_KEY)
    Call<ReviewsList> getReviewsList(@Path("id")int movieId);

    @GET("movie/{id}/videos?api_key=" + BuildConfig.API_KEY)
    Call<VideosList> getVideosList(@Path("id")int movieId);

    @GET("discover/movie?api_key=" + BuildConfig.API_KEY)
    Call<MoviesList> getMoviesList(@Query("sort_by") String sortBy);
}
