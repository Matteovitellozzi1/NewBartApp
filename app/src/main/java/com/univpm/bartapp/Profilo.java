package com.univpm.bartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.univpm.bartapp.fragment.fragment_profilo;

public class Profilo extends AppCompatActivity implements View.OnClickListener{

// DEVO METTERE IL SALDO DELLA MONETA VIRTUALE!

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    Button btnDelete;
    Button btnModificaNome;
    Button btnModificaPassword;

    public static final int LOGIN_REQUEST=101;
    private fragment_profilo fragmentProfilo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);
        fragmentProfilo = new fragment_profilo();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , fragmentProfilo).commit();
        btnDelete=(Button) findViewById(R.id.btn_eliminaAccount);
        btnModificaNome=(Button) findViewById(R.id.btn_modificaNome);
        btnModificaPassword=(Button) findViewById(R.id.btn_modificaPassword);
        btnDelete.setOnClickListener(this);
        btnModificaNome.setOnClickListener(this);
        btnModificaPassword.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_eliminaAccount:  eliminaAccount(v);
            break;
            case R.id.btn_modificaNome: modificaNome(v);
            break;
            case R.id.btn_modificaPassword: modificaPassword(v);
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
}
