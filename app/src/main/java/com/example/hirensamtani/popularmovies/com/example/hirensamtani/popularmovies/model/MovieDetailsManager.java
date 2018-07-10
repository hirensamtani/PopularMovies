package com.example.hirensamtani.popularmovies.com.example.hirensamtani.popularmovies.model;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hirensamtani.popularmovies.R;
import com.example.hirensamtani.popularmovies.TrailerListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hirensamtani on 7/2/16.
 */
public class MovieDetailsManager extends AsyncTask<Void,Void,Void> {
    private static MovieBean movieBean;
    private ArrayList movieTrailerList;
    private Context context;
    private static ListView trailerList;

    public MovieDetailsManager(Context context, ListView trailerList, MovieBean movieBean){
        this.context=context;
        this.trailerList=trailerList;
        this.movieBean=movieBean;
    }

    public ArrayList getMovieTrailerList(){
        return this.movieTrailerList;
    }




    public ArrayList getMovieTrailerList(MovieBean movieBean) {
        ArrayList retList = new ArrayList();
        String retJsonList = null;
        this.movieBean=movieBean;

        Map movieMap = new HashMap();

        HttpURLConnection connection = null;
        Uri.Builder dataUri = new Uri.Builder();



        if(movieBean.getSortBy().equals(context.getString(R.string.favourites_sort_order))){
            MovieDBHelper movieDBHelper = new MovieDBHelper(context);
            retList=movieDBHelper.getMovieTrailerList(movieBean.getMovieID());
        }
        else{

        dataUri.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieBean.getMovieID())
                .appendPath("videos")
                .appendQueryParameter("api_key", context.getString(R.string.api_key)); //api key here





        URL dataUrl = null;
        try {
            dataUrl = new URL(dataUri.toString());
            connection = (HttpURLConnection) dataUrl.openConnection();
            connection.connect();
            connection.setConnectTimeout(10000);
            int status = connection.getResponseCode();

            if(status!=HttpURLConnection.HTTP_OK){
                return null;
            }



            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String responseString;
            StringBuilder sb = new StringBuilder();
            while ((responseString = reader.readLine()) != null) {
                sb = sb.append(responseString);
            }
            retJsonList = sb.toString();



            JSONObject data = new JSONObject(retJsonList);
            JSONArray movieArray = data.optJSONArray("results");



            for(int i = 0; i < movieArray.length(); i++) {
                JSONObject movieTrailer=(JSONObject) movieArray.get(i);
                movieMap = new HashMap();
                movieMap.put("name",movieTrailer.getString("name"));
                movieMap.put("key", movieTrailer.getString("key"));

                retList.add(movieMap);

            }



        } catch (MalformedURLException e) {
            Log.e("Exception:- ", Log.getStackTraceString(e));
        } catch (IOException e) {
            Log.e("Exception:- ", Log.getStackTraceString(e));
        } catch (JSONException e) {
            Log.e("Exception:- ", Log.getStackTraceString(e));
        }
        }





        return retList;

    }

    protected void onPreExecute() {
       super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... params) {
        this.movieTrailerList = getMovieTrailerList(movieBean);
        return null;
    }

    protected void onPostExecute(Void result) {
       super.onPostExecute(result);
        if(movieTrailerList!=null&&movieTrailerList.size()>0) {
            TrailerListAdapter trailerListAdapter = new TrailerListAdapter(this.context, movieTrailerList);
            this.trailerList.setAdapter(trailerListAdapter);
            this.trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView textView = (TextView) view.findViewById(R.id.movie_trailer_name);

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.youtubeURL)+textView.getTag().toString()));
            context.startActivity(browserIntent);
                }
            });

        }

    }
}
