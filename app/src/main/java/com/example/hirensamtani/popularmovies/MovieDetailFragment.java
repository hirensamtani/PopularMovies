package com.example.hirensamtani.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hirensamtani.popularmovies.CommonUtils.CommonUtils;
import com.example.hirensamtani.popularmovies.com.example.hirensamtani.popularmovies.model.MovieBean;
import com.example.hirensamtani.popularmovies.com.example.hirensamtani.popularmovies.model.MovieDBHelper;
import com.example.hirensamtani.popularmovies.com.example.hirensamtani.popularmovies.model.MovieDetailsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static String movieID;
    private static String movieJSON;
    private MovieDBHelper movieDBHelper;
    private static List movieTrailerList;
    private static MovieDetailsManager movieDetailsManager;



    private OnFragmentInteractionListener mListener;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieDetailFragment newInstance(String param1, String param2) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        return fragment;
    }

    public String getMovieJSON(){
        return this.movieJSON;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        final Context context = rootView.getContext();


        Intent intent = getActivity().getIntent();
        this.movieJSON=intent.getStringExtra("movieJSON");

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(context);
        final String sortBy=sharedPref.getString("movie_sort_order", this.getString(R.string.default_sort_order));


        JSONObject movieData = null;

        TextView textViewMovieName = (TextView) rootView.findViewById(R.id.textViewMovieName);
        TextView textViewReleaseDate = (TextView) rootView.findViewById(R.id.textViewReleaseDate);
        TextView textViewRating = (TextView) rootView.findViewById(R.id.textViewRating);
        ImageView imageViewMoviePoster = (ImageView) rootView.findViewById(R.id.imageViewMoviePoster);
        TextView textViewSynopsis = (TextView) rootView.findViewById(R.id.textViewSynopsis);
        ListView trailerList = (ListView) rootView.findViewById(R.id.trailerList);
        Button buttonMarkFavourite = (Button) rootView.findViewById(R.id.buttonMarkFavourite);



        try {
            movieData = new JSONObject(movieJSON);
            this.movieID = movieData.getString("id");
            textViewMovieName.setText(movieData.getString("title"));

            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date myDate;
                myDate = df.parse(movieData.getString("release_date"));
                df = new SimpleDateFormat("MMMM dd, yyyy ");
                String releaseDate =  df.format(myDate);
                textViewReleaseDate.setText(releaseDate);

            }catch (ParseException pe) {
                textViewReleaseDate.setText(context.getString(R.string.default_release_date_unavailable_text));
            }





            textViewRating.setText(movieData.getString("vote_average") + "/10");


            TableRow movieRatingRow = (TableRow) rootView.findViewById(R.id.movieRatingRow);
            movieRatingRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!sortBy.equals(context.getString(R.string.favourites_sort_order))) {
                        onClickMovieReview(v);
                    }
                }
            });



            movieDBHelper = new MovieDBHelper(context);
            final int movieFavouriteCnt = movieDBHelper.checkMovieIDCount(movieID);

            if((sortBy.equals(context.getString(R.string.favourites_sort_order)))||(movieFavouriteCnt>0)) {
                buttonMarkFavourite.setText(context.getString(R.string.unmark_button_rating_text));
            }


            buttonMarkFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((sortBy.equals(context.getString(R.string.favourites_sort_order))) || (movieFavouriteCnt > 0)) {
                        onClickRemoveFromFavourites(v, context, movieID, sortBy);
                    } else {
                        onClickMarkAsFavourite(v, context, sortBy);
                    }
                }
            });




            if(movieData.getString("overview").trim().equals("")){
                textViewSynopsis.setText(context.getString(R.string.unavailable_synopsis_text));
            }
            else{
                textViewSynopsis.setText(movieData.getString("overview"));
            }


            CommonUtils commonUtils = new CommonUtils();
            if(sortBy.equals(context.getString(R.string.favourites_sort_order)))
            {
                String storagePath = commonUtils.getAppStoragePath(context);

                File file = new File(storagePath + movieData.getString("backdrop_path"));

                commonUtils.loadImage(context, file, imageViewMoviePoster);

            }
            else{
                commonUtils.loadImage(context,
                        context.getString(R.string.poster_path) + movieData.getString("backdrop_path"),
                        imageViewMoviePoster);

            }


        } catch (JSONException e) {
            Log.e("Exception:- ", Log.getStackTraceString(e));
        }catch (NullPointerException ne){
            Log.e("Exception:- ", Log.getStackTraceString(ne));
        }




        MovieBean movieBean = new MovieBean();
        movieBean.setMovieID(movieID);
        movieBean.setSortBy(sortBy);
        movieDetailsManager = new MovieDetailsManager(context,trailerList,movieBean);
        movieDetailsManager.execute();

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onClickMovieReview(View view){

        Intent intent = new Intent(getActivity(),MovieReviewActivity.class);
        intent.putExtra("movieID",movieID);
        startActivity(intent);

    }


    public void onClickMarkAsFavourite(View view, final Context context, final String sortBy){
        movieDBHelper = new MovieDBHelper(context);
        Map movieTrailerMap;
        movieTrailerList = movieDetailsManager.getMovieTrailerList();




        boolean inserttrue = movieDBHelper.insertMovieDetails(movieID, movieJSON);

        for(int i=0;i<movieTrailerList.size();i++){
            movieTrailerMap= new HashMap();
            movieTrailerMap =(Map)movieTrailerList.get(i);
            movieDBHelper.insertMovieTrailerDetails(movieID,
                    movieTrailerMap.get("name").toString(),
                    movieTrailerMap.get("key").toString());

        }

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, context.getString(R.string.favourite_mark_success), duration);
        toast.show();


        Button btn = (Button)view;
        btn.setText(context.getString(R.string.unmark_button_rating_text));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRemoveFromFavourites(v,context,movieID,sortBy);

            }
        });

        CommonUtils commonUtils = new CommonUtils();
        String storagePath = commonUtils.getAppStoragePath(context);
        String moviePosterURL="";
        String movieDetailPosterURL="";

        try {
            JSONObject movieData = new JSONObject(movieJSON);
            moviePosterURL=context.getString(R.string.poster_path)+movieData.getString("poster_path");
            movieDetailPosterURL=context.getString(R.string.poster_path)+movieData.getString("backdrop_path");

            commonUtils.StoreImage(context,moviePosterURL,storagePath,movieData.getString("poster_path"));
            commonUtils.StoreImage(context, movieDetailPosterURL, storagePath, movieData.getString("backdrop_path"));


        } catch (JSONException e) {
            Log.e("Exception:- ", Log.getStackTraceString(e));
        }




    }

    public void onClickRemoveFromFavourites(View view, final Context context, final String movieID,final String sortBy){
        movieDBHelper = new MovieDBHelper(context);



        boolean inserttrue = movieDBHelper.removeMovieTrailerDetails(movieID);
        inserttrue = movieDBHelper.removeMovieDetails(movieID);

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, context.getString(R.string.favourite_unmark_success), duration);
        toast.show();

        Button btn = (Button)view;
        btn.setText(context.getString(R.string.default_button_rating_text));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMarkAsFavourite(v, context,sortBy);

            }
        });

        CommonUtils commonUtils = new CommonUtils();
        String storagePath = commonUtils.getAppStoragePath(context);
        String moviePosterURL="";
        String movieDetailPosterURL="";

        try {

            JSONObject movieData = new JSONObject(movieJSON);

            File file = new File(commonUtils.getAppStoragePath(context)+movieData.getString("poster_path"));



            commonUtils.deleteFile(context, storagePath, movieData.getString("poster_path"));
            commonUtils.deleteFile(context, storagePath, movieData.getString("backdrop_path"));

            FragmentActivity curr_activity = getActivity();

            if(sortBy.equals(context.getString(R.string.favourites_sort_order))) {
                btn.setClickable(false);
                if(curr_activity.getClass().isInstance(MainActivity.class)){
                    Intent intent = curr_activity.getIntent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    curr_activity.finish();
                    startActivity(intent);

                }
                else{
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }


        } catch (JSONException e) {
            Log.e("Exception:- ", Log.getStackTraceString(e));
        }

    }
}
