package android.example.com.movieshub;

import android.content.Context;
import android.content.Intent;
import android.example.com.movieshub.Model.Movie;
import android.example.com.movieshub.Model.Review;
import android.example.com.movieshub.Model.ReviewsList;
import android.example.com.movieshub.Utils.MoviesService;
import android.example.com.movieshub.Utils.QueryUtils;
import android.example.com.movieshub.ViewHolder.ReviewsAdapter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.squareup.picasso.Picasso;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String TAG = "MovieDetailActivity";
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView imageView;
    TextView txtv_overview,txtv_release_date;
    RatingBar ratingBar;
    RecyclerView reviewsRecycler;
    List<Review> reviews = new ArrayList<>();
    ReviewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();
        final Movie movie = (Movie) intent.getSerializableExtra("movie");
        populateUI(movie);
        requestReviews(movie.getId());
    }

    public void populateUI(Movie movie){
        collapsingToolbarLayout = findViewById(R.id.coll_toolbar_movie_detail);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsingToolbarLayoutExpandedTextStyle);

        imageView = findViewById(R.id.imgv_movie_detail);
        imageView.setAdjustViewBounds(true);

        collapsingToolbarLayout.setTitle(movie.getTitle());
        Picasso.get().load(movie.getPoster_path()).into(imageView);

        txtv_overview = findViewById(R.id.txtv_movie_overview);
        txtv_overview.setText(movie.getOverview());

        txtv_release_date = findViewById(R.id.txtv_release_date);
        txtv_release_date.setText(movie.getRelease_date());

        ratingBar = findViewById(R.id.rating_bar);
        ratingBar.setIsIndicator(true);
        ratingBar.setRating(Float.parseFloat(movie.getVoteAverage()) - 5);

        reviewsRecycler = findViewById(R.id.recycler_view_reviews);
        adapter = new ReviewsAdapter(reviews);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);

        reviewsRecycler.setLayoutManager(layoutManager);
        reviewsRecycler.addItemDecoration(decoration);
        reviewsRecycler.setAdapter(adapter);


    }

    public void requestReviews(int movieId){
        if(QueryUtils.isOnline(this)) {
            Retrofit retrofit = new Retrofit
                    .Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(QueryUtils.BASE_URI_MOVIES).build();
            MoviesService moviesService = retrofit.create(MoviesService.class);
            moviesService.getReviewsList(movieId).enqueue(new Callback<ReviewsList>() {
                @Override
                public void onResponse(Call<ReviewsList> call, Response<ReviewsList> response) {
                    reviews = response.body().getResults();
                    adapter.setReviews(reviews);
                }

                @Override
                public void onFailure(Call<ReviewsList> call, Throwable t) {
                    Log.i(TAG, "onFailure: " + t.getMessage());
                }
            });
        }else {
            Toast.makeText(this,getString(R.string.network_err),Toast.LENGTH_LONG).show();
        }
    }
    public void requestVideo(int movieId){
        if(QueryUtils.isOnline(this)) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(QueryUtils.BASE_URI_MOVIES).build();
            MoviesService moviesService = retrofit.create(MoviesService.class);
            moviesService.getVideosJson(movieId, BuildConfig.API_KEY).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        if(response.code() == HttpURLConnection.HTTP_OK) {
                            Log.i(TAG, "onResponse: "+ response.body().string());
                        }else {
                            Log.i(TAG, "onResponse: "+ response.code());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }else {
            Toast.makeText(this,getString(R.string.network_err),Toast.LENGTH_LONG).show();
        }
    }


}
