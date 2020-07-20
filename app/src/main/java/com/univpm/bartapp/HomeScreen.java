package com.univpm.bartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.ContactsContract;
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
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView nomeHeader;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private ImageView imageHeader;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        menuItem = (MenuItem) findViewById(R.id.Contattaci);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        View headerView = navView.getHeaderView(0);
        nomeHeader = (TextView) headerView.findViewById(R.id.nomeHeader);
        nomeHeader.setText(firebaseUser.getDisplayName());
        imageHeader = (ImageView) headerView.findViewById(R.id.button_add);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        //inserisco l'immagine del profilo nell'header della navigation view
        storageReference.child("Image").child("Profile Pic").child(firebaseAuth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(imageHeader);
            }
        });
        //inserimento fragment della recycler per la lista dei prodotti che sono sul mercato
        RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_container_visualizza, recyclerViewFragment, "");
        ft.commit();
    }

    //gestione della navigazione nella navigation view con tutti i fragment/activity utilizzabili
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { // con uno switch potrò scegliere cosa fare in base a cosa premuto
        Fragment selected = null;
        switch (item.getItemId()) {

            case R.id.aggiungi_prodotto: {
                Intent intent = new Intent(this, Inserimento.class);
                startActivity(intent);
                return true;
            }

            case R.id.Contattaci: {
                invioMail();
                return true;
            }

            case R.id.Home: {
                selected = new RecyclerViewFragment();
                break;
            }

            case R.id.Miei_Oggetti: {
                selected = new MieiOggettiFragment();
                break;
            }

            case R.id.Offerte_ricevute: {
                selected = new OfferteRicevute();
                break;
            }

            case R.id.Offerte_inviate: {
                selected = new OfferteInviate();
                break;
            }

            case R.id.Riepilogo_scambi: {
                selected= new RiepilogoFragment();
                break;
            }

            case R.id.Logout: {
                logout();
                return true;
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_visualizza, selected).commit();
        drawerLayout.closeDrawers();
        return true;
    }

    //metodo per il 'contattaci' e contattare gli sviluppatori in caso di problemi
    public void invioMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Help Request");

        intent.setData(Uri.parse("mailto:BartApp@developers.com"));
        startActivity(intent);
    }

    //navigazione per l'header che ci permette di giungere alla schermata del profilo
    public void headerNavigation(View v) {
        Intent intent = new Intent(this, Profilo.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //metodo per effettuare il logout dall'App con un dialog per confermare se effettivamente l'utente lo vuole fare.
    protected void logout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(HomeScreen.this);
        dialog.setTitle("Attenzione");
        dialog.setCancelable(false);
        dialog.setMessage("Sei sicuro di voler effettuare il Logout?");
        dialog.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(HomeScreen.this, Login_Screen.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
}
