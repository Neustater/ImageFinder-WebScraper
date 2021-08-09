## ImageFinder Goal
The goal of this task is to perform a web crawl on the URL string provided. This project is dependent on [Jsoup](https://jsoup.org/) and is included in the maven dependency.

What the crawler can do/is:
- Crawl the rest of the site or sub-pages to find more images
- Multi-threaded/ can crawl multiple pages at the same time (but not the same page)
- Is "friendly" - respects sites bot files and does not bombard the site
- Displays images through front end

## Structure
The ImageFinder servlet is found in `src/main/java/com/eulerity/hackathon/imagefinder/ImageFinder.java`. 

The main landing page for this project can be found in `src/main/webapp/index.html`. This page contains more instructions and serves as the starting page for the web application. 

### Requirements
Before beginning, make sure you have the following installed and ready to use
- Maven 3.5 or higher
- Java 8

### Setup
To start, open a terminal window and navigate to wherever you unzipped to the root directory `imagefinder`. To build the project, run the command:

>`mvn package`

If all goes well you should see some lines that ends with "BUILD SUCCESS". When you build your project, maven should build it in the `target` directory. To clear this, you may run the command:

>`mvn clean`

To run the project, use the following command to start the server:

>`mvn clean test package jetty:run`

You should see a line at the bottom that says "Started Jetty Server". Now, if you enter `localhost:8080` into your browser, you should see the `index.html` welcome page!

## Potential Future Updates
- Improve multithreading efficiency
- Switch to a different parser (non-HTML only) to allow retrieval of code from Javascipt heavy websites
