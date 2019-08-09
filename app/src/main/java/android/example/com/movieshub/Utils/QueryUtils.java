package android.example.com.movieshub.Utils;

import android.content.Context;
import android.example.com.movieshub.Model.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private static final String TAG = "QueryUtils";

    private static final String TITLE = "title";
    private static final String POSTER = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String RATING = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String NOT_AVAILABLE = "Data not available";
private static final String RESULTS = "results";

    public static List<Movie> fetchMoviesList(String url){
        Log.i(TAG, "fetchMoviesList: " + url);
        URL urlObject = createUrl(url);

        String jsonResponse = null;
        try {
            jsonResponse = makeRequest(urlObject);
        } catch (IOException e) {
            Log.e(TAG, "fetchNewsList: error in closing input stream" );
        }
        List<Movie> MoviesList = extractMoviesFromJson(jsonResponse);
        return MoviesList;

    }

    private static URL createUrl(String url){
        URL urlObject = null;

        try {
            urlObject= new URL(url);
        } catch (MalformedURLException e) {
            Log.d(TAG, "createUrl: ");
        }

        return urlObject;
    }

    private static String makeRequest(URL url) throws IOException {
        Log.i(TAG, "makeRequest: code " + url);
        String jsonResponse = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            Log.i(TAG, "makeRequest: code " + response.code());
            if(response.code() == 200){
                jsonResponse = response.body().string();
            }
        }catch (IOException e){
            e.printStackTrace();
            e.getMessage();
        }finally {
            if(response != null)
                response.close();
            else {

            }
        }

        Log.i(TAG, "makeRequest: 0 " + jsonResponse);
        return jsonResponse;
    }

    private static List<Movie> extractMoviesFromJson(String jsonResponse) {
       String title,overview,rating,poster,releaseDate;
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
                Log.i(TAG, "extractMoviesFromJson: " + poster);

                releaseDate = movie.optString(RELEASE_DATE,NOT_AVAILABLE);
                Movie currMovie = new Movie(title,poster,overview,rating,releaseDate);
                movies.add(currMovie);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }


}
