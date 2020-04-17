package com.univpm.bartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class HomeScreen extends AppCompatActivity {
    int ciao;

    Menu menu= findViewById(R.id.profilo);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.navbar_home, menu);
        return true;
    }
    public void profilo(MenuItem item){
        Intent intent= new Intent(this, Profilo.class);
        startActivity(intent);
    }
}

