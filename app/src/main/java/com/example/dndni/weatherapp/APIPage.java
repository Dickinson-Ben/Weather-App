package com.example.dndni.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.View;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Arrays;

import android.widget.ListAdapter;
import android.widget.ListView;


import android.os.AsyncTask;
import java.util.ArrayList;
import android.widget.SimpleAdapter;

import android.widget.*;
import android.util.Log;

import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;

import static android.R.attr.fragment;
import static com.example.dndni.weatherapp.R.layout.activity_apipage;


public class APIPage extends AppCompatActivity{

    String TAG = APIPage.class.getSimpleName(); //TAG for use in error catching!
    ListView lv;

    ArrayList<HashMap<String, String>> WeatherItems;
    // string combination goes here as API URL plus the other relevant strings as a +=, then
    //This section invokes a normal permission, the request must be added in the manifest file but no additional code is needed


    //Variables for input into API URL

    public String Currentdate = " ";
    public String average_c = " ";
    public String min_c = " ";
    public String max_c = " ";
    public String avgerage_f = " ";
    public String min_f = " ";
    public String max_f = " ";
    public String summary = " ";

    public double lon;
    public double lat;

    // array list to store each weather item from the API, so we can sort the JSON data into a list view item!



    //On create, is as it sounds. Runs the code in it when the intent is "Created" which in this case means once it is switched to.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apipage);

        WeatherItems = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        ActivityCompat.requestPermissions(APIPage.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);


    }

    // Permission requesting for "Dangerous" permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // display short notification stating permission granted

                    if (ActivityCompat.checkSelfPermission(APIPage.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(APIPage.this, "Permission granted to access location!", Toast.LENGTH_SHORT).show();


                        // acquire a reference to the system Location Manager
                        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                        // Use GPS provider to get last known location
                        String locationProvider = LocationManager.GPS_PROVIDER;
                        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

                        //sets the co-ordinates to these global variables to be used in a different method
                        lat = lastKnownLocation.getLatitude();
                        lon = lastKnownLocation.getLongitude();
                        new GetForecast().execute(); //Begins the ASYNC task for parsing once permissions have been granted

                    }



                } else {
                    //if perimssion is denied, return the user to the navigation screen
                    Intent NavigationScreen = new Intent(this, NavigationScreen.class);
                    startActivity(NavigationScreen);
                    Toast.makeText(APIPage.this, "Permission denied, we need your locatoin to tell you the weather!", Toast.LENGTH_SHORT).show();

                }
                return;
            }
        }
    }


    //Start of the Async task, it is in 4 parts.

    //Async methods for API usage. These are here to prevent the app being un-useable whilst API is running
    public class GetForecast extends AsyncTask<Void, Void, Void> {


        //Combines the strings after recieving the location from getlastknownlocation! This way it can tell the weather wherever you are!
        //Currently doesn't work on an emulator that can't use google maps

        String URLpart1 = "https://www.amdoren.com/api/weather.php?api_key=SXWB3YAFGrMxbga9nDdVahA8YB36WS&lat=";
        String APIURL = URLpart1 + lat + "&lon=" + lon;

        @Override

        protected void onPreExecute() {
            //toast notification (A pop up on the screen)
            Toast.makeText(APIPage.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override

        protected Void doInBackground(Void... arg0) {
            // create new instance of the httpConnect class
            HTTPConnect jParser = new HTTPConnect();
            // get json data from the URL, all of the information from the JSON file is held here
            String json = jParser.getJSONFromUrl(APIURL);
            Log.e(TAG, "Response from url: " + json);
            //the below section retrieves the JSON data from the string in the HTTPConnect class
            //this section also parses the data into array lists for use with an array adapter and list view.

            if (json != null) // if the JSON string isn't empty, i.e. the json file was able to download, execute this
            {
                try {
                    // parse returned json string into json array
                    JSONObject JsonWeatherObject = new JSONObject(json);

                    JSONArray forecast = JsonWeatherObject.getJSONArray("forecast");

                    // loop through json array and add each tweet to item in arrayList
                    for (int i = 0; i < forecast.length(); i++) {
                       //loop through the forecast array to get the
                        JSONObject WJO = forecast.getJSONObject(i); // the array list to sort the JSON data into as
                        //we cannot put the data directly into a JSON Object.


                        //the different parts of the obeject. Temprature, the forecast etc.
                        //Filling them with the relevant information from the downloaded JSON file
                        Currentdate = WJO.getString("date");
                        average_c = WJO.getString("avg_c");
                        min_c = WJO.getString("min_c");
                        max_c = WJO.getString("max_c");
                        avgerage_f = WJO.getString("avg_f");
                        min_f = WJO.getString("min_f");
                        max_f = WJO.getString("max_f");
                        summary = WJO.getString("summary");


                        //Here, if the JSON contains it, I could put additional nodes.

                        //adding each child node to the hashmap

                        HashMap<String, String> weather = new HashMap<>();
                        weather.put("CurrentDate", Currentdate);
                        weather.put("avg_c", "Average Temp in Celcius: " + average_c);
                        weather.put("min_c", min_c);
                        weather.put("max_c", max_c);
                        weather.put("avg_f", avgerage_f);
                        weather.put("min_f", min_f);
                        weather.put("max_f", max_f);
                        weather.put("summary", summary);

                        WeatherItems.add(weather);
                        Log.e(TAG, "Response from url: " + weather);//for debugging


                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Error Parsing the Json File: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json Parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }

            return null;
        }

        @Override
        //This puts the contents of the JSON file into the list view, code to add additional information goes here
        //such as images, font styles, colours and size etc.

        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            Log.e(TAG, "Post Exexute Launched" + WeatherItems);
            Log.e(TAG, "Response from url: " + APIURL);

            ListAdapter adapter = new SimpleAdapter(APIPage.this, WeatherItems,
                    R.layout.list_item, new String[]{"CurrentDate", "summary", "avg_c"},
                    new int[]{R.id.currentdate, R.id.summary, R.id.averagec});
            //NEXT Add in the other weather items? Perhaps just temprature.
            lv.setAdapter(adapter);
            //API call limit reached but the data DOES get parsed.
        }
    }


    //button to return user to the navigation screen
    public void GoBack(View view) {

        //returns the user to the navigation page
        Intent NavigationIntent = new Intent(this, NavigationScreen.class);

        startActivity(NavigationIntent);
    }
}
