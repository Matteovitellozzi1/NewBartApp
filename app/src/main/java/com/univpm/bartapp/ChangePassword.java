package com.univpm.bartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private String nuova_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        editText= findViewById(R.id.editText3);
        Button btnModifica= (Button) findViewById(R.id.button4);

        Log.i("a", currentUser.getDisplayName().toString());

        btnModifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuova_password= editText.getText().toString();

                if (nuova_password.length() == 0){
                    Toast.makeText(ChangePassword.this, "Inserisci dei caratteri", Toast.LENGTH_LONG).show();
                }
                else{


                    Log.i("a", "password modificata");
                    currentUser.updatePassword(nuova_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ChangePassword.this, "Password cambiata", Toast.LENGTH_LONG).show();
                                Intent intent= new Intent(ChangePassword.this, Profilo.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

    }
}
