package com.univpm.bartapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewFragment extends Fragment {

    public FirebaseRecyclerOptions<Oggetto> options;

    private MyAdapter adapter;

    private FirebaseRecyclerAdapter<Oggetto, MyAdapter.FirebaseViewHolder> firebaseRecyclerAdapter; // l'adapter del filtro*/
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    ArrayList<Oggetto> arrayList;
    SearchView mySearchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Inserisco nel fragment la recycler view
        View view = inflater.inflate(R.layout.recycler_view_fragment, container, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mySearchView = (SearchView) view.findViewById(R.id.searchview);

        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                firebaseSearch(query);
                recyclerView.setAdapter(firebaseRecyclerAdapter);
                firebaseRecyclerAdapter.startListening();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
               return true;
            }
        });

        //Connessione con il database "oggetti" di Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("oggetti");
        databaseReference.keepSynced(true);

        //Estraggo tutti gli oggetti che verranno inseriti nella Recycler
        FirebaseRecyclerOptions<Oggetto> options = new FirebaseRecyclerOptions.Builder<Oggetto>()
                .setQuery(databaseReference, Oggetto.class)
                .build();


        adapter = new MyAdapter(options);
        recyclerView.setAdapter(adapter);
        return view;
    }



     protected void firebaseSearch(String searchText) {
        final Query query = databaseReference.orderByChild("nome").equalTo(searchText.toLowerCase());

        options = new FirebaseRecyclerOptions.Builder<Oggetto>().setQuery(query, Oggetto.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Oggetto, MyAdapter.FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyAdapter.FirebaseViewHolder firebaseViewHolder, final int i, @NonNull Oggetto oggetto) {
                firebaseViewHolder.nome.setText(oggetto.getNome());
                firebaseViewHolder.nomeVenditore.setText(oggetto.getNomeVenditore());
                firebaseViewHolder.prezzo.setText(String.valueOf(oggetto.getPrezzo()));
                firebaseViewHolder.idUser.setText(oggetto.getIdUser());
                final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReference();
                final String keyId = this.getRef(i).getKey();
                final String descrizione = oggetto.getDescrizione();

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

                        Bundle bundle= new Bundle();
                        bundle.putString("IdOggetto", keyId);
                        bundle.putString("Nome1", Nome1);
                        bundle.putString("NomeVend", NomeVend);
                        bundle.putString("Prezzo1", Prezzo1);
                        bundle.putString("idUser", IdUser1);
                        bundle.putString("descrizione", descrizione);
                        AppCompatActivity abc= (AppCompatActivity) v.getContext();
                        VisualizzaProdottoFragment visualizzaProdottoFragment= new VisualizzaProdottoFragment();
                        visualizzaProdottoFragment.setArguments(bundle);
                        firebaseRecyclerAdapter.stopListening();
                        abc.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_visualizza, visualizzaProdottoFragment, "").addToBackStack(null).commit();

                    }
                });
            }

            @NonNull
            @Override
            public MyAdapter.FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyAdapter.FirebaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row, parent, false));
            }
        };


    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

}
