package new10.example.com.movieshub.ViewModel;

import new10.example.com.movieshub.Model.MoviesList;
import new10.example.com.movieshub.Repository.MainMoviesRepository;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainMoviesViewModel extends ViewModel {
    public static final String QUERY_SORT_BY_POPULARITY = "popularity.desc";
    public static final String QUERY_SORT_BY_RATING = "vote_average.desc";

    private MutableLiveData<MoviesList> mutableLiveData;

    private MainMoviesRepository moviesRepository  = MainMoviesRepository.getInstance();



    public MutableLiveData<MoviesList> getPopularMovies() {
        if(mutableLiveData == null) {
            mutableLiveData = moviesRepository.getMovies(QUERY_SORT_BY_POPULARITY);
        }
        return mutableLiveData;
    }
    public MutableLiveData<MoviesList> getRatedMovies() {
        if(mutableLiveData == null) {
            mutableLiveData = moviesRepository.getMovies(QUERY_SORT_BY_RATING);
        }
        return mutableLiveData;
    }

    public void setMutableLiveData(MutableLiveData<MoviesList> mutableLiveData) {
        this.mutableLiveData = mutableLiveData;
    }
}
