package android.example.com.movieshub.Model;

import java.util.List;

public class VideosList {
    private List<TrailerVideo> results;

    public VideosList(List<TrailerVideo> results) {
        this.results = results;
    }

    public List<TrailerVideo> getResults() {
        return results;
    }

    public void setResults(List<TrailerVideo> results) {
        this.results = results;
    }
}
