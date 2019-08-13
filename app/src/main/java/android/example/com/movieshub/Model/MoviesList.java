package android.example.com.movieshub.Model;

import java.util.List;

public class MoviesList {
    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public MoviesList(List<Movie> results) {
        this.results = results;
    }
}
