package com.univpm.bartapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MieiOggettiFragment extends Fragment {

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private MyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth mAuth;
    ArrayList<Oggetto> arrayList;
    private FirebaseUser currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("oggetti");
        databaseReference.keepSynced(true);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        arrayList = new ArrayList<Oggetto>();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_nosearch, container, false);

        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("oggetti");
        databaseReference.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String utente = mAuth.getUid();

        FirebaseRecyclerOptions<Oggetto> options = new FirebaseRecyclerOptions.Builder<Oggetto>()
                .setQuery(databaseReference.orderByChild("idUser").equalTo(utente), Oggetto.class)
                .build();

        adapter= new MyAdapter(options);
        recyclerView.setAdapter(adapter);

        return view;
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
