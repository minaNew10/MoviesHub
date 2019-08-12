package android.example.com.movieshub.Utils;

import android.example.com.movieshub.BuildConfig;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.Calendar;

public interface MoviesService {
    @GET("movie")
    Call<ResponseBody> getJson(@Query("sort_by") String sortBy,@Query("api_key") String apiKey);
}
