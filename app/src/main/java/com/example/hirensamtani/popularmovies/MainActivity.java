package com.example.hirensamtani.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.hirensamtani.popularmovies.CommonUtils.CommonUtils;


public class MainActivity extends ActionBarActivity {
    private Menu menu;
    private static String movieJSON;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null){
            this.movieJSON = savedInstanceState.getString("movieJSON");
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;

        if(movieJSON!=null){
            MenuItem item = menu.findItem(R.id.menu_item_share_main);
            item.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        Context context = getBaseContext();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            intent = new Intent(MainActivity.this,movieSettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            return true;
        }

        else if (id == R.id.menu_item_share_main) {
            CommonUtils commonUtils = new CommonUtils();
            TextView textView = (TextView) findViewById(R.id.movie_trailer_name);
            commonUtils.shareUrl(context,context.getString(R.string.youtubeURL)
                    +textView.getTag().toString(),MainActivity.this);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    public void onClickMovieItem(View view){

        String movJSON = view.getTag().toString();



        MenuItem item = menu.findItem(R.id.menu_item_share_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        View movieDetailFragment = findViewById(R.id.fragment_moviedetail);


        Intent intent = getIntent();
        intent.putExtra("movieJSON", movJSON);

        if(movieDetailFragment!=null){
            this.movieJSON = movJSON;
            View fragment_movielist = findViewById(R.id.fragment_movielist);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if(fragment_movielist.getLayoutParams().width!=400){
                fragment_movielist.getLayoutParams().width=400;
                MovieFragment movieFragment1 = new MovieFragment();
                fragmentTransaction.replace(R.id.fragment_movielist, movieFragment1);
            }




            MovieDetailFragment movieDetailFragment1 = new MovieDetailFragment();
            fragmentTransaction.replace(R.id.fragment_moviedetail, movieDetailFragment1);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            item.setVisible(true);

        }

        else{
        intent = new Intent(MainActivity.this,MovieDetails.class);
        intent.putExtra("movieJSON", view.getTag().toString());
        startActivity(intent);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();

        String fromActivity = intent.getStringExtra("fromActivity");
        if(fromActivity!=null){
                if(fromActivity.equals("settingsActivity")){
                    this.movieJSON = null;
                    intent.putExtra("fromActivity","MainActivity");
                }
            }



        MovieFragment movieFragment_main = new MovieFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_movielist, movieFragment_main);
        fragmentTransaction.commit();

        if(movieJSON!=null){
            //loadDetailFragment(Intent intent,FragmentTransaction fragmentTransaction,FragmentManager fragmentManager)

            View movieDetailFragment = findViewById(R.id.fragment_moviedetail);
            intent.putExtra("movieJSON", movieJSON);

            if(movieDetailFragment!=null){
                View fragment_movielist = findViewById(R.id.fragment_movielist);
                fragmentTransaction = fragmentManager.beginTransaction();

                if(fragment_movielist.getLayoutParams().width!=400){
                    fragment_movielist.getLayoutParams().width=400;
                    MovieFragment movieFragment1 = new MovieFragment();
                    fragmentTransaction.replace(R.id.fragment_movielist, movieFragment1);
                }




                MovieDetailFragment movieDetailFragment1 = new MovieDetailFragment();
                fragmentTransaction.replace(R.id.fragment_moviedetail, movieDetailFragment1);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }

        }


    }


    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        finish();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString("movieJSON", this.movieJSON);
        super.onSaveInstanceState(savedInstanceState);
    }




}
