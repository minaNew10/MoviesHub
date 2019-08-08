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
    private static final String MOVIES_API_URL = "http://image.tmdb.org/t/p/";
    private static final String TAG = "MainMoviesAdapter";
    public MainMoviesAdapter(Context context,List<Movie> movies) {
        this.movies = movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public class  MainMoviesViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_movie;
        public MainMoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_movie = itemView.findViewById(R.id.img_film);
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
        String url = buildPosterUrl(movies.get(i).getPoster());
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

    public String buildPosterUrl(String specificPath){
        Uri baseUrl = Uri.parse(MOVIES_API_URL);
        Uri.Builder uriBuilder = baseUrl.buildUpon();
        uriBuilder.appendEncodedPath("w185");
        uriBuilder.appendEncodedPath(specificPath)
                .build();
        return  uriBuilder.toString();
    }
}
