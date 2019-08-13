package android.example.com.movieshub.Utils;

import android.content.Context;
import android.example.com.movieshub.Model.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static final String TAG = "QueryUtils";
    public static final String BASE_URI_MOVIES = "https://api.themoviedb.org/3/";
    private static final String TITLE = "title";
    private static final String POSTER = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String RATING = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String RESULTS = "results";
    private static final String ID = "id";
    private static final int DEFAULT_ID = 0;
    private static final String NOT_AVAILABLE = "Data not available";


    public static List<Movie> extractMoviesFromJson(String jsonResponse) {
       String title,overview,rating,poster,releaseDate;
       int id;
       List<Movie> movies = new ArrayList<>();
        try {
            JSONObject rootJsonObject = new JSONObject(jsonResponse);
            JSONArray results = rootJsonObject.optJSONArray(RESULTS);
            for(int i = 0; i < results.length(); i++){
                JSONObject movie = results.getJSONObject(i);
                title = movie.optString(TITLE,NOT_AVAILABLE);
                overview = movie.optString(OVERVIEW,NOT_AVAILABLE);
                rating = movie.optString(RATING,NOT_AVAILABLE);
                poster = movie.optString(POSTER,NOT_AVAILABLE);
                id = movie.optInt(ID,DEFAULT_ID);
                releaseDate = movie.optString(RELEASE_DATE,NOT_AVAILABLE);
                Movie currMovie = new Movie(id,title,poster,overview,rating,releaseDate);
                movies.add(currMovie);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
