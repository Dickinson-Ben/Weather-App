package com.example.dndni.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.util.Log;




//This class is for retrieving JSON data through the weather API. Parsing is handled n the APIPage class
//The API Page will be handeling the actual data retrieved from the API URL Given here.


public class HTTPConnect {

    //public variables here
    //for debugging purposess
    final String TAG = "JsonParser.java";

    //For storing the downloaded JSON data
    static String json = " ";

    //The activity calls this method and passes in the URL of thre REST service!
        public String getJSONFromUrl(String url) {

        try {
            //Handles the connecion to the REST API service

            URL u = new URL(url); //The URL of the REST API

            //opens the connection and sets the amount of content to retrieve
            HttpURLConnection restConnection = (HttpURLConnection) u.openConnection();
            restConnection.setRequestMethod("GET");
            restConnection.setRequestProperty("Content-length", "0");
            restConnection.setUseCaches(false);
            restConnection.setAllowUserInteraction(false);

            //sets the time out times for connection and read attempts
            //This is set in Milliseconds
            restConnection.setConnectTimeout(10000);
            restConnection.setReadTimeout(10000);
            restConnection.connect();

            //returns the status of the connection response. True or false (1 0r 0)
            int status = restConnection.getResponseCode();

            // switch statement to catch HTTP 200 and 201 errors.
            switch (status) {
                case 200:
                    //Case 200 is blank? Look into why
                case 201:
                    // live connection to your REST service is established here using getInputStream() method
                    BufferedReader br = new BufferedReader(new InputStreamReader(restConnection.getInputStream()));

                    // create a new string builder to store json data returned from the REST service
                    StringBuilder sb = new StringBuilder();
                    String line;

                    // loop through returned data line by line and append to stringbuilder 'sb' variable
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    // remember, you are storing the json as a stringy
                    try {
                        json = sb.toString();
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing data " + e.toString());
                    }
                    // return JSON String containing data to Tweet activity (or whatever your activity is called!)
                    return json;
            }
            // HTTP 200 and 201 error handling from switch statement
        } catch (MalformedURLException ex) {
            Log.e(TAG, "Malformed URL ");
        } catch (IOException ex) {
            Log.e(TAG, "IO Exception ");
        }
        return null;
    }
}


