package com.example.hirensamtani.popularmovies.com.example.hirensamtani.popularmovies.model;

import android.app.ProgressDialog;
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

import com.example.hirensamtani.popularmovies.MovieReviewListAdapter;
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
 * Created by hirensamtani on 5/3/16.
 */
public class MovieReviewManager extends AsyncTask<Void,Void,Void> {
    private MovieBean movieBean;
    private ArrayList reviewList;
    private ProgressDialog dialog;
    private Context context;
    private ListView movieReviewList;
    private boolean emptyString=true;

    public MovieReviewManager(Context context, ListView movieReviewList, MovieBean movieBean){
        this.context=context;
        this.movieReviewList=movieReviewList;
        this.movieBean=movieBean;
    }


    public ArrayList getMovieReviewList(MovieBean movieBean) {
        ArrayList retList = new ArrayList();
        String retJsonList = null;
        this.movieBean=movieBean;

        Map movieMap = new HashMap();

        HttpURLConnection connection = null;
        Uri.Builder dataUri = new Uri.Builder();



        dataUri.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieBean.getMovieID())
                .appendPath("reviews")
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
                JSONObject movieReview=(JSONObject) movieArray.get(i);
                movieMap = new HashMap();
                movieMap.put("author",movieReview.getString("author"));
                movieMap.put("content", movieReview.getString("content"));
                movieMap.put("url", movieReview.getString("url"));

                retList.add(movieMap);

            }



        } catch (IOException e) {
            emptyString=false;
            Log.e("Exception:- ", Log.getStackTraceString(e));
        } catch (JSONException e) {
            Log.e("Exception:- ", Log.getStackTraceString(e));
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
        this.reviewList = getMovieReviewList(movieBean);
        return null;
    }

    protected void onPostExecute(Void result) {
        // dismiss progress dialog and update ui
        super.onPostExecute(result);
        if(reviewList!=null&&reviewList.size()>0) {
            MovieReviewListAdapter movieReviewListAdapter = new MovieReviewListAdapter(this.context, reviewList);
            this.movieReviewList.setAdapter(movieReviewListAdapter);
            this.movieReviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView textView = (TextView) view.findViewById(R.id.movieReviewer);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(textView.getTag().toString()));
            context.startActivity(browserIntent);
                }
            });

        }


        else{
            int duration = Toast.LENGTH_SHORT;
            String emptyToastMessage= context.getString(R.string.connection_error);
            if(emptyString){
                emptyToastMessage = context.getString(R.string.empty_movie_review_list);
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
