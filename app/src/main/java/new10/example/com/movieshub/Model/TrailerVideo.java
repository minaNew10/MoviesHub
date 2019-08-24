package new10.example.com.movieshub.Model;

import android.net.Uri;

public class TrailerVideo {
    private String name;
    private String key;
    private final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";

    public TrailerVideo(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return buildVideoUrl();
    }



    private Uri buildVideoUrl(){
        Uri baseUrl = Uri.parse(YOUTUBE_BASE_URL);
        Uri.Builder uriBuilder = baseUrl.buildUpon();
        uriBuilder.appendQueryParameter("v",key);
        return  uriBuilder.build();
    }
}
