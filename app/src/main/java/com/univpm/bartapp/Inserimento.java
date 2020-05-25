package com.univpm.bartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;

public class Inserimento extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseUser currentUser;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    EditText nomeProdotto, input_descrizione, input_prezzo;
    ImageView immagineOggetto;
    Button inviodati;
    Oggetto oggetto;
    Uri imagePath;
    public static int PICK_IMAGE = 123;
    StorageReference storageReference;

    //serve per fare in modo che quando carico un immagine dalla memoria venga inserita nell'imageview
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                immagineOggetto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Inserisci Prodotto");
        actionBar.setDisplayHomeAsUpEnabled(true);

        nomeProdotto = findViewById(R.id.nome_prodotto);
        input_descrizione = findViewById(R.id.descrizione);
        input_prezzo = findViewById(R.id.input_prezzo);
        inviodati = findViewById(R.id.btn_invio_prodoto);
        immagineOggetto = findViewById(R.id.foto_prodotto);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        oggetto= new Oggetto();

        immagineOggetto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent profileIntent = new Intent();
                    profileIntent.setType("image/*");
                    profileIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(profileIntent, "Select Image."), PICK_IMAGE);
                }
            });


        databaseReference = FirebaseDatabase.getInstance().getReference().child("oggetti");
        inviodati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        //Invio dei dati di carattere prettamente
                        final String nome = nomeProdotto.getText().toString();
                        final int prezzo = Integer.parseInt(input_prezzo.getText().toString());
                        final String nome_venditore = currentUser.getDisplayName();
                        final String descrizione = input_descrizione.getText().toString();

                        //inserimento dei dati dell'oggetto
                        oggetto.setPrezzo(prezzo);
                        oggetto.setNomeVenditore(nome_venditore);
                        oggetto.setNome(nome);
                        oggetto.setDescrizione(descrizione);
                        oggetto.setIdUser(currentUser.getUid());
                        databaseReference.push().setValue(oggetto);

                        //inserimento in firebase dell'immagine dell'oggetto
                        firebaseStorage = FirebaseStorage.getInstance();
                        storageReference = firebaseStorage.getReference();

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference(mAuth.getUid());
                        StorageReference imageReference = storageReference.child("Image").child("ImmaginiOggetti").child(mAuth.getUid()).child(nome); //User id/Images/Profile Pic.jpg
                        UploadTask uploadTask = imageReference.putFile(imagePath); //uri dell'immagine
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Inserimento.this, "Error: Uploading profile picture", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(Inserimento.this, "Profile picture uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Toast.makeText(Inserimento.this, "Oggetto inserito", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Inserimento.this, HomeScreen.class);
                        startActivity(intent);

        } catch (NullPointerException e) {
            Toast.makeText(Inserimento.this, getString(R.string.inforequired), Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e){
            Toast.makeText(Inserimento.this, getString(R.string.inforequired), Toast.LENGTH_SHORT).show();
        }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        this.finish();
        return true;
    }


}
