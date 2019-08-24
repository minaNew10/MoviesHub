package new10.example.com.movieshub;

import android.content.Intent;
import butterknife.BindView;
import butterknife.ButterKnife;
import new10.example.com.movieshub.Database.AppDatabase;

import new10.example.com.movieshub.Model.*;
import android.example.com.movieshub.R;
import new10.example.com.movieshub.Utils.QueryUtils;
import new10.example.com.movieshub.ViewHolder.ReviewsAdapter;


import new10.example.com.movieshub.ViewModel.MovieDetailViewModel;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.*;

import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;
import new10.example.com.movieshub.Model.*;

import java.util.ArrayList;
import java.util.List;

import static androidx.appcompat.widget.LinearLayoutCompat.VERTICAL;



public class MovieDetailActivity extends AppCompatActivity {
    private static final String TAG = "MovieDetailActivity";
    @BindView(R.id.coll_toolbar_movie_detail) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.imgv_movie_detail)ImageView imgvMovie;
    @BindView(R.id.imgv_play_button)ImageView imgvPlay;
    @BindView(R.id.imgv_star)ImageView imgvStar;
    @BindView(R.id.imgv_share)ImageView imgvShare;
    @BindView(R.id.txtv_movie_overview) TextView txtv_overview;
    @BindView(R.id.txtv_release_date)TextView txtv_release_date;
    @BindView(R.id.txtv_reviews_label)TextView label_reviews;
    @BindView(R.id.rating_bar) RatingBar ratingBar;
    @BindView(R.id.recycler_view_reviews) RecyclerView reviewsRecycler;
    List<Review> reviews = new ArrayList<>();
    List<TrailerVideo> trailers = new ArrayList<>();
    ReviewsAdapter adapter;
    Movie movie;
    //Member variable for the database
    AppDatabase appDatabase;
    @BindView(R.id.toolbar_movie_detail) Toolbar toolbar;
    //a flag to determine whether to add the film to fav or remove it
    boolean isFav =false;
    MovieDetailViewModel movieDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        Intent intent = getIntent();
        movie = intent.getExtras().getParcelable(getString(R.string.key_for_passing_movie));
        setupViewModel();
        populateUI(movie);


    }

    private void setupViewModel() {
        movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
        MutableLiveData<ReviewsList> currReviewsList = movieDetailViewModel.getMoviesReviews(movie.getId());
        MutableLiveData<VideosList> currTrailersList = movieDetailViewModel.getTrailerMovies(movie.getId());

        currReviewsList.observe(this, new Observer<ReviewsList>() {
            @Override
            public void onChanged(ReviewsList reviewsList) {
                reviews = reviewsList.getResults();
                if (reviews == null || reviews.size() == 0) {
                    label_reviews.setText(getString(R.string.no_reviews_available));
                }else {
                    label_reviews.setText(getString(R.string.reviews_label));
                    reviewsRecycler.setVisibility(View.VISIBLE);
                    adapter.setReviews(reviews);
                }

            }
        });

        currTrailersList.observe(this, new Observer<VideosList>() {
            @Override
            public void onChanged(VideosList videosList) {
                 trailers = videosList.getResults();
                 if(trailers == null || trailers.size() == 0){
                     imgvMovie.setAlpha(Float.parseFloat("1"));
                 }else {
                     imgvShare.setVisibility(View.VISIBLE);
                     imgvPlay.setVisibility(View.VISIBLE);
                 }

            }
        });

//        movieDetailViewModel.loadImageIntoView(QueryUtils.buildPosterUrl(movie.getPoster_path()),imgvMovie);

    }
    //this method to set the image of star according to the movie
    private void setStarButtonResource() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
               final Movie resultMovie = appDatabase.movieDao().loadMovieById(movie.getId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(resultMovie!= null && movie.getId() == resultMovie.getId()){
                            imgvStar.setImageResource(R.drawable.star_fav);
                            isFav = true;
                        }else {
                            imgvStar.setImageResource(R.drawable.star);
                        }
                    }
                });
            }
        });
    }


    //override this to allow back effect on up button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void populateUI(final Movie movie){

        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsingToolbarLayoutExpandedTextStyle);

        imgvMovie.setAdjustViewBounds(true);

        imgvPlay.setVisibility(View.GONE);
        imgvShare.setVisibility(View.GONE);
        imgvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(trailers.get(0).getUri());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        imgvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleShareImageClick(trailers.get(0).getUri());
            }
        });

        setStarButtonResource();
        imgvStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleStarImgClick();
            }
        });

        collapsingToolbarLayout.setTitle(movie.getTitle());
        Picasso.get().load(QueryUtils.buildPosterUrl(movie.getPoster_path())).into(imgvMovie);

        txtv_overview.setText(movie.getOverview());

        txtv_release_date.setText(movie.getRelease_date());

        ratingBar.setIsIndicator(true);
        ratingBar.setRating(Float.parseFloat(movie.getVote_average()) - 5);

        adapter = new ReviewsAdapter(reviews);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);

        reviewsRecycler.setLayoutManager(layoutManager);
        reviewsRecycler.addItemDecoration(decoration);
        reviewsRecycler.setAdapter(adapter);
        reviewsRecycler.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

    }

    private void handleStarImgClick() {

        if(!isFav) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.movieDao().insertMovie(movie);
                    isFav = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),getString(R.string.movie_saved_to_fav),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            imgvStar.setImageResource(R.drawable.star_fav);
        }else {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.movieDao().removeMovie(movie);
                    isFav = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),getString(R.string.movie_removed_from_fav),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

            imgvStar.setImageResource(R.drawable.star);
        }


    }

    private void handleShareImageClick(Uri uri) {
        String mimeType = "video/mp4";
        // This is just the title of the window that will pop up when we call startActivity
        String title = getString(R.string.choose_app_to_share);
        ShareCompat.IntentBuilder
                        .from(this)
                        .setStream(uri)
                        .setType(mimeType)
                        .setChooserTitle(title)
                        .startChooser();


    }


}
