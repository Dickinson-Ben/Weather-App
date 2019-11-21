package com.example.dndni.weatherapp;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

//Things to add here

//Images

//Current Forecast image

//navigational information

//FOR THE LOVE OF GOD FIX THE LAYOUT IT'S AWFUL



public class NavigationScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_screen);
    }


    public void APIScreen(View view) {

        // create an intent to start the activity called TestActivity
        Intent intent = new Intent(this, APIPage.class);
        // start TestActivity!
        startActivity(intent);
    }



    public void Quit(View view) {

        //quit code here
        NavigationScreen.this.finish();
        System.exit(0);

    }
}
