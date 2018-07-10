package com.example.hirensamtani.popularmovies.CommonUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.example.hirensamtani.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.io.File;



/**
 * Created by hirensamtani on 21/3/16.
 */
public class CommonUtils {

    public String getAppStoragePath(Context context){
        String file_path=context.getFilesDir().getAbsolutePath();
        return file_path;
    }


    public void StoreImage(Context context, String url_str, String location, String fileName){
        FileUpload fileUpload = new FileUpload(context,url_str,location,fileName);
        fileUpload.execute();


    }


    public boolean deleteFile(Context context,String location, String fileName){
        boolean result=false;
        //add a slash in file name
        File file = new File(location+fileName);
        result=file.delete();
        return result;
    }

    public void loadImage(Context context,File file,View imgView){
        Picasso
                .with(context)
                .load(file)
                .fit()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into((ImageView) imgView);
    }

    public void loadImage(Context context,String url,View imgView){
        Picasso
                .with(context)
                .load(url)
                .fit()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into((ImageView) imgView);
    }

    public void shareUrl(Context context,String url,Activity activity){
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,context.getString(R.string.shareURLSubject));



        String shareMessage = context.getString(R.string.shareURLText)+url;
        intent.putExtra(android.content.Intent.EXTRA_TEXT,
                shareMessage);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(Intent.createChooser(intent,
                context.getString(R.string.shareHeader)));
    }


}
