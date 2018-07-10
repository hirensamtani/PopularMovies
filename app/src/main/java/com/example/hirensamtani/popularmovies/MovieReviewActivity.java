package com.example.hirensamtani.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.example.hirensamtani.popularmovies.com.example.hirensamtani.popularmovies.model.MovieBean;
import com.example.hirensamtani.popularmovies.com.example.hirensamtani.popularmovies.model.MovieReviewManager;

public class MovieReviewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Context context = getApplicationContext();
        ListView movieReviewList = (ListView) findViewById(R.id.movieReviewList);

        Intent intent = getIntent();
        String movieID=intent.getStringExtra("movieID");

        MovieBean movieBean = new MovieBean();
        movieBean.setMovieID(movieID);
        MovieReviewManager movieDetailsManager = new MovieReviewManager(MovieReviewActivity.this,movieReviewList,movieBean);
        movieDetailsManager.execute();

    }

}
