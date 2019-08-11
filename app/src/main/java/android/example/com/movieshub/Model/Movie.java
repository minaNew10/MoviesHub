package android.example.com.movieshub.Model;


import android.net.Uri;

import java.io.Serializable;

public class Movie implements Serializable {
    private String title;
    private String poster;
    private String overview;
    private String rating;
    private String releaseDate;
    private static final String MOVIES_API_URL = "http://image.tmdb.org/t/p/";

    public Movie(String title, String poster, String overview, String rating, String releaseDate) {
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return buildPosterUrl(poster);
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
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
