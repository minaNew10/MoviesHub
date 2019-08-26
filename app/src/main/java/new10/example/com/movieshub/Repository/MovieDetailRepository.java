package new10.example.com.movieshub.Repository;

import android.content.Context;
import new10.example.com.movieshub.Database.AppDatabase;
import new10.example.com.movieshub.Model.Movie;
import new10.example.com.movieshub.Model.ReviewsList;
import new10.example.com.movieshub.Model.VideosList;
import new10.example.com.movieshub.Utils.MoviesService;
import new10.example.com.movieshub.Utils.RetrofitService;
import android.widget.ImageView;
import androidx.lifecycle.MutableLiveData;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public  MutableLiveData<VideosList> getVideos(int movieId){
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

}
