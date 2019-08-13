package android.example.com.movieshub.Utils;

import android.example.com.movieshub.BuildConfig;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.Calendar;

public interface MoviesService {
    @GET("discover/movie?api_key=" + BuildConfig.API_KEY)
    Call<ResponseBody> getMoviesJson(@Query("sort_by") String sortBy);

    @GET("movie/{id}/reviews?api_key=" + BuildConfig.API_KEY)
    Call<ResponseBody> getReviewsJson(@Path("id")int movieId);

    @GET("movie/{id}/videos")
    Call<ResponseBody> getVideosJson(@Path("id")int movieId,@Query("api_key") String apiKey);
}
