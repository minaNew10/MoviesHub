package android.example.com.movieshub.Utils;

import android.content.Context;
import android.example.com.movieshub.Model.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.net.Uri;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

   
    public static final String BASE_URI_MOVIES = "https://api.themoviedb.org/3/";
    private static final String IMAGE_MOVIES_API_URL = "http://image.tmdb.org/t/p/";



    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String buildPosterUrl(String specificPath){
        Uri baseUrl = Uri.parse(IMAGE_MOVIES_API_URL);
        Uri.Builder uriBuilder = baseUrl.buildUpon();
        uriBuilder.appendEncodedPath("w185");
        uriBuilder.appendEncodedPath(specificPath)
                .build();
        return  uriBuilder.toString();
    }

}
