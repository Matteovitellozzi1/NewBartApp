package com.univpm.bartapp;


import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestoreDataSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapterOfferte extends FirestoreRecyclerAdapter<Offerta, MyAdapterOfferte.FirestoreViewHolder> {

    private String idProdAcq;
    private String idProdVend;
    private String idAcq;
    private String idVend;
    private String nomeOggettoVend, nomeVend;
    private String nomeOggettoAcq;
    private String emailVend;
    Context context;
    FirebaseFirestore firebaseFirestore;

    public MyAdapterOfferte(@NonNull FirestoreRecyclerOptions<Offerta> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final FirestoreViewHolder viewHolder, int position, @NonNull final Offerta offerta) {

        firebaseFirestore= FirebaseFirestore.getInstance();
        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        storageReference.child("Image").child("ImmaginiOggetti").child(offerta.getIdVend()).child(offerta.getNomeOggettoVend()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(viewHolder.immagineOggettoAcq);
            }
        });

        storageReference.child("Image").child("ImmaginiOggetti").child(offerta.getIdAcq()).child(offerta.getNomeOggettoAcq()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(viewHolder.immagineOggettoVend);
            }
        });

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        final String a = documentSnapshot.getId();
        viewHolder.nomeAcq.setText(offerta.getNomeVend());
        viewHolder.nomeVend.setText(offerta.getNomeAcq());
        viewHolder.nomeOggettoAcq.setText(offerta.getNomeOggettoVend());
        viewHolder.nomeOggettoVend.setText(offerta.getNomeOggettoAcq());
        viewHolder.prezzoAcq.setText(offerta.getPrezzoOggettoVend());
        viewHolder.prezzoVend.setText(offerta.getPrezzoAcq());

        final Long keyId = this.getItemId(position);


        viewHolder.btnRifiuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rifiuta(a);
            }
        });
        viewHolder.btnAccetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idProdAcq = offerta.getIdProdAcq();
                idProdVend = offerta.getIdProdVend();
                nomeOggettoAcq = offerta.getNomeOggettoAcq();
                nomeOggettoVend = offerta.getNomeOggettoVend();
                idAcq = offerta.getIdAcq();
                idVend = offerta.getIdVend();
                nomeVend=offerta.getNomeVend();
                emailVend=offerta.getEmailVend();
                accetta(a, idProdAcq, idProdVend,idAcq, idVend, nomeOggettoAcq, nomeOggettoVend, nomeVend, emailVend);
            }

        });
    }

    @NonNull
    @Override
    public FirestoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAdapterOfferte.FirestoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row_offerte, parent, false));
    }

    static class FirestoreViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeOggettoVend, nomeOggettoAcq;
        public TextView prezzoVend, prezzoAcq;
        public TextView nomeAcq, nomeVend;
        public ImageView immagineOggettoVend, immagineOggettoAcq;
        public Button btnAccetta, btnRifiuta;

        public FirestoreViewHolder(@NonNull View v) {
            super(v);
            nomeOggettoAcq = (TextView) v.findViewById(R.id.nome_oggetto);
            nomeOggettoVend = (TextView) v.findViewById(R.id.nome_oggetto_vend);
            prezzoAcq = (TextView) v.findViewById(R.id.prezzo);
            prezzoVend = (TextView) v.findViewById(R.id.prezzo_mio);
            nomeAcq = (TextView) v.findViewById(R.id.nome_venditore);
            nomeVend = (TextView) v.findViewById(R.id.nome_mio);
            immagineOggettoVend = (ImageView) v.findViewById(R.id.immagine_mio_oggetto);
            immagineOggettoAcq = (ImageView) v.findViewById(R.id.immagine_oggetto);
            btnAccetta = (Button) v.findViewById(R.id.btn_accetta);
            btnRifiuta = (Button) v.findViewById(R.id.btn_rifiuta);
        }

    }

    public void rifiuta(final String keyId) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Attenzione!");
        dialog.setCancelable(false);
        dialog.setMessage("Sei sicuro di voler rifiutare l'offerta?");
        dialog.setPositiveButton("Rifiuta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("scambi").document(keyId).delete();

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
    //Funzione per accettare lo scambio in cui creo un database in firestore per registrare un riepilogo dello scambio.
    public void accetta(final String keyId, final String idProdAcq, final String idProdVend, final String idAcq, final
                        String idVend, final String nomeOggettoAcq, final String nomeOggettoVend, final String nomeVend, final String emailVend) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Attenzione!");
        dialog.setCancelable(false);
        dialog.setMessage("Sei sicuro di voler accettare l'offerta?");
        dialog.setPositiveButton("Accetta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                            FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
                            final Map<String, Object> map= new HashMap<>();

                            map.put("emailAcq", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            map.put("emailVend", emailVend);
                            map.put("nomeOggettoAcq", nomeOggettoAcq);
                            map.put("nomeOggettoVend", nomeOggettoVend);
                            map.put("nomeUtenteVend", nomeVend);
                            map.put("idAcq", idAcq);
                            map.put("idVend", idVend);

                            firebaseFirestore.collection("riepilogoscambi").document().set(map);

                            operazione1(idProdAcq);
                            operazione2(idProdAcq);
                            operazione3(idProdVend);
                            operazione4(idProdVend, idProdAcq, idAcq, idVend, nomeOggettoAcq, nomeOggettoVend);

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

    //Elimino tutte le offerte che mi sono state fatte sullo stesso oggetto
    public void operazione1 (String idProdAcq) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("scambi").whereEqualTo("idProdAcq", idProdAcq)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        document.getReference().delete();
                        Toast.makeText(context, "Scambio effettuato", Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
            }
        });
    }
    //Elimino tutte le offerte che IO ho inviato ad altri dell'oggetto che scambio con l'utente.
    public void operazione2 (String idProdAcq) {
        FirebaseFirestore db0 = FirebaseFirestore.getInstance();
        db0.collection("scambi").whereEqualTo("idProdVend", idProdAcq).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        document.getReference().delete();
                        Toast.makeText(context, "Scambio effettuato", Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
            }
        });

    }
    //Elimino tutte le offerte in cui l'oggetto che mi offre l'altro utente è stato richiesto da altri utenti.
    public void operazione3 (String idProdVend) {
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        db1.collection("scambi").whereEqualTo("idProdAcq", idProdVend).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        document.getReference().delete();
                    }
                } else {
                }
            }
        });
    }
    //Elimino tutte le offerte in cui l'oggetto che mi offre l'altro utente è stato proposto ad altri utenti.
    public void operazione4 (final String idProdVend, final  String idProdAcq, final String idAcq, final String idVend, final String nomeOggettoAcq, final String nomeOggettoVend) { //oggetto offerto
        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        db2.collection("scambi").whereEqualTo("idProdVend", idProdVend).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        document.getReference().delete();
                        Toast.makeText(context, "Scambio effettuato", Toast.LENGTH_SHORT).show();
                        eliminaProdotti(idProdVend, idProdAcq, idAcq, idVend, nomeOggettoAcq, nomeOggettoVend);
                    }
                } else {
                }
            }
        });
    }
    //Elimino dal catalogo tutti i prodotti insieme alle immagini di cui accetto lo scambio.
    public void eliminaProdotti (String idProdVend, String idProdAcq, String idAcq, String idVend, String nomeOggettoAcq, String nomeOggettoVend){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("oggetti").child(idProdVend).removeValue();
        firebaseDatabase.getReference("oggetti").child(idProdAcq).removeValue();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image").child("ImmaginiOggetti").child(idAcq);
        storageReference.child(nomeOggettoAcq).delete();
        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference().child("Image").child("ImmaginiOggetti").child(idVend);
        storageReference1.child(nomeOggettoVend).delete();
    }
}
