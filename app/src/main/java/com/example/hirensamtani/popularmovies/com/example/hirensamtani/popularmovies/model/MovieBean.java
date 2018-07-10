package com.example.hirensamtani.popularmovies.com.example.hirensamtani.popularmovies.model;

/**
 * Created by hirensamtani on 7/2/16.
 */
public class MovieBean {
    private String sortBy="popularity.desc";
    private String movieID;


    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getSortBy(){
        return this.sortBy;
    }

    public void setSortBy(String sortBy){
        this.sortBy = sortBy;
    }



}
