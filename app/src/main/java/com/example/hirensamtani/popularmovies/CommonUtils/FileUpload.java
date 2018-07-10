package com.example.hirensamtani.popularmovies.CommonUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hirensamtani on 21/3/16.
 */
public class FileUpload extends AsyncTask<Void,Void,Void> {

    private Context context;
    private String url_str;
    private String location;
    private String fileName;

    public FileUpload(Context context,String url_str, String location, String fileName){
        this.context=context;
        this.url_str=url_str;
        this.location=location;
        this.fileName=fileName;
    }


public boolean StoreImage(Context context,String url_str, String location, String fileName){
        boolean result=false;
        InputStream inputStream;

        try {
        URL url = new URL(url_str);
        inputStream = url.openStream();

        //Add a slash in filename
        byte[] buffer = new byte[1500];
        OutputStream outputStream = new FileOutputStream(location+fileName);

        try{
        int bytesRead=0;
        while((bytesRead = inputStream.read(buffer,0,buffer.length))>=0){
        outputStream.write(buffer,0,bytesRead);
        }
        }
        finally{
        outputStream.close();
        buffer=null;
        }

        result=true;

        } catch (MalformedURLException e) {
            Log.e("Exception:- ", Log.getStackTraceString(e));
        } catch (IOException e) {
            Log.e("Exception:- ", Log.getStackTraceString(e));
        }


        return result;
        }


@Override
protected Void doInBackground(Void... params) {
        StoreImage(context,url_str,location,fileName);
        return null;
        }
}
