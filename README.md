# Eulerity Hackathon Challenge
Congratulations on making it to this stage of Eulerity's interview process! In this folder is a project for a partially built web application whose goal is to crawl a provided URL and pick out the images from it. This README will provide more information about the goals of the project, its structure, and setup and submission instructions.

## ImageFinder Goal
The goal of this task is to perform a web crawl on the URL string provided. From the crawl, you will need to parse out all of the images on that web page and return a JSON array of strings that represent the URLs of all images on the page. You will also need to designate which images you determine to be logos. [Jsoup](https://jsoup.org/) is a great basic library for crawling and is already included as a maven dependency in this project, however you are welcome to use whatever library you would like.

You also need to build upon this task in one (or more) of the following ways:
- Crawl the rest of the site or sub-pages to find more images
- Make your crawler multithreaded so your crawl is performed on multiple pages at a time
- Think about how not to crawl the same page multiple times
- Make your crawler "friendly" - try not to get banned from the site by performing too many crawls
- Show off your front-end dev skills with Javascript, HTML, and/or CSS to make the site look more engaging
- Any other way you feel you can show off your strengths as a developer 😊

**PLEASE do not send us a submission with only a basic JSoup crawl and only a couple lines of code.** This is your chance to prove what you could contribute to our team.

Your project will be due exactly 48 hours after you receive this project. To submit, zip up your project (`imagefinder.zip`) and email it back to me. **Please include a list of URLs that you used to test in your submissions.** You should place them in the attached `test-links.txt` file found in the root of this project.

## Structure
The ImageFinder servlet is found in `src/main/java/com/eulerity/hackathon/imagefinder/ImageFinder.java`. This is the only provided Java class. Feel free to add more classes or packages as you see fit. 

The main landing page for this project can be found in `src/main/webapp/index.html`. This page contains more instructions and serves as the starting page for the web application. You may edit this page as much as it suits you, and/or add other pages. 

Finally, in the root directory of this project, you will find the `pom.xml`. This contains the project configuration details used by maven to build the project. If you want/need to use outside dependencies, you should add them to this file.

## Running the Project
Here we will detail how to setup and run this project so you may get started, as well as the requirements needed to do so.

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

You should see a line at the bottom that says "Started Jetty Server". Now, if you enter `localhost:8080` into your browser, you should see the `index.html` welcome page! If all has gone well to this point, you're ready to begin!

## Submission
When you are finished working on the project, before zipping up and emailing back your submission, **PLEASE RUN ONE LAST `mvn clean` COMMAND TO REMOVE ANY UNNECESSARY FILES FROM YOUR SUBMISSION**. Please also make sure to add the URLs you used to test your project to the `test-links.txt` file. After doing these things, you may zip up the root directory (`imagefinder`) and email it back to us.

## Final Notes
- If you feel you need more time to work, you are free to ask for it
- If you are having any trouble, especially with the setup, please reach out and we will try to answer as soon as we can
- The ideas listed above on how to expand the project are great starting points, but feel free to add in your own ideas as well
- Try to follow some good-practice principles when working on your code, such as meaningful and clean variable/method names and other good coding practices
- The code we have provided is to allow you to hit the ground running. You are free to use whatever web service you would like (as long as you use Java and it is runnable from the command line)
- We look forward to seeing what you can do, so good luck and have fun😊