package com.eulerity.hackathon.imagefinder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.github.itechbear.robotstxt.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

/**
 The ParseThread Class creates and handles threads to parse and scrape
 websites for urls and images
 @author Michael Neustater
 */

public class ParserThread implements Runnable{

    private static final int REQUEST_DELAY = 250;  //Maximum delay between page requests
    private static ArrayList<String> visitedURLS;   //ArrayList to Store VisitedURLS
    private ArrayList<String> scrapedURLS;          //contains all urls seen on page
    public static ArrayList<String> scrapedIMGS;

    //domain vars
    private static String connectionType;
    private static String prefix;
    private static String extension;
    private static String domainName;

    //robots.txt vars
    private static String robotsTxt;
    private static RobotsMatcher matcher; //for checking robots.txt

    //thread tracker vars
    public static int linksParsed = 0;
    private int currentWorker = linksParsed;    //thread number for debugging
    private static boolean stop = false;
    public static ExecutorService es;

    private Document doc;
    private String URL;



    @Override
    public void run() {
    }

    /**
     * The Method resets all static variables
     */
    public static boolean reset(){
        matcher = null;
        visitedURLS = null;
        scrapedIMGS = null;
        connectionType = null;
        prefix = null;
        extension = null;
        domainName = null;
        linksParsed = 0;
        stop = false;
        es = null;
        System.out.println("Scrapper RESET!");
        return true;
    }

    /**
     * The Method sets univeral stop value for workers
     */
    public static void stop(){
        stop = true;
        System.out.println("Threads STOPPED!");
    }

    /**
     * The Method checks if worker should stop
     */
    public static void checkStatus(){
        if(stop == true){
            Thread.currentThread().interrupt();
        }
    }

    public ParserThread(String URL) {
        checkStatus();
        if(linksParsed == 0){
            System.out.println("New Scrapper Started!");
        }

        linksParsed++;
        //if(linksParsed% 5 == 0)
        System.out.println("Number of Links Parsed (Thread): " + linksParsed + " (Current URL: " + URL + ")");

        try {
            this.URL = URL;

            doc = Jsoup.connect(this.URL).ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .timeout(12000)
                    .followRedirects(true)
                    .get();

            if (domainName == null) {
                visitedURLS = new ArrayList<String>();
                visitedURLS.add(URL);

                scrapedIMGS = new ArrayList<String>();

                //break the url up
                StringTokenizer tokenizer = new StringTokenizer(URL, "//,.,/");
                connectionType = tokenizer.nextToken();

                //check if prefix like www. exists
                String noOrigin = URL.substring(connectionType.length() + 2);

                int endOfURL = noOrigin.indexOf('/');
                if(endOfURL == -1) {
                    endOfURL = noOrigin.length();
                }

                String noQuery = noOrigin.substring(0, endOfURL);
                int periods = 0;
                for(int i = 0; i < noQuery.length(); i++ ){
                    if(noQuery.charAt(i) == '.'){
                        periods++;
                    }
                }

                if(periods == 2) {
                    prefix = tokenizer.nextToken() + '.';
                    domainName = tokenizer.nextToken();
                    extension = '.' + tokenizer.nextToken();
                }else{
                    prefix = "";
                    domainName = tokenizer.nextToken();
                    extension = '.' + tokenizer.nextToken();
                }

                matcher = new RobotsMatcher();
                robotsTxt = robotsDownloader(connectionType + "//" + prefix + domainName + extension + "/robots.txt");

                boolean initRoboCheck = RobotsCheck(URL);
                //get favicon for site
                if(initRoboCheck) {
                    String favicon = connectionType + "//" + prefix + domainName + extension + "/favicon.ico";
                    scrapedIMGS.add(favicon);

                }else{
                    System.err.println("INITIAL URL DISALLOWED! ATTEMPTING TO STOP!!");
                    linksParsed = -1;
                    return;
                }
            }
            URLScrape();
            IMGScrape();

                for (String currentURL : scrapedURLS) {
                    checkStatus();
                    if (!visitedURLS.contains(currentURL)) {
                        try {
                            //sleep to avoid overwhelming website
                            Thread.sleep((long) (Math.random() * REQUEST_DELAY));
                        } catch (InterruptedException e) {
                            System.err.println("Sleep between threads interrupted!");
                        }
                        visitedURLS.add(currentURL);
                        es.execute(new Runnable() {
                            @Override
                            public void run() {
                            }

                            ParserThread newThread = new ParserThread(currentURL);
                        });
                    }
                }

        } catch (IOException e) {
            //e.printStackTrace();
            if(URL.contains("mailto:")){
                return;
            }
            System.err.println("Could not connect to: " + URL + " LP: " + linksParsed);
        } catch (IllegalArgumentException e) {
            //e.printStackTrace();
            System.err.println("Invalid URL:" + URL + " LP:" + linksParsed);
        } catch (RejectedExecutionException e){
            System.err.println("New Thread Was Prevented on URL: " + URL + " (was request terminated?)");
        }
    }

    /**
     * The Method scrapes the page for all URLS
     */
    private void URLScrape(){
            scrapedURLS = new ArrayList<String>();

            //get all URLS listed on page and insert to a  (check they are of the same domain)
            Elements URLS = doc.select("a[href]");
            for (Element URLElement : URLS) {
                checkStatus();
                String pulledURL = URLElement.attr("abs:href");

                if (pulledURL.contains("//" + prefix + domainName + extension) && RobotsCheck(pulledURL)) {
                    scrapedURLS.add(pulledURL);
                }
            }
    }

    /**
     * The Method scrapes the page for all images
     */
    private void IMGScrape(){
            Elements IMGS = doc.select("img");

            for (Element IMGElement : IMGS) {
                checkStatus();
                String pulledIMG = IMGElement.attr("abs:src");
                if (!scrapedIMGS.contains(pulledIMG) && RobotsCheck(pulledIMG)) {
                    scrapedIMGS.add(pulledIMG);
                }
            }

    }

    /**
     * The Method Downloads the sites robots.txt file to a string
     * @param URL String form of the URL of robots.txt
     */
    public String robotsDownloader(String URL){
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            URL robotsURL = new URL(URL);

            InputStream in = robotsURL.openStream();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append(System.lineSeparator());
                }
            } finally {
                in.close();
            }
            return sb.toString();
        }catch (Exception e){
            System.err.println("NO VALID robots.txt FOUND! ASSUMING ALL URLS VALID!");
            return null;
        }
    }

    /**
     * The Method Checks Given URL with robots.txt file
     * @param URL String form of the URL being checked
     */
    private boolean RobotsCheck(String URL){
        matcher = new RobotsMatcher();
        if(robotsTxt != null){
            return matcher.OneAgentAllowedByRobots(robotsTxt, "FooBot", URL);
        } else {
            return true;
        }
    }
}