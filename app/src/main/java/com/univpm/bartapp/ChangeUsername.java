package com.univpm.bartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ChangeUsername extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private EditText editText;
    private String nuovo_nome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Modifica Nome Utente");
        actionBar.setDisplayHomeAsUpEnabled(true);

        editText= findViewById(R.id.editText2);
        Button btnModifica= (Button) findViewById(R.id.button);

        btnModifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuovo_nome= editText.getText().toString();
                UserProfileChangeRequest profileUpdates;
                if(nuovo_nome.equals(currentUser.getDisplayName())){
                    Toast.makeText(ChangeUsername.this, "Inserisci un nome utente diverso", Toast.LENGTH_LONG).show();
                }
                else if (nuovo_nome.length() == 0){
                    Toast.makeText(ChangeUsername.this, "Inserisci un nome valido", Toast.LENGTH_LONG).show();
                }
                else{
                    profileUpdates= new UserProfileChangeRequest.Builder().setDisplayName(nuovo_nome).build();
                    currentUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ChangeUsername.this, "Nome Utente cambiato", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        this.finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
