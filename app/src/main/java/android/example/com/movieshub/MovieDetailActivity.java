package android.example.com.movieshub;

import android.content.Intent;
import android.example.com.movieshub.Model.Movie;
import android.example.com.movieshub.Utils.MoviesService;
import android.example.com.movieshub.Utils.QueryUtils;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.squareup.picasso.Picasso;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import java.io.IOException;
import java.net.HttpURLConnection;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String TAG = "MovieDetailActivity";
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView imageView;
    TextView txtv_overview,txtv_release_date;
    RatingBar ratingBar;
    ImageButton imgBtnReviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();
        final Movie movie = (Movie) intent.getSerializableExtra("movie");
        populateUI(movie);

        imgBtnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestReviews(movie.getId());
            }
        });

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

        imgBtnReviews = findViewById(R.id.img_btn_reviews);
    }

    public void requestReviews(int movieId){
        if(QueryUtils.isOnline(this)) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(QueryUtils.BASE_URI_MOVIES).build();
            MoviesService moviesService = retrofit.create(MoviesService.class);
            moviesService.getReviewsJson(movieId).enqueue(new Callback<ResponseBody>() {
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
