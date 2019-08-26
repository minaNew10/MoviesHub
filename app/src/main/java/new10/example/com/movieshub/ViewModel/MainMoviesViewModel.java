package new10.example.com.movieshub.ViewModel;

import new10.example.com.movieshub.Model.MoviesList;
import new10.example.com.movieshub.Repository.MainMoviesRepository;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import new10.example.com.movieshub.Utils.QueryUtils;

public class MainMoviesViewModel extends ViewModel {

    private MutableLiveData<MoviesList> mutableLiveData;

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

    public void setMutableLiveData(MutableLiveData<MoviesList> mutableLiveData) {
        this.mutableLiveData = mutableLiveData;
    }
}
