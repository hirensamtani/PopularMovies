package com.example.hirensamtani.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by hirensamtani on 5/3/16.
 */
public class MovieReviewListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList movieReviewList;
    private Map movieReviewMap;



    public MovieReviewListAdapter(Context context, ArrayList movieReviewList) {

        super(context, R.layout.layout_movie_review, movieReviewList);
        this.context = context;
        this.movieReviewList = movieReviewList;

        inflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.layout_movie_review, parent, false);
        }

        movieReviewMap =(Map)movieReviewList.get(position);

        TextView movieReviewer = (TextView) convertView.findViewById(R.id.movieReviewer);
        TextView movieReview = (TextView) convertView.findViewById(R.id.movieReview);



        movieReviewer.setText(movieReviewMap.get("author").toString());
        movieReviewer.setTag(movieReviewMap.get("url").toString());
        movieReview.setText(movieReviewMap.get("content").toString());


        return convertView;
    }
}
