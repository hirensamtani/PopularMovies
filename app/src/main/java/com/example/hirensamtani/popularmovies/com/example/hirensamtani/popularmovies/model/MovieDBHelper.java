package com.example.hirensamtani.popularmovies.com.example.hirensamtani.popularmovies.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.hirensamtani.popularmovies.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hirensamtani on 6/3/16.
 */
public class MovieDBHelper  extends SQLiteOpenHelper{
    public Context context;
    public static final String DATABASE_NAME="MovieDBName.db";
    public static final String MOVIE_DETAILS_TABLE_NAME="movieDetail";
    public static final String MOVIE_DETAILS_COLUMN_MOVIE_ID="movieID";
    public static final String MOVIE_DETAILS_COLUMN_MOVIE_JSON="movieJSON";

    public static final String MOVIE_TRAILER_TABLE_NAME="movieTrailer";
    public static final String MOVIE_TRAILER_COLUMN_MOVIE_ID="movieID";
    public static final String MOVIE_TRAILER_COLUMN_TRAILER_NAME="trailer_name";
    public static final String MOVIE_TRAILER_COLUMN_TRAILER_KEY="trailer_key";



    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +MOVIE_DETAILS_TABLE_NAME+
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                MOVIE_DETAILS_COLUMN_MOVIE_ID+" TEXT UNIQUE NOT NULL, " +
                MOVIE_DETAILS_COLUMN_MOVIE_JSON+" TEXT " +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS "+MOVIE_TRAILER_TABLE_NAME+
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                MOVIE_TRAILER_COLUMN_MOVIE_ID+" TEXT," +
                MOVIE_TRAILER_COLUMN_TRAILER_NAME+" TEXT," +
                MOVIE_TRAILER_COLUMN_TRAILER_KEY+" TEXT," +
                "FOREIGN KEY("+MOVIE_TRAILER_COLUMN_MOVIE_ID+") REFERENCES "
                +MOVIE_DETAILS_TABLE_NAME+"("+MOVIE_DETAILS_COLUMN_MOVIE_ID+") " +
                ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+MOVIE_TRAILER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+MOVIE_DETAILS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertMovieDetails  (String movieID, String movieJSON)
    {

        String[] movieIDArg= {movieID};
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVIE_DETAILS_COLUMN_MOVIE_ID, movieID);
        contentValues.put(MOVIE_DETAILS_COLUMN_MOVIE_JSON, movieJSON);
        db.delete(MOVIE_DETAILS_TABLE_NAME, MOVIE_DETAILS_COLUMN_MOVIE_ID+" = ? ", movieIDArg);
        db.insert(MOVIE_DETAILS_TABLE_NAME, null, contentValues);

        removeMovieTrailerDetails(movieID);

        return true;
    }

    public boolean insertMovieTrailerDetails  (String movieID, String trailer_name,String trailer_key)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVIE_TRAILER_COLUMN_MOVIE_ID, movieID);
        contentValues.put(MOVIE_TRAILER_COLUMN_TRAILER_NAME, trailer_name);
        contentValues.put(MOVIE_TRAILER_COLUMN_TRAILER_KEY, trailer_key);
        db.insert(MOVIE_TRAILER_TABLE_NAME, null, contentValues);

        return true;
    }


    public ArrayList getFavouriteMovieList()
    {
        ArrayList array_list = new ArrayList();
        Map movieDetailMap;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select "+MOVIE_DETAILS_COLUMN_MOVIE_ID+
                ", "+MOVIE_DETAILS_COLUMN_MOVIE_JSON+" from "+MOVIE_DETAILS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            movieDetailMap = new HashMap();
            movieDetailMap.put("movieID",res.getString(res.getColumnIndex(MOVIE_DETAILS_COLUMN_MOVIE_ID)));
            movieDetailMap.put("movieJSON", res.getString(res.getColumnIndex(MOVIE_DETAILS_COLUMN_MOVIE_JSON)));

            try {
                JSONObject poster= new JSONObject(movieDetailMap.get("movieJSON").toString());
                movieDetailMap.put("poster_path",context.getString(R.string.poster_path)+poster.getString("poster_path"));

            } catch (JSONException e) {
                Log.e("Exception:- ", Log.getStackTraceString(e));
            }


            array_list.add(movieDetailMap);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getMovieTrailerList(String movieID)
    {
        ArrayList array_list = new ArrayList();
        Map movieDetailMap;
        String[] movieTrailerArgs = {movieID};


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select "+MOVIE_TRAILER_COLUMN_TRAILER_NAME+
                ", "+MOVIE_TRAILER_COLUMN_TRAILER_KEY +
                " from "+MOVIE_TRAILER_TABLE_NAME+" where "
                +MOVIE_TRAILER_COLUMN_MOVIE_ID+" LIKE ?", movieTrailerArgs);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            movieDetailMap = new HashMap();
            movieDetailMap.put("name",res.getString(res.getColumnIndex(MOVIE_TRAILER_COLUMN_TRAILER_NAME)));
            movieDetailMap.put("key", res.getString(res.getColumnIndex(MOVIE_TRAILER_COLUMN_TRAILER_KEY)));


            array_list.add(movieDetailMap);
            res.moveToNext();
        }
        return array_list;
    }

    public boolean removeMovieTrailerDetails(String movieID)
    {
        String[] movieIDArg= {movieID};
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(MOVIE_TRAILER_TABLE_NAME, MOVIE_TRAILER_COLUMN_MOVIE_ID+" = ? ", movieIDArg);
        return true;
    }

    public boolean removeMovieDetails(String movieID)
    {
        String[] movieIDArg= {movieID};
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(MOVIE_DETAILS_TABLE_NAME, MOVIE_DETAILS_COLUMN_MOVIE_ID+" = ? ", movieIDArg);
        return true;
    }

    public int checkMovieIDCount(String movieID)
    {
        int movieIDCount = 0;
        String[] movieTrailerArgs = {movieID};


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select COUNT("+MOVIE_DETAILS_COLUMN_MOVIE_ID+") from "
                +MOVIE_DETAILS_TABLE_NAME
                +" where "+MOVIE_DETAILS_COLUMN_MOVIE_ID+" LIKE ?", movieTrailerArgs);
        res.moveToFirst();
        movieIDCount= res.getInt(0);


        return movieIDCount;
    }
}
