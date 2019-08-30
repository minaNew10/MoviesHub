package new10.example.com.movieshub.Utils;




import android.example.com.movieshub.BuildConfig;
import new10.example.com.movieshub.Model.MoviesList;
import new10.example.com.movieshub.Model.ReviewsList;
import new10.example.com.movieshub.Model.VideosList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesService {
    @GET("{id}/reviews?api_key=" + BuildConfig.API_KEY)
    Call<ReviewsList> getReviewsList(@Path("id")int movieId);

    @GET("{id}/videos?api_key=" + BuildConfig.API_KEY)
    Call<VideosList> getVideosList(@Path("id")int movieId);

    @GET("{sortMode}?api_key=" + BuildConfig.API_KEY)
    Call<MoviesList> getMoviesList(@Path("sortMode") String sortBy);
}
