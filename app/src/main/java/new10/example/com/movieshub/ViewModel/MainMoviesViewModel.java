package new10.example.com.movieshub.ViewModel;

import android.content.Context;

import new10.example.com.movieshub.Model.Movie;
import new10.example.com.movieshub.Model.MoviesList;
import new10.example.com.movieshub.Repository.FavMoviesRepository;
import new10.example.com.movieshub.Repository.MainMoviesRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import new10.example.com.movieshub.Utils.QueryUtils;

public class MainMoviesViewModel extends ViewModel {

    private MutableLiveData<MoviesList> mutableLiveData;
    private LiveData<List<Movie>> favMovies;

    private MainMoviesRepository moviesRepository  = MainMoviesRepository.getInstance();

    public MutableLiveData<MoviesList> getPopularMovies() {
        if(mutableLiveData == null) {
            mutableLiveData = moviesRepository.getMovies(QueryUtils.QUERY_SORT_BY_POPULARITY);
        }
        return mutableLiveData;
    }
    public MutableLiveData<MoviesList> getRatedMovies() {
        if(mutableLiveData == null) {
            mutableLiveData = moviesRepository.getMovies(QueryUtils.QUERY_SORT_BY_RATING);
        }
        return mutableLiveData;
    }

    public LiveData<List<Movie>> getFavMovies(Context context){
        if(favMovies == null){
            favMovies = FavMoviesRepository.getFavMovies(context);
        }
        return favMovies;
    }

    public void setMutableLiveData(MutableLiveData<MoviesList> mutableLiveData) {
        this.mutableLiveData = mutableLiveData;
    }
}
