package com.example.v.courseworkparser;
import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Semaphore;

/**
 * Created by V on 05.06.2016.
 */
public class requestManager implements Runnable {
    private String title;
    private String year;
    private String responseText;
    private Semaphore semaphore;
    public requestManager(inputData data, Semaphore semaphore) {
        this.title = data.getMovieName();
        this.year = data.getMovieYear();
        this.semaphore=semaphore;
    }
    @Override
    public void run() {
        HttpURLConnection conn;
        BufferedReader rd;
        String line="";
        URL url = null;
        responseText="";
        try {
            String titleURL=title.replaceAll(" +","+");
            url = new URL("http://omdbapi.com/?t=" + titleURL + "&y=" + year + "&plot=" + "full" + "&r=" + "xml");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("t", title);
            conn.setRequestProperty("y", year);
            conn.setRequestProperty("plot", "full");
            conn.setRequestProperty("r", "xml");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                responseText += line;
            }
            //conn.getHead
            rd.close();
            System.out.println(responseText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        semaphore.release();
    }
    public String getResponseText() {
        return responseText;
    }
}
