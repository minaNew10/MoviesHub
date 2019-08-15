package android.example.com.movieshub;

import android.content.Intent;
import android.example.com.movieshub.Database.AppDatabase;

import android.example.com.movieshub.Model.*;
import android.example.com.movieshub.Utils.MoviesService;
import android.example.com.movieshub.Utils.QueryUtils;
import android.example.com.movieshub.ViewHolder.ReviewsAdapter;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String TAG = "MovieDetailActivity";
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView imgvMovie,imgvPlay,imgvStar;
    TextView txtv_overview,txtv_release_date,label_reviews;
    RatingBar ratingBar;
    RecyclerView reviewsRecycler;
    List<Review> reviews = new ArrayList<>();
    ReviewsAdapter adapter;
    Movie movie;
    //Member variable for the database
    AppDatabase appDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        Intent intent = getIntent();
        movie = (Movie) intent.getSerializableExtra("movie");
        populateUI(movie);
        requestReviews(movie.getId());


    }


    public void populateUI(final Movie movie){
        collapsingToolbarLayout = findViewById(R.id.coll_toolbar_movie_detail);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsingToolbarLayoutExpandedTextStyle);

        imgvMovie = findViewById(R.id.imgv_movie_detail);
        imgvMovie.setAdjustViewBounds(true);

        imgvPlay = findViewById(R.id.imgv_play_button);

        imgvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestVideo(movie.getId());
            }
        });

        imgvStar = findViewById(R.id.imgv_star);

        imgvStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleStarImg(false);
            }
        });
        collapsingToolbarLayout.setTitle(movie.getTitle());
        Picasso.get().load(QueryUtils.buildPosterUrl(movie.getPoster_path())).into(imgvMovie);

        txtv_overview = findViewById(R.id.txtv_movie_overview);
        txtv_overview.setText(movie.getOverview());

        txtv_release_date = findViewById(R.id.txtv_release_date);
        txtv_release_date.setText(movie.getRelease_date());

        label_reviews = findViewById(R.id.txtv_reviews_label);

        ratingBar = findViewById(R.id.rating_bar);
        ratingBar.setIsIndicator(true);
        ratingBar.setRating(Float.parseFloat(movie.getVote_average()) - 5);

        reviewsRecycler = findViewById(R.id.recycler_view_reviews);
        adapter = new ReviewsAdapter(reviews);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);

        reviewsRecycler.setLayoutManager(layoutManager);
        reviewsRecycler.addItemDecoration(decoration);
        reviewsRecycler.setAdapter(adapter);
        reviewsRecycler.setVisibility(View.GONE);

    }

    private void handleStarImg(boolean isFav) {
        if(!isFav){
            imgvStar.setImageResource(R.drawable.star_fav);
            Log.i(TAG, "handleStarImg: insert" + movie.getPoster_path());
            appDatabase.movieDao().insertMovie(movie);

        }else{
            imgvStar.setImageResource(R.drawable.star);

        }
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
                    if (reviews == null || reviews.size() == 0) {
                        label_reviews.setText(getString(R.string.no_reviews_available));
                    }else {
                        label_reviews.setText(getString(R.string.reviews_label));
                        reviewsRecycler.setVisibility(View.VISIBLE);
                        adapter.setReviews(reviews);
                    }
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
            Retrofit retrofit = new Retrofit
                    .Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(QueryUtils.BASE_URI_MOVIES).build();
            MoviesService moviesService = retrofit.create(MoviesService.class);
            moviesService.getVideosList(movieId).enqueue(new Callback<VideosList>() {

                @Override
                public void onResponse(Call<VideosList> call, Response<VideosList> response) {
                    List<TrailerVideo> list = response.body().getResults();
                    if(list == null || list.size() == 0){
                        Toast.makeText(getApplicationContext(),getString(R.string.no_available_video),Toast.LENGTH_LONG).show();
                        imgvPlay.setVisibility(View.GONE);
                        imgvMovie.setAlpha(Float.parseFloat("1"));
                    }else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(list.get(0).getUri());
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<VideosList> call, Throwable t) {

                }


            });
        }else {
            Toast.makeText(this,getString(R.string.network_err),Toast.LENGTH_LONG).show();
        }
        return ;
    }


}
