package android.example.com.movieshub.Model;


import android.net.Uri;

import java.io.Serializable;

public class Movie implements Serializable {
    private int id;
    private String title;
    private String poster_path;
    private String overview;
    private String vote_average;
    private String release_date;

    private static final String IMAGE_MOVIES_API_URL = "http://image.tmdb.org/t/p/";

    public Movie(int id, String title, String poster_path, String overview, String vote_average, String release_date) {
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return buildPosterUrl(poster_path);
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String buildPosterUrl(String specificPath){
        Uri baseUrl = Uri.parse(IMAGE_MOVIES_API_URL);
        Uri.Builder uriBuilder = baseUrl.buildUpon();
        uriBuilder.appendEncodedPath("w185");
        uriBuilder.appendEncodedPath(specificPath)
                .build();
        return  uriBuilder.toString();
    }
}

