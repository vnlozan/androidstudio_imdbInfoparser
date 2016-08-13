package com.example.v.courseworkparser;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Semaphore;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by V on 04.06.2016.
 */
public class FileDataManager {
    private File sdFile;
    private Context context;
    public FileDataManager(Context context) {
        this.context=context;
        try {
            String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><movies></movies>";
            sdFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "history.xml");
            if (!sdFile.exists()) {
                sdFile.createNewFile();
                String file = sdFile.getAbsolutePath();
                FileWriter sdFileWritter = new FileWriter(file, true);
                BufferedWriter heapBufferWritter = new BufferedWriter(sdFileWritter);
                heapBufferWritter.write(data);
                heapBufferWritter.close();
                System.out.println("DoneDoneDone");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addNewData(fullMovieInfo movieData) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc=docBuilder.parse(sdFile);
        Element rootElement = doc.getDocumentElement();

        Element staff = doc.createElement("movie");
        rootElement.appendChild(staff);

        Element nameElement = doc.createElement("title");
        nameElement.appendChild(doc.createTextNode(movieData.getTitleMovie()));
        staff.appendChild(nameElement);

        Element posterElement = doc.createElement("poster");
        posterElement.appendChild(doc.createTextNode(movieData.getPosterMovie()));
        staff.appendChild(posterElement);

        Element yearElement = doc.createElement("year");
        yearElement.appendChild(doc.createTextNode(movieData.getYearMovie()));
        staff.appendChild(yearElement);

        Element directorElement = doc.createElement("director");
        directorElement.appendChild(doc.createTextNode(movieData.getDirectorMovie()));
        staff.appendChild(directorElement);

        Element genreElement = doc.createElement("genre");
        genreElement.appendChild(doc.createTextNode(movieData.getGenreMovie()));
        staff.appendChild(genreElement);

        Element actorsElement = doc.createElement("actors");
        actorsElement.appendChild(doc.createTextNode(movieData.getActorsMovie()));
        staff.appendChild(actorsElement);

        Element plotElement = doc.createElement("plot");
        plotElement.appendChild(doc.createTextNode(movieData.getPlotMovie()));
        staff.appendChild(plotElement);

        Element runtimeElement = doc.createElement("runtime");
        runtimeElement.appendChild(doc.createTextNode(movieData.getRuntimeMovie()));
        staff.appendChild(runtimeElement);

        Element ratingElement = doc.createElement("rating");
        ratingElement.appendChild(doc.createTextNode(movieData.getImdbRatingMovie()));
        staff.appendChild(ratingElement);

        Element countryElement = doc.createElement("country");
        countryElement.appendChild(doc.createTextNode(movieData.getCountryMovie()));
        staff.appendChild(countryElement);

        Element writerElement = doc.createElement("writer");
        writerElement.appendChild(doc.createTextNode(movieData.getWriterMovie()));
        staff.appendChild(writerElement);

        Element typeElement = doc.createElement("type");
        typeElement.appendChild(doc.createTextNode(movieData.getTypeMovie()));
        staff.appendChild(typeElement);

        Element votesElement = doc.createElement("imdbVotes");
        votesElement.appendChild(doc.createTextNode(movieData.getImdbVotesMovie()));
        staff.appendChild(votesElement);

        Element awardsElement = doc.createElement("awards");
        awardsElement.appendChild(doc.createTextNode(movieData.getAwardsMovie()));
        staff.appendChild(awardsElement);

        Element idElement = doc.createElement("imdbID");
        idElement.appendChild(doc.createTextNode(movieData.getIDMovie()));
        staff.appendChild(idElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        FileOutputStream fos = null;
        fos = new FileOutputStream(sdFile);
        Result fileResult = new StreamResult(fos);
        transformer.transform(source, fileResult);

        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"history.xml");
        InputStream fileIS = new FileInputStream(f);
        BufferedReader buf = new BufferedReader(new InputStreamReader(fileIS));
        String readString = new String();
        while((readString = buf.readLine())!= null) {
            Log.d("Content after updating:", readString);
        }
    }
    public void removeAllData() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        //получить корневой элемент
        Document doc=docBuilder.parse(sdFile);
        removeRecursively(doc, Node.ELEMENT_NODE, "movie");
        removeRecursively(doc, Node.COMMENT_NODE, null);
        doc.normalize();
        //сохранить
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        FileOutputStream fos = null;
        fos = new FileOutputStream(sdFile);
        Result fileResult = new StreamResult(fos);
        transformer.transform(source, fileResult);
        //file reading
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"history.xml");
        InputStream fileIS = new FileInputStream(f);
        BufferedReader buf = new BufferedReader(new InputStreamReader(fileIS));
        String readString = new String();
        while((readString = buf.readLine())!= null) {
            Log.d("Content after deleting:", readString);
        }
    }
    private void removeRecursively(Node node, short nodeType, String name) {
        if (node.getNodeType()==nodeType && (name == null || node.getNodeName().equals(name)))
        {
            node.getParentNode().removeChild(node);
        }
        else
        {
            NodeList list = node.getChildNodes();
            for (int i = 0; i < list.getLength(); i++)
            {
                removeRecursively(list.item(i), nodeType, name);
            }
        }
    }
    public void ShowAllData(CustomListAdapter adapter) throws ParserConfigurationException, IOException, SAXException {
        if(adapter.getCount()>0){ adapter.clear();adapter.notifyDataSetChanged();}
        try {
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"history.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(f);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("movie");
            String titleP = null,posterP = null,runtimeP = null,yearP = null,ratingP = null,
                    directorP = null,actorsP = null,plotP = null,genreP = null,countryP = null,writerP = null,
                    votesP=null,awardsP=null,idP=null,typeP=null;
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                Element eElement = (Element) nNode;
                posterP=eElement.getElementsByTagName("poster").item(0).getTextContent();
                titleP=eElement.getElementsByTagName("title").item(0).getTextContent();
                runtimeP=eElement.getElementsByTagName("runtime").item(0).getTextContent();
                countryP=eElement.getElementsByTagName("country").item(0).getTextContent();
                directorP=eElement.getElementsByTagName("director").item(0).getTextContent();
                actorsP=eElement.getElementsByTagName("actors").item(0).getTextContent();
                ratingP=eElement.getElementsByTagName("rating").item(0).getTextContent();
                plotP=eElement.getElementsByTagName("plot").item(0).getTextContent();
                yearP=eElement.getElementsByTagName("year").item(0).getTextContent();
                genreP=eElement.getElementsByTagName("genre").item(0).getTextContent();
                writerP=eElement.getElementsByTagName("writer").item(0).getTextContent();
                votesP=eElement.getElementsByTagName("imdbVotes").item(0).getTextContent();
                awardsP=eElement.getElementsByTagName("awards").item(0).getTextContent();
                idP=eElement.getElementsByTagName("imdbID").item(0).getTextContent();
                typeP=eElement.getElementsByTagName("type").item(0).getTextContent();
                Bitmap bitmapImg=null;imageDownloader image = null;
                if(InternetConnection.checkConnection(context))
                {
                    Semaphore semaphore=new Semaphore(0);
                    image=new imageDownloader(posterP,semaphore);
                    new Thread(image).start();
                    semaphore.acquire();
                    bitmapImg=image.getmIcon();
                }
                adapter.add(new fullMovieInfo(titleP,yearP,posterP,directorP,genreP,writerP,countryP,
                        plotP,ratingP,actorsP,runtimeP,idP,typeP,votesP,awardsP,bitmapImg));
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean checkIfExistsData(fullMovieInfo movieData) throws ParserConfigurationException, IOException, SAXException {
        String title = movieData.getTitleMovie();
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(sdFile);

        NodeList list = doc.getElementsByTagName("movie");
        for (int i = 0; i < list.getLength(); i++)
        {
            Node nNode = list.item(i);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            Element eElement = (Element) nNode;
            System.out.println(eElement.getElementsByTagName("title").item(0).getTextContent());
            System.out.println(title);
            if((eElement.getElementsByTagName("title").item(0).getTextContent()).equals(title))
            {
                return true;
            }
        }
        return false;
    }
    public void deleteOldData(fullMovieInfo movieData) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        String title=movieData.getTitleMovie();
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(sdFile);

        NodeList list = doc.getElementsByTagName("movie");
        for (int i = 0; i < list.getLength(); i++)
        {
            Node nNode = list.item(i);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            Element eElement = (Element) nNode;
            System.out.println(eElement.getElementsByTagName("title").item(0).getTextContent());
            System.out.println(title);
            if((eElement.getElementsByTagName("title").item(0).getTextContent()).equals(title))
            {
                nNode.getParentNode().removeChild(nNode);
            }
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        FileOutputStream fos = null;
        fos = new FileOutputStream(sdFile);
        Result fileResult = new StreamResult(fos);
        transformer.transform(source, fileResult);
    }
}
