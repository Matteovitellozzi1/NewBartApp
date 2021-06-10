package com.univpm.bartapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

import static android.hardware.camera2.params.BlackLevelPattern.COUNT;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class Statistiche1 extends Fragment  {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private TextView scambiati, esposti, espostialgiorno;
    private TextView VTS,VTSgiorno,accessi;
    private TextView statistiche, userengagement, mobileengagement, topregion;
    private RecyclerView.LayoutManager layoutManager;
    private  String valore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        /*databaseReference = FirebaseDatabase.getInstance().getReference().child("oggetti");
        databaseReference.keepSynced(true);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();*/

        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.activity_statistiche, container, false);
        View view = inflater.inflate(R.layout.activity_statistiche1, container, false);

        scambiati = view.findViewById(R.id.scambiati);

        final TextView  valoretotale = (TextView) view.findViewById(R.id.valoretotale);


        esposti = view.findViewById(R.id.esposti);
        espostialgiorno = view.findViewById(R.id.espostialgiorno);
        VTS = view.findViewById(R.id.scambiatitotale);
        VTSgiorno = view.findViewById(R.id.scambiatigiorno);
        accessi = view.findViewById(R.id.accessigiornalieri);
        statistiche = view.findViewById(R.id.Statistiche);
        //userengagement = view.findViewById(R.id.userengagement);
        //mobileengagement = view.findViewById(R.id.mobileengagement);
        topregion = view.findViewById(R.id.topregion);


        //RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.recycler_view);

        String mAuth= FirebaseAuth.getInstance().getCurrentUser().getUid();

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


        firebaseFirestore.collection("scambi")
                .orderBy("prezzoOggettoVend")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                int sum=0;
                                Map<String,Object> map = (Map<String, Object>) document.getData();
                                Object price = map.get("prezzoOggettoVend");
                                int pvalue = Integer.parseInt(String.valueOf(price));
                                sum+= pvalue;

                                //long count = (long)document.get(String.valueOf(COUNT));
                                //for (int i=1; i<=count; i++)
                                    //String catName = doc.getString(“CAT” + String.valueOf(i));
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //QuerySnapshot doc = task.getResult();
                                //valore = document.getData().toString();
                                valoretotale.setText(sum);

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        //firebaseFirestore.collection("scambi").addSnapshotListener(new OnSuccessListener<>())

        /*firebaseFirestore.collection("scambi").whereEqualTo("prezzoOggettoVend", "100").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        valoretotale.setText(document.toString());

                    }
                }
            }
        });*/


        /*firebaseFirestore.collectionGroup("scambi").whereEqualTo("idVend", mAuth).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        valoretotale.setText(queryDocumentSnapshots.toString());
                    }
                });*/
        /*firebaseFirestore.collection("scambi").document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Query query = firebaseFirestore.collection("scambi")
                    valoretotale.setText((query.toString()));
                }
            }
        });*/
        /*CollectionReference db = firebaseFirestore.collection("scambi");


        Task<QuerySnapshot> query= db.orderBy("emailVend").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                valoretotale.setText.toString());
            }
        });*/
        //Query query1 = firebaseFirestore.collection("scambi").orderBy("prezzoOggettoVend");

        //valoretotale.setText((query.toString()));
                //+(query1.toString()));








        return view;
    }
}