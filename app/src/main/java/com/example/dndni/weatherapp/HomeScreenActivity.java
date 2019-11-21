package com.example.dndni.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

//Things to add here.

//Home screen image
//Quit button


public class HomeScreenActivity extends AppCompatActivity {

    @Override
    //On create, or on launch in otherwords, perform these actions
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }


    //On push of get started button, perform these actions
    public void GetStartedButton(View view) {

        // create an intent
        Intent NavigationScreenIntent = new Intent(this, NavigationScreen.class);
        startActivity(NavigationScreenIntent);

    }
}
