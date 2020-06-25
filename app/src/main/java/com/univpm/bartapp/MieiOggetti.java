package com.univpm.bartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MieiOggetti extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Oggetto> options;
   private FirebaseRecyclerAdapter<Oggetto, MyAdapter.FirebaseViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    ArrayList<Oggetto> arrayList;
    String idUser;


    @Override
    protected void onStop() {
        super.onStop();
        //adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
       // adapter.startListening();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miei_oggetti);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        //String utente = mAuth.getUid();

        arrayList = new ArrayList<Oggetto>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("oggetti");
        databaseReference.keepSynced(true);
        idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //options = new FirebaseRecyclerOptions.Builder<Oggetto>().setQuery(databaseReference, Oggetto.class).build();
        options = new FirebaseRecyclerOptions.Builder<Oggetto>().setQuery(databaseReference.orderByChild("idUser").equalTo(idUser), Oggetto.class).build();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_miei_oggetti);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();


        drawerLayout = findViewById(R.id.drawer_layout1);
    }

    public void fetch() {
        Log.i("a", "SONO nella fetch");
        adapter = new FirebaseRecyclerAdapter<Oggetto, MyAdapter.FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyAdapter.FirebaseViewHolder firebaseViewHolder, final int i, @NonNull final Oggetto oggetto) {
                firebaseViewHolder.nome.setText(oggetto.getNome());
                Log.i("a", "SONO QUI");
                firebaseViewHolder.nomeVenditore.setText(oggetto.getNomeVenditore());
                firebaseViewHolder.prezzo.setText(String.valueOf(oggetto.getPrezzo()));
                firebaseViewHolder.idUser.setText(oggetto.getIdUser());

                final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReference();

                String idUser = oggetto.getIdUser();
                String nome = oggetto.getNome();
                storageReference.child("Image").child("ImmaginiOggetti").child(idUser).child(nome).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerCrop().into(firebaseViewHolder.immagineOggetto);
                    }
                });

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
                        String IdOggetto = getRef(i).toString();

                        Intent intent = new Intent(v.getContext(), VisualizzaProdotto.class);
                        //intent.putExtra("descrizione", descrizione);
                        //intent.putExtra("nomeVend", nomeVend);

                        intent.putExtra("IdOggetto", IdOggetto);
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
            public MyAdapter.FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyAdapter.FirebaseViewHolder(LayoutInflater.from(MieiOggetti.this).inflate(R.layout.rv_row, parent, false));
            }
        };
        recyclerView.setAdapter(adapter);
    }

}
