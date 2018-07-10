package com.example.hirensamtani.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.hirensamtani.popularmovies.CommonUtils.CommonUtils;

public class MovieDetails extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_movie_details);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Context context = getBaseContext();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_share) {
            CommonUtils commonUtils = new CommonUtils();
            TextView textView = (TextView) findViewById(R.id.movie_trailer_name);
            commonUtils.shareUrl(context, context.getString(R.string.youtubeURL)
                    + textView.getTag().toString(), MovieDetails.this);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }


    public void onBackPressed() {
        finish();
    }

}
