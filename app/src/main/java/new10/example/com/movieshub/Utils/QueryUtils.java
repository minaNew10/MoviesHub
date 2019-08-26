package new10.example.com.movieshub.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.net.Uri;

public class QueryUtils {

   
    public static final String BASE_URI_MOVIES = "https://api.themoviedb.org/3/";
    private static final String IMAGE_MOVIES_API_URL = "http://image.tmdb.org/t/p/";

    public static final String QUERY_SORT_BY_POPULARITY = "popularity.desc";
    public static final String QUERY_SORT_BY_RATING = "vote_average.desc";


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
