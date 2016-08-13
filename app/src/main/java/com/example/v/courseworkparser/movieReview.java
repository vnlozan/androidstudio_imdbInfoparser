package com.example.v.courseworkparser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class movieReview extends AppCompatActivity {
    private String name;
    private String year;
    private String genre;
    private String director;
    private String poster;
    private String actors;
    private String plot;
    private String country;
    private String rating;
    private String writer;
    private String runtime;
    private String awards;
    private String votes;
    private String id;
    private TextView textViewName;
    private TextView textViewYear;
    private TextView textViewGenre;
    private TextView textViewWriter;
    private TextView textViewDirector;
    private TextView textViewActors;
    private TextView textViewPlot;
    private TextView textViewCountry;
    private TextView textViewRuntime;
    private TextView textViewImdbRating;
    private TextView textViewImdbVotes;
    private TextView textViewAwards;
    private TextView textViewID;
    private ImageView posterView;
    private imageDownloader image;
    private fullMovieInfo movieData;
    private FileDataManager file;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                System.out.println("SAVESAVE");
                file = new FileDataManager(this);
                try {
                    if(file.checkIfExistsData(movieData)==false) {
                        try {
                            file.addNewData(movieData);
                            Toast.makeText(this, "Movie's info's successfully added to your list", Toast.LENGTH_LONG).show();
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (TransformerException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "Movie's info already exists in your list", Toast.LENGTH_LONG).show();
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.action_delete:
                System.out.println("DELETEDELETE");
                file = new FileDataManager(this);
                try {
                    if(file.checkIfExistsData(movieData)==true) {
                        file.deleteOldData(movieData);
                        Toast.makeText(this, "Movie's info's successfully deleted from your list", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(this, "There's no such Movie Info in your list to delete", Toast.LENGTH_LONG).show();
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return true;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_review, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review);
        movieData=getIntent().getExtras().getParcelable("movieInfo");
        name = movieData.getTypeMovie()+" Title: "+movieData.getTitleMovie();
        year="Year: "+movieData.getYearMovie();
        genre="Movie Genre: "+movieData.getGenreMovie();
        director="Director: "+movieData.getDirectorMovie();
        poster=movieData.getPosterMovie();
        actors="Actors: "+movieData.getActorsMovie();
        plot="Plot: "+movieData.getPlotMovie();
        runtime="Runtime: "+movieData.getRuntimeMovie();
        country="Country: "+movieData.getCountryMovie();
        rating="ImDB Rating "+movieData.getImdbRatingMovie();
        writer="Writer: "+movieData.getWriterMovie();
        awards="Awards: "+movieData.getAwardsMovie();
        votes="Votes: "+movieData.getImdbVotesMovie();

        id="Get the Source Full Movie INFO in browser by id : "+movieData.getIDMovie();

        textViewName = (TextView) findViewById(R.id.movieTextView);
        textViewName.setText(name);
        textViewYear = (TextView) findViewById(R.id.yearTextView);
        textViewYear.setText(year);
        textViewPlot = (TextView) findViewById(R.id.plotTextView);
        textViewPlot.setText(plot);
        textViewActors = (TextView) findViewById(R.id.actorsTextView);
        textViewActors.setText(actors);
        textViewGenre = (TextView) findViewById(R.id.genreTextView);
        textViewGenre.setText(genre);
        textViewImdbRating = (TextView) findViewById(R.id.ratingTextView);
        textViewImdbRating.setText(rating);
        textViewDirector = (TextView) findViewById(R.id.directorTextView);
        textViewDirector.setText(director);
        textViewCountry = (TextView) findViewById(R.id.countryTextView);
        textViewCountry.setText(country);
        textViewRuntime = (TextView) findViewById(R.id.runtimeTextView);
        textViewRuntime.setText(runtime);
        textViewWriter = (TextView) findViewById(R.id.writerTextView);
        textViewWriter.setText(writer);
        textViewAwards = (TextView) findViewById(R.id.awardsTextView);
        textViewAwards.setText(awards);
        textViewImdbVotes = (TextView) findViewById(R.id.votesTextView);
        textViewImdbVotes.setText(votes);
        textViewID = (TextView) findViewById(R.id.idTextView);
        textViewID.setText(id);
        final Context con=this;
        textViewID.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(InternetConnection.checkConnection(con)) {
                    String url = "http://www.imdb.com/title/" + movieData.getIDMovie();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
                else
                    Toast.makeText(con, "Connect please device to internet", Toast.LENGTH_LONG).show();
                return false;
            }
        });



        if(InternetConnection.checkConnection(this))
        {
            Semaphore semaphore=new Semaphore(0);
            image=new imageDownloader(poster,semaphore);
            new Thread(image).start();
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            posterView=(ImageView) findViewById(R.id.posterImageView);
            posterView.setImageBitmap(image.getmIcon());
        }
    }
}

