package com.example.hirensamtani.popularmovies.com.example.hirensamtani.popularmovies.model;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.example.hirensamtani.popularmovies.ImageListAdapter;
import com.example.hirensamtani.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hirensamtani on 7/2/16.
 */
public class MovieManager extends AsyncTask<Void,Void,Void> {
    private MovieBean movieBean;
    private ArrayList movieList;
    private ProgressDialog dialog;
    private Context context;
    private GridView gridView;
    private boolean emptyString = true;

    public MovieManager(Context context,GridView gridView,MovieBean movieBean){
        this.context=context;
        this.gridView=gridView;
        this.movieBean=movieBean;
    }


    public ArrayList getMovieList(){
        return this.movieList;
    }



    public ArrayList getMovieArrayList(MovieBean movieBean) {
        ArrayList retList = new ArrayList();
        String retJsonList = null;
        this.movieBean=movieBean;

        Map movieMap = new HashMap();

        HttpURLConnection connection = null;
        Uri.Builder dataUri = new Uri.Builder();



        if(movieBean.getSortBy().equals(context.getString(R.string.favourites_sort_order))){
            MovieDBHelper movieDBHelper = new MovieDBHelper(context);

            retList=movieDBHelper.getFavouriteMovieList();
        }

        else{

        dataUri.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("sort_by",movieBean.getSortBy())
                .appendQueryParameter("api_key", context.getString(R.string.api_key));





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
                JSONObject poster=(JSONObject) movieArray.get(i);
                movieMap = new HashMap();
                movieMap.put("poster_path",context.getString(R.string.poster_path)+poster.getString("poster_path"));
                movieMap.put("movieJSON", poster.toString());

                retList.add(movieMap);

            }



        } catch (IOException e) {
            Log.e("Exception:- ", Log.getStackTraceString(e));
            emptyString = false;
        } catch (JSONException e) {
            Log.e("Exception:- ", Log.getStackTraceString(e));
        }
        }





        return retList;

    }

    protected void onPreExecute() {
        //display progress dialog.

        super.onPreExecute();
        dialog = new ProgressDialog(this.context);
        dialog.setMessage(context.getString(R.string.dialog_message));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


    }

    @Override
    protected Void doInBackground(Void... params) {
        movieList = getMovieArrayList(movieBean);
        return null;
    }

    protected void onPostExecute(Void result) {
        // dismiss progress dialog and update ui
        super.onPostExecute(result);
        if(movieList!=null&&movieList.size()>0)
            gridView.setAdapter(new ImageListAdapter(this.context, movieList,movieBean.getSortBy()));

        else{
            int duration = Toast.LENGTH_LONG;

            String emptyToastMessage= context.getString(R.string.connection_error);
            if(emptyString){
                emptyToastMessage = context.getString(R.string.empty_movie_list);
            }

            Toast toast = Toast.makeText(context,emptyToastMessage , duration);
            toast.show();
        }


        try{
            if(dialog.isShowing())
                dialog.dismiss();

        }catch (IllegalArgumentException ie){
            Log.e("Exception:- ", Log.getStackTraceString(ie));
        }
    }
}
