package com.univpm.bartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class Profilo extends AppCompatActivity implements View.OnClickListener {

// DEVO METTERE IL SALDO DELLA MONETA VIRTUALE!

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private static int PICK_IMAGE=123;


    Button btnDelete;
    ImageButton btnModificaNome;
    ImageButton btnModificaPassword;
    Button btnSalvaModifica;
    TextView nomeCognome, nomeUtente, emailProfilo;
    ImageView immagineProfilo;
    Uri imagePath;
    FrameLayout frameLayout;

    public static final int LOGIN_REQUEST=101;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                immagineProfilo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profilo");
        actionBar.setDisplayHomeAsUpEnabled(true);

        nomeCognome = (TextView) findViewById(R.id.text_nome);
        nomeUtente = (TextView) findViewById(R.id.nome_utente);
        emailProfilo = (TextView) findViewById(R.id.emailProfilo);
        immagineProfilo = (ImageView) findViewById(R.id.propic);
        btnDelete = (Button) findViewById(R.id.btn_eliminaAccount);
        btnModificaNome = (ImageButton) findViewById(R.id.edit1);
        btnModificaPassword = (ImageButton) findViewById(R.id.edit2);
        btnSalvaModifica = (Button) findViewById(R.id.btn_modifica_immagine);
        btnDelete.setOnClickListener(this);
        btnModificaNome.setOnClickListener(this);
        btnModificaPassword.setOnClickListener(this);

        btnSalvaModifica.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        nomeCognome.setText(currentUser.getDisplayName());
        nomeUtente.setText(currentUser.getDisplayName());
        emailProfilo.setText(currentUser.getEmail());

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        storageReference.child("Image").child("Profile Pic").child(mAuth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(immagineProfilo);
            }
        });


        immagineProfilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityProfilo = new Intent();
                activityProfilo.setType("image/*");
                activityProfilo.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(activityProfilo, "Seleziona immagine."), PICK_IMAGE);

                btnSalvaModifica.setVisibility(View.VISIBLE);
                btnSalvaModifica.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StorageReference imageReference = storageReference.child("Image").child("Profile Pic").child(mAuth.getUid());
                        Log.i("TAG", "prima dell'uri");
                        //UploadTask uploadTask = imageReference.putFile(imagePath);
                        if (imagePath != null) {
                            sendUserData();
                        }
                        else{
                            Toast.makeText(Profilo.this, "Non hai inserito alcuna immagine!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(mAuth.getUid());
        StorageReference imageReference = storageReference.child("Image").child("Profile Pic").child(mAuth.getUid()); //User id/Images/Profile Pic.jpg
        UploadTask uploadTask = imageReference.putFile(imagePath); //uri dell'immagine
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profilo.this, "Error: Uploading profile picture", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Profilo.this, "Profile picture uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_eliminaAccount:  eliminaAccount(v);
                break;
            case R.id.edit1: modificaNome(v);
                break;
            case R.id.edit2: modificaPassword(v);
                break;
        }
    }


    public void modificaNome(View v){

        Intent intent= new Intent(Profilo.this, ChangeUsername.class);
        startActivity(intent);
    }

    public void modificaPassword(View v){

        Intent intent= new Intent(Profilo.this, ChangePassword.class);
        startActivity(intent);
    }


    public void eliminaAccount(View view) {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        AlertDialog.Builder dialog= new AlertDialog.Builder(Profilo.this);
        dialog.setTitle("Sei sicuro?");

        dialog.setMessage("Cancellare questo account comporterà la sua totale" +
                "rimozione e non potrai più accedere all'app");
        dialog.setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String idUser =currentUser.getUid();
                StorageReference storageReference=FirebaseStorage.getInstance().getReference().child("Image").child("Profile Pic");
                storageReference.child(idUser).delete();
                currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Profilo.this, "Account Eliminato", Toast.LENGTH_LONG).show();
                            Intent intent= new Intent(Profilo.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Profilo.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
        dialog.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog= dialog.create();
        alertDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        this.finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        this.finish();
    }
}