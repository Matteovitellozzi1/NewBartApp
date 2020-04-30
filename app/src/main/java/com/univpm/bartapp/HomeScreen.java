package com.univpm.bartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.navigation.NavigationView;
import com.univpm.bartapp.fragment.fragment_profilo;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private fragment_profilo fragmentProfilo;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        menuItem= (MenuItem) findViewById(R.id.Contattaci); //N.B.: NON SO SE SERVA VERAMENTE
        Log.i("a", "SONO QUI"); // ricordati di cancellare tutti i Log

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(this);

        /*recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //mAdapter = new MyAdapter();
        recyclerView.setAdapter(mAdapter);*/

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { // con uno switch potrò scegliere cosa fare in base a cosa premuto
        switch (item.getItemId()) {
            case R.id.Contattaci : invioMail(); break;
            case R.id.Home : {
                Intent intent = new Intent(this, HomeScreen.class);
                startActivity(intent);
                break;
            }
            case R.id.aggiungi_prodotto : {
                Intent intent= new Intent (this, Inserimento.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }

    public void invioMail(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Help Request");
        //intent.putExtra(Intent.EXTRA_TEXT, et.getText().toString());
        intent.setData(Uri.parse("mailto:mattiasospetti@libero.it"));
        startActivity(intent);
    }

    public void headerNavigation (View v) {
        Intent intent = new Intent(this, Profilo.class);
        startActivity(intent);
    }

}

