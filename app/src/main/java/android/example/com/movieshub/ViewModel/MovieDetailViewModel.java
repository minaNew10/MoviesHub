package android.example.com.movieshub.ViewModel;

import android.example.com.movieshub.Model.ReviewsList;
import android.example.com.movieshub.Model.VideosList;
import android.example.com.movieshub.Repository.MovieDetailRepository;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MovieDetailViewModel extends ViewModel {

    private MutableLiveData<VideosList> trailers;
    private MutableLiveData<ReviewsList>  reviews;

    private MovieDetailRepository movieDetailRepository = MovieDetailRepository.getInstance();

    public MutableLiveData<VideosList> getTrailerMovies(int movieId) {
        if(trailers == null) {
            trailers = movieDetailRepository.getVideos(movieId);
        }
        return trailers;
    }
    public MutableLiveData<ReviewsList> getMoviesReviews(int movieId) {
        if(reviews == null) {
            reviews = movieDetailRepository.getReviews(movieId);
        }
        return reviews;
    }

    public void setVideosList(MutableLiveData<VideosList> mutableLiveData) {
        this.trailers = mutableLiveData;
    }
    public void setReviewsList(MutableLiveData<ReviewsList> mutableLiveData) {
        this.reviews = mutableLiveData;
    }

}
