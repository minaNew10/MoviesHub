package new10.example.com.movieshub.ViewModel;

import android.content.Context;
import new10.example.com.movieshub.Model.Movie;
import new10.example.com.movieshub.Model.ReviewsList;
import new10.example.com.movieshub.Model.VideosList;
import new10.example.com.movieshub.Repository.FavMoviesRepository;
import new10.example.com.movieshub.Repository.MovieDetailRepository;
import android.widget.ImageView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MovieDetailViewModel extends ViewModel {

    private MutableLiveData<VideosList> trailers;
    private MutableLiveData<ReviewsList>  reviews;
    private MutableLiveData<Boolean> isFav;

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
    public MutableLiveData<Boolean> isFavMovie(Context context,int movieId){
        isFav = FavMoviesRepository.isFavMov(context,movieId);
        return isFav;
    }

    public void loadImageIntoView(String url, ImageView imageView){
        if(imageView.getDrawable() == null)
                 movieDetailRepository.loadImage(url,imageView);
    }

    public void insertMovieIntoFav(Movie movie){
        FavMoviesRepository.insertMovieIntoFav(movie);
    }
    public void removeMovieFromFav(Movie movie){
        FavMoviesRepository.removeMovieFromFav(movie);
    }

}
