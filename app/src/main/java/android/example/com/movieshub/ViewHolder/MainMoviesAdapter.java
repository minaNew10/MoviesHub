package android.example.com.movieshub.ViewHolder;

import android.content.Context;
import android.example.com.movieshub.Model.Movie;
import android.example.com.movieshub.R;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

public class MainMoviesAdapter extends RecyclerView.Adapter<MainMoviesAdapter.MainMoviesViewHolder> {
    private Context context;
    private List<Movie> movies;

    private static final String TAG = "MainMoviesAdapter";
    // create a member variable of the interface and pass it in the constructor
    private MainMoviesAdapterOnClickHandler onClickHandler;
    // create interface to handle click on the recycler view item
    public interface MainMoviesAdapterOnClickHandler{
         void onClick(Movie movie);
    }
    public MainMoviesAdapter(Context context,List<Movie> movies,MainMoviesAdapterOnClickHandler onClickHandler) {
        this.movies = movies;
        this.onClickHandler = onClickHandler;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
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
            onClickHandler.onClick(movies.get(getAdapterPosition()));
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
        String url = movies.get(i).getPoster();
        Log.i(TAG, "onBindViewHolder: " + url);
        Picasso.get().load(url).
                error(R.drawable.error)
                .fit()
                .into(mainMoviesViewHolder.iv_movie)

        ;

//        mainMoviesViewHolder.iv_movie.setImageResource(images[i]);
    }

    @Override
    public int getItemCount() {
        if(movies == null)
             return 0;
        return movies.size();
    }


}
