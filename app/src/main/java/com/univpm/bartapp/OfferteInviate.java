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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class OfferteInviate extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    MyAdapterOfferteInviate adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_nosearch, container, false);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        String mAuth= FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        Query query = db.collection("scambi").whereEqualTo("idVend",mAuth);

        FirestoreRecyclerOptions<Offerta> options = new FirestoreRecyclerOptions.Builder<Offerta>()
                .setQuery(query , Offerta.class)
                .build();
        adapter = new MyAdapterOfferteInviate(options, this.getContext());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        adapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        adapter.stopListening();
        super.onStop();
    }
}
