package com.example.v.courseworkparser;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class mainActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etYear;
    private FileDataManager file;
    private ListView listView;
    private CustomListAdapter adapter;
    private inputData data;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                Quit();
                return true;
            default:
                return true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        verifyStoragePermissions(this);
        etName=(EditText)findViewById(R.id.editTextMovieName);
        etName.setMaxLines(1);
        etName.setMaxWidth(50);
        etYear=(EditText)findViewById(R.id.editTextYear);
        etYear.setMaxLines(1);
        etYear.setMaxWidth(20);
        file=new FileDataManager(this);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new CustomListAdapter(this, R.layout.list_element_view);
        listView.setAdapter(adapter);
        try {
            file.ShowAllData(adapter);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fullMovieInfo rm = adapter.getItem(position);
                Intent i = new Intent(mainActivity.this, movieReview.class);
                i.putExtra("movieInfo",rm);
                startActivity(i);
            }
        });
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        try {
            file.ShowAllData(adapter);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
    public void buttonFind(View v) throws ExecutionException, InterruptedException, IOException, SAXException, ParserConfigurationException, TransformerException {
        if(InternetConnection.checkConnection(this)) {
            data = new inputData(etName.getText().toString(), etYear.getText().toString());
            Semaphore semaphore = new Semaphore(0);
            requestManager req = new requestManager(data, semaphore);
            new Thread(req).start();
            semaphore.acquire();
            String responseText = req.getResponseText();
            xmlResponseManager response = new xmlResponseManager(responseText);
            if (response.checkResponse()) {
                response.parseData();
                fullMovieInfo movieData=response.getmovieData();
                //file.addNewData(movieData);
                Intent i = new Intent(mainActivity.this, movieReview.class);
                i.putExtra("movieInfo",movieData);
                startActivity(i);
                file.ShowAllData(adapter);
            } else
                Toast.makeText(this, "There's no such movie in DataBase", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "Connect please device to internet", Toast.LENGTH_LONG).show();
    }
    public void buttonClearHistory(View v) throws SAXException, TransformerException, ParserConfigurationException, IOException {
        file.removeAllData();
        file.ShowAllData(adapter);
    }
    public static void verifyStoragePermissions(Activity activity) {
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        }
    }
    protected void Quit() {
        ExitActivity.exitApplication(this);
    }
}
class shortMovieInfo {
    private ImageView imageView;
    private TextView textView;

    public void setImageView(Bitmap imageView) {
        this.imageView.setImageBitmap(imageView);
    }

    public void setTextView(String textView) {
        this.textView.setText(textView);
    }

    public shortMovieInfo(ImageView imageView, TextView textView) {
        this.imageView = imageView;
        this.textView = textView;
    }
    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTextView() {
        return textView;
    }
}
class fullMovieInfo implements Parcelable {
    private String titleMovie;
    private String yearMovie;
    private String posterMovie;
    private String directorMovie;
    private String genreMovie;
    private String writerMovie;
    private String countryMovie;
    private String plotMovie;
    private String imdbRatingMovie;
    private String actorsMovie;
    private String runtimeMovie;
    private String IDMovie;
    private String TypeMovie;
    private String imdbVotesMovie;
    private String awardsMovie;
    private Bitmap image;

    public void setIDMovie(String IDMovie) {
        this.IDMovie = IDMovie;
    }
    public void setTypeMovie(String typeMovie) {
        TypeMovie = typeMovie;
    }
    public void setImdbVotesMovie(String imdbVotesMovie) {
        this.imdbVotesMovie = imdbVotesMovie;
    }
    public void setAwardsMovie(String awardsMovie) {
        this.awardsMovie = awardsMovie;
    }
    public void setTitleMovie(String titleMovie) {
        this.titleMovie = titleMovie;
    }
    public void setYearMovie(String yearMovie) {
        this.yearMovie = yearMovie;
    }
    public void setPosterMovie(String posterMovie) {
        this.posterMovie = posterMovie;
    }
    public void setDirectorMovie(String directorMovie) {
        this.directorMovie = directorMovie;
    }
    public void setGenreMovie(String genreMovie) {
        this.genreMovie = genreMovie;
    }
    public void setWriterMovie(String writerMovie) {
        this.writerMovie = writerMovie;
    }
    public void setCountryMovie(String countryMovie) {
        this.countryMovie = countryMovie;
    }
    public void setPlotMovie(String plotMovie) {
        this.plotMovie = plotMovie;
    }
    public void setImdbRatingMovie(String imdbRatingMovie) {
        this.imdbRatingMovie = imdbRatingMovie;
    }
    public void setActorsMovie(String actorsMovie) {
        this.actorsMovie = actorsMovie;
    }
    public void setRuntimeMovie(String runtimeMovie) {
        this.runtimeMovie = runtimeMovie;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public fullMovieInfo(String titleMovie, String yearMovie, String posterMovie,
                         String directorMovie, String genreMovie, String writerMovie,
                         String countryMovie, String plotMovie, String imdbRatingMovie,
                         String actorsMovie, String runtimeMovie,String IDMovie,
                         String TypeMovie,String imdbVotesMovie,String awardsMovie, Bitmap image) {
        this.titleMovie = titleMovie;
        this.yearMovie = yearMovie;
        this.posterMovie = posterMovie;
        this.directorMovie = directorMovie;
        this.genreMovie = genreMovie;
        this.writerMovie = writerMovie;
        this.countryMovie = countryMovie;
        this.plotMovie = plotMovie;
        this.imdbRatingMovie = imdbRatingMovie;
        this.actorsMovie = actorsMovie;
        this.runtimeMovie = runtimeMovie;
        this.image = image;
        this.IDMovie=IDMovie;
        this.TypeMovie=TypeMovie;
        this.imdbVotesMovie=imdbVotesMovie;
        this.awardsMovie=awardsMovie;
    }
    public fullMovieInfo(Bitmap image)
    {
        this.image = image;
    }
    public fullMovieInfo() {}
    public String getTitleMovie() {
        return titleMovie;
    }
    public String getYearMovie() {
        return yearMovie;
    }
    public String getPosterMovie() {
        return posterMovie;
    }
    public String getDirectorMovie() {
        return directorMovie;
    }
    public String getGenreMovie() {
        return genreMovie;
    }
    public String getWriterMovie() {
        return writerMovie;
    }
    public String getCountryMovie() {
        return countryMovie;
    }
    public String getPlotMovie() {
        return plotMovie;
    }
    public String getImdbRatingMovie() {
        return imdbRatingMovie;
    }
    public String getActorsMovie() {
        return actorsMovie;
    }
    public String getRuntimeMovie() {
        return runtimeMovie;
    }
    public Bitmap getImage() {
        return image;
    }
    public String getIDMovie() {
        return IDMovie;
    }
    public String getTypeMovie() {
        return TypeMovie;
    }
    public String getImdbVotesMovie() {
        return imdbVotesMovie;
    }
    public String getAwardsMovie() {
        return awardsMovie;
    }

    protected fullMovieInfo(Parcel in) {
        titleMovie = in.readString();
        yearMovie = in.readString();
        posterMovie = in.readString();
        directorMovie = in.readString();
        genreMovie = in.readString();
        writerMovie = in.readString();
        countryMovie = in.readString();
        plotMovie = in.readString();
        imdbRatingMovie = in.readString();
        actorsMovie = in.readString();
        runtimeMovie = in.readString();
        IDMovie=in.readString();
        TypeMovie=in.readString();
        imdbVotesMovie=in.readString();
        awardsMovie=in.readString();
        //image = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titleMovie);
        dest.writeString(yearMovie);
        dest.writeString(posterMovie);
        dest.writeString(directorMovie);
        dest.writeString(genreMovie);
        dest.writeString(writerMovie);
        dest.writeString(countryMovie);
        dest.writeString(plotMovie);
        dest.writeString(imdbRatingMovie);
        dest.writeString(actorsMovie);
        dest.writeString(runtimeMovie);
        dest.writeString(IDMovie);
        dest.writeString(TypeMovie);
        dest.writeString(imdbVotesMovie);
        dest.writeString(awardsMovie);
        //dest.writeValue(image);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<fullMovieInfo> CREATOR = new Parcelable.Creator<fullMovieInfo>() {
        @Override
        public fullMovieInfo createFromParcel(Parcel in) {
            return new fullMovieInfo(in);
        }

        @Override
        public fullMovieInfo[] newArray(int size) {
            return new fullMovieInfo[size];
        }
    };
}
class inputData {
    private String movieName;
    private String movieYear;
    public inputData(String name,String year) {
        movieName=name;
        movieYear=year;
    }
    public String getMovieYear() {
        return movieYear;
    }
    public String getMovieName() {
        return movieName;
    }
}


