package new10.example.com.movieshub.Repository;


import new10.example.com.movieshub.Model.MoviesList;
import new10.example.com.movieshub.Utils.MoviesService;
import new10.example.com.movieshub.Utils.RetrofitService;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainMoviesRepository {

    private static MainMoviesRepository moviesRepository;

    public static MainMoviesRepository getInstance(){
        if (moviesRepository == null){
            moviesRepository = new MainMoviesRepository();
        }
        return moviesRepository;
    }

    private MoviesService moviesService;

    public MainMoviesRepository(){
        moviesService = RetrofitService.createService(MoviesService.class);
    }

    public MutableLiveData<MoviesList> getMovies(String sortBy){
        final MutableLiveData<MoviesList> moviesData = new MutableLiveData<>();
        moviesService.getMoviesList(sortBy).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if(response.isSuccessful()){
                    moviesData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                     moviesData.setValue(null);
            }
        });
        return moviesData;
    }


}
