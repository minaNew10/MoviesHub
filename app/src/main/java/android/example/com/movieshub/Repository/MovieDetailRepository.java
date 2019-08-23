package android.example.com.movieshub.Repository;

import android.content.Context;
import android.example.com.movieshub.Database.AppDatabase;
import android.example.com.movieshub.Model.Movie;
import android.example.com.movieshub.Model.MoviesList;
import android.example.com.movieshub.Model.ReviewsList;
import android.example.com.movieshub.Model.VideosList;
import android.example.com.movieshub.R;
import android.example.com.movieshub.Utils.MoviesService;
import android.example.com.movieshub.Utils.QueryUtils;
import android.example.com.movieshub.Utils.RetrofitService;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.lifecycle.MutableLiveData;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailRepository {
    private static MovieDetailRepository movieDetailRepository;
    private AppDatabase appDatabase;
    public static MovieDetailRepository getInstance(){
        if (movieDetailRepository == null){
            movieDetailRepository = new MovieDetailRepository();
        }
        return movieDetailRepository;
    }

    private MoviesService moviesService;

    public MovieDetailRepository() {
        moviesService = RetrofitService.createService(MoviesService.class);
    }

    public  MutableLiveData<ReviewsList> getReviews(int movieId){
        final MutableLiveData<ReviewsList> reviewsData = new MutableLiveData<>();
            moviesService.getReviewsList(movieId).enqueue(new Callback<ReviewsList>() {
                @Override
                public void onResponse(Call<ReviewsList> call, Response<ReviewsList> response) {
                        if(response.isSuccessful()){
                            reviewsData.setValue(response.body());
                        }
                }

                @Override
                public void onFailure(Call<ReviewsList> call, Throwable t) {
                    reviewsData.setValue(null);
                }
            });
            return reviewsData;
    }

    public  MutableLiveData<VideosList> getVideos( int movieId){
        final MutableLiveData<VideosList> videosData = new MutableLiveData<>();
            moviesService.getVideosList(movieId).enqueue(new Callback<VideosList>() {
                @Override
                public void onResponse(Call<VideosList> call, Response<VideosList> response) {
                        if(response.isSuccessful()){
                            videosData.setValue(response.body());
                        }
                }

                @Override
                public void onFailure(Call<VideosList> call, Throwable t) {
                    videosData.setValue(null);
                }
            });
            return videosData;
    }

    public void loadImage(String url, ImageView imageView){
             Picasso.get().load(url).fit().into(imageView);
    }

    public boolean isMovieFav(Context context,int movieId){
        appDatabase = AppDatabase.getInstance(context);
        Movie movie = appDatabase.movieDao().loadMovieById(movieId);
        return movie == null ? false : true;
    }

}
