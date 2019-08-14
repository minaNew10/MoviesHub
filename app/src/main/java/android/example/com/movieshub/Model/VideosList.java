package android.example.com.movieshub.Model;

import java.util.List;

public class VideosList {
    private List<Video> results;

    public VideosList(List<Video> results) {
        this.results = results;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
