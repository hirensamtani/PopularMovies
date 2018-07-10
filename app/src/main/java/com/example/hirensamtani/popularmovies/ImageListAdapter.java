package com.example.hirensamtani.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.hirensamtani.popularmovies.CommonUtils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by hirensamtani on 9/2/16.
 */
public class ImageListAdapter extends ArrayAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList imageUrls;
    private Map movieMap;
    private String sortBy;



    public ImageListAdapter(Context context, ArrayList imageUrls,String sortBy) {

        super(context, R.layout.layout_picassa, imageUrls);
        this.context = context;
        this.imageUrls = imageUrls;
        this.sortBy = sortBy;

        inflater = LayoutInflater.from(context);



    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        if (null == convertView) {
            convertView = inflater.inflate(R.layout.layout_picassa, parent, false);
        }

        movieMap =(Map)imageUrls.get(position);

        ImageView imgView =(ImageView) convertView.findViewById(R.id.imageViewMoviePicassa);
        imgView.setTag(movieMap.get("movieJSON"));
        CommonUtils commonUtils = new CommonUtils();

        if(sortBy.equals(context.getString(R.string.favourites_sort_order)))
        {
            String storagePath = commonUtils.getAppStoragePath(context);

            JSONObject movieData = null;
            try {
                movieData = new JSONObject(movieMap.get("movieJSON").toString());
                File file = new File(storagePath + movieData.getString("poster_path"));
                commonUtils.loadImage(context, file, imgView);
            } catch (JSONException e) {
                Log.e("Exception:- ", Log.getStackTraceString(e));
            }

        }
        else {
            commonUtils.loadImage(context, movieMap.get("poster_path").toString(), imgView);
        }
        return convertView;
    }


}
