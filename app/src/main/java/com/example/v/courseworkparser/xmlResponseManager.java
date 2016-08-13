package com.example.v.courseworkparser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by V on 04.06.2016.
 */
public class xmlResponseManager {
    private fullMovieInfo movieData;
    private String movieXMLInfo;
    private Boolean responseOK;
    public xmlResponseManager(String movieXMLInfo) {
        this.movieXMLInfo = movieXMLInfo;
        this.responseOK=false;
        this.movieData =new fullMovieInfo();
    }
    public boolean checkResponse() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(movieXMLInfo));
        Document doc = db.parse(is);
        NodeList nodeLst = doc.getElementsByTagName("root");
        Node node = nodeLst.item(0);
        Element element = (Element) node;
        String response = element.getAttribute("response");
        System.out.println(response);
        if(response.equals("True"))
        {
            responseOK=true;
        }
        else
        {
            responseOK=false;
        }
        return responseOK;
    }
    public void parseData() throws ParserConfigurationException, IOException, SAXException {
        if(responseOK) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(movieXMLInfo));
            Document doc = db.parse(is);
            NodeList nodeLst = doc.getElementsByTagName("movie");
            Node node = nodeLst.item(0);
            Element element = (Element) node;
            movieData.setTitleMovie(element.getAttribute("title"));
            movieData.setYearMovie(element.getAttribute("year"));
            movieData.setGenreMovie(element.getAttribute("genre"));
            movieData.setDirectorMovie(element.getAttribute("director"));
            movieData.setWriterMovie(element.getAttribute("writer"));
            movieData.setActorsMovie(element.getAttribute("actors"));
            movieData.setPlotMovie(element.getAttribute("plot"));
            movieData.setCountryMovie(element.getAttribute("country"));
            movieData.setPosterMovie(element.getAttribute("poster"));
            movieData.setImdbRatingMovie(element.getAttribute("imdbRating"));
            movieData.setRuntimeMovie(element.getAttribute("runtime"));
            movieData.setAwardsMovie(element.getAttribute("awards"));
            movieData.setIDMovie(element.getAttribute("imdbID"));
            movieData.setImdbVotesMovie(element.getAttribute("imdbVotes"));
            movieData.setTypeMovie(element.getAttribute("type"));
        }
    }
    public fullMovieInfo getmovieData() {
        return movieData;
    }
}
