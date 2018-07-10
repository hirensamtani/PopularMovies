package com.example.hirensamtani.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by hirensamtani on 28/2/16.
 */
public class TrailerListAdapter  extends ArrayAdapter{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList trailerUrls;
    private Map movieTrailerMap;



    public TrailerListAdapter(Context context, ArrayList trailerUrls) {

        super(context, R.layout.layout_movie_trailer_list, trailerUrls);
        this.context = context;
        this.trailerUrls = trailerUrls;

        inflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.layout_movie_trailer_list, parent, false);
        }

        movieTrailerMap =(Map)trailerUrls.get(position);

        TextView movieText = (TextView) convertView.findViewById(R.id.movie_trailer_name);
        ImageView imgPlayButton =(ImageView) convertView.findViewById(R.id.play_button_img);

        movieText.setText(movieTrailerMap.get("name").toString());

        movieText.setTag(movieTrailerMap.get("key").toString());
        imgPlayButton.setTag(movieTrailerMap.get("key").toString());

        return convertView;
    }




}
