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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ChangePassword extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private EditText editText;
    private String nuova_password, conferma_password;
    private EditText editText4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Modifica password");
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        editText = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        Button btnModifica = (Button) findViewById(R.id.button4);

        btnModifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuova_password = editText.getText().toString();
                conferma_password = editText4.getText().toString();

                if (nuova_password.length() == 0) {
                    Toast.makeText(ChangePassword.this, "Inserisci dei caratteri", Toast.LENGTH_LONG).show();
                } else if (nuova_password.length() <= 6) {
                    Toast.makeText(ChangePassword.this, "La password deve essere di almeno 6 caratteri", Toast.LENGTH_LONG).show();
                } else if (!conferma_password.equals(nuova_password)) {
                    Toast.makeText(ChangePassword.this, "Password di conferma errata", Toast.LENGTH_LONG).show();
                } else {
                    currentUser.updatePassword(nuova_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangePassword.this, "Password cambiata", Toast.LENGTH_LONG).show();
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
