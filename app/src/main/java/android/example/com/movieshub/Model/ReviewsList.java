package android.example.com.movieshub.Model;

import java.util.List;

public class ReviewsList {
    private List<Review> results;

    public ReviewsList(List<Review> results) {
        this.results = results;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
