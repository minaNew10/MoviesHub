package android.example.com.movieshub.ViewHolder;

import android.example.com.movieshub.Database.FavouriteMovie;
import android.example.com.movieshub.Model.Movie;
import android.example.com.movieshub.R;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MainMoviesAdapter extends RecyclerView.Adapter<MainMoviesAdapter.MainMoviesViewHolder> {
    private List<Movie> movies;
    private List<FavouriteMovie> favouriteMovies;

    private static final String TAG = "MainMoviesAdapter";
    // create a member variable of the interface and pass it in the constructor
    private MainMoviesAdapterOnClickHandler onClickHandler;
    // create interface to handle click on the recycler view item
    public interface MainMoviesAdapterOnClickHandler{
         void onClick(int pos);
    }

    public MainMoviesAdapter(List<Movie> movies, MainMoviesAdapterOnClickHandler onClickHandler) {
        this.movies = movies;
        favouriteMovies = null;
        this.onClickHandler = onClickHandler;
    }

    public MainMoviesAdapter(List<FavouriteMovie> movies,List<FavouriteMovie> favouriteMovies, MainMoviesAdapterOnClickHandler onClickHandler) {
        movies = movies;
        favouriteMovies = favouriteMovies;
        this.onClickHandler = onClickHandler;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        favouriteMovies = null;
        notifyDataSetChanged();
    }

    public void setFavouriteMovies(List<FavouriteMovie> movies) {
        favouriteMovies = movies;
        this.movies = null;
        notifyDataSetChanged();
    }

    public class  MainMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView iv_movie;
        public MainMoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_movie = itemView.findViewById(R.id.img_film);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            onClickHandler.onClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MainMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.film_item,viewGroup,false);
        return new MainMoviesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainMoviesViewHolder mainMoviesViewHolder, int i) {
        String url = "";
        if(movies != null) {
             url = movies.get(i).getPoster_path();
        }else {
            url = favouriteMovies.get(i).getPoster_path();
        }
        Picasso.get().load(url).
                error(R.drawable.error)
                .fit()
                .into(mainMoviesViewHolder.iv_movie);
    }

    @Override
    public int getItemCount() {
        if(movies == null){
            return favouriteMovies.size();
        }
        return movies.size();
    }


}
