package com.univpm.bartapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registrazione extends AppCompatActivity {

    private TextInputEditText textNome, textCognome, textEmail, textPassword;
    private Button btnRegistra;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);
        mAuth = FirebaseAuth.getInstance();
        textNome= findViewById(R.id.text_nome);
        textCognome= findViewById(R.id.text_cognome);
        textEmail=findViewById(R.id.text_email);
        textPassword=findViewById(R.id.text_Password);
        btnRegistra=findViewById(R.id.btnRegistra);
        btnRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String nome= textNome.getText().toString();
                    final String cognome= textCognome.getText().toString();
                    final String email=textEmail.getText().toString();
                    final String password=textPassword.getText().toString();
                    //metodo predefinito di firebase per registrarsi
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final FirebaseUser user = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileChangeRequest= new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nome+ " "+ cognome)
                                        .build();
                                user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        writeuserToDb(nome, cognome, user.getUid(), email);
                                        Intent intent= new Intent();
                                        intent.putExtra("nome", textNome.getText().toString());
                                        intent.putExtra("cognome", textCognome.getText().toString());
                                        intent.putExtra("email", textEmail.getText().toString());
                                        intent.putExtra("password",textPassword.getText().toString());
                                        Intent intent1 = new Intent(Registrazione.this, Login_Screen.class);
                                        startActivity(intent1);
                                    }
                                });

                            }
                            else{
                                task.getException().printStackTrace();
                                Toast.makeText(Registrazione.this, getString(R.string.errorsignup), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                catch (NullPointerException e) {
                    Toast.makeText(Registrazione.this, getString(R.string.inforequired), Toast.LENGTH_SHORT).show();
                } catch (IllegalArgumentException e){
                    Toast.makeText(Registrazione.this, getString(R.string.inforequired), Toast.LENGTH_SHORT).show();
                } /* oppure semplicemente un unico catch con Exception e*/

            }
        });

        getSupportActionBar().setTitle(getString(R.string.Registrati));

    }

    private void writeuserToDb(String nome, String cognome, String uid, String email){
        Map<String, Object> user= new HashMap<>();
        user.put("nome", nome);
        user.put("cognome", cognome);
        user.put("email", email);
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("utenti"). document(uid).set(user);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(Registrazione.this, Login_Screen.class);
        startActivity(intent1);
    }
}