package com.univpm.bartapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;


import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    int ciao;


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Oggetto> options;
    private FirebaseRecyclerAdapter<Oggetto, FirebaseViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    MenuItem menuItem;
    private DatabaseReference databaseReference;
    ArrayList<Oggetto> arrayList;
    SearchView mySearchView;



    //private Button showDetails;


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        menuItem = (MenuItem) findViewById(R.id.Contattaci); //N.B.: NON SO SE SERVA VERAMENTE
        Log.i("a", "SONO QUI1"); // ricordati di cancellare tutti i Log

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // nuovo, data 10/5
        //settaggio della recycler view con il riferimento al database
        arrayList = new ArrayList<Oggetto>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("oggetti");


        databaseReference.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<Oggetto>().setQuery(databaseReference, Oggetto.class).build();
        Log.i("a", "SONO QUI2");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        Log.i("a", "SONO QUI5");
        fetch(); //recycler view presa dei dati

        Log.i("a", "SONO QUI4");

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(this);

        mySearchView =(SearchView) findViewById(R.id.searchview);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { // con uno switch potrò scegliere cosa fare in base a cosa premuto
        switch (item.getItemId()) {
            case R.id.Contattaci:
                invioMail();
                break;
            case R.id.Home: {
                Intent intent = new Intent(this, HomeScreen.class);
                startActivity(intent);
                break;
            }
            case R.id.aggiungi_prodotto: {
                Intent intent = new Intent(this, Inserimento.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }

    public void invioMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Help Request");
        //intent.putExtra(Intent.EXTRA_TEXT, et.getText().toString());
        intent.setData(Uri.parse("mailto:mattiasospetti@libero.it"));
        startActivity(intent);
    }

    public void headerNavigation(View v) {
        Intent intent = new Intent(this, Profilo.class);
        startActivity(intent);
    }

    public void fetch() {
        adapter = new FirebaseRecyclerAdapter<Oggetto, FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FirebaseViewHolder firebaseViewHolder, final int i, @NonNull final Oggetto oggetto) {

                mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        //Query query1 = databaseReference.orderByChild("nome").startAt(query).endAt(query + "\uf8ff");
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String query) {

                        return true;
                    }
                });

                firebaseViewHolder.nome.setText(oggetto.getNome());
                Log.i("a", "SONO QUI3");
                firebaseViewHolder.nomeVenditore.setText(oggetto.getNomeVenditore());
                firebaseViewHolder.prezzo.setText(String.valueOf(oggetto.getPrezzo()));
                firebaseViewHolder.idUser.setText(oggetto.getIdUser());

                firebaseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TextView nome1 = v.findViewById(R.id.nome_oggetto);
                        String Nome1 = nome1.getText().toString();
                        TextView nomeVend = v.findViewById(R.id.nome_venditore);
                        String NomeVend = nomeVend.getText().toString();
                        TextView prezzo1 = v.findViewById(R.id.prezzo);
                        String Prezzo1 = prezzo1.getText().toString();
                        TextView idUser1 = v.findViewById(R.id.id_venditore);
                        String IdUser1 = idUser1.getText().toString();

                        Intent intent = new Intent(v.getContext(), VisualizzaProdotto.class);
                        //intent.putExtra("descrizione", descrizione);
                        //intent.putExtra("nomeVend", nomeVend);

                        intent.putExtra("Nome1", Nome1);
                        intent.putExtra("NomeVend", NomeVend);
                        intent.putExtra("Prezzo1", Prezzo1);
                        intent.putExtra("idUser", IdUser1);
                        startActivity(intent);

                    }
                });


            }


            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FirebaseViewHolder(LayoutInflater.from(HomeScreen.this).inflate(R.layout.rv_row, parent, false));
            }



        };
        recyclerView.setAdapter(adapter);

    }

}

