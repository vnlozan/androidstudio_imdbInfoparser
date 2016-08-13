package com.example.v.courseworkparser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Semaphore;

/**
 * Created by V on 06.06.2016.
 */
public class imageDownloader implements Runnable{
    private String imageURL;
    private Bitmap mIcon;
    private Semaphore semaphore;
    public imageDownloader(String imageURL,Semaphore semaphore) {
        this.imageURL = imageURL;
        this.mIcon=null;
        this.semaphore=semaphore;
    }
    @Override
    public void run() {
        try {
            InputStream in = new java.net.URL(imageURL).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        semaphore.release();
    }
    public Bitmap getmIcon() {
        return mIcon;
    }
}
