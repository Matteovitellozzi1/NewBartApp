package com.univpm.bartapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import com.univpm.bartapp.fragment.fragment_profilo;

public class Profilo extends AppCompatActivity {

    public static final int LOGIN_REQUEST=101;
    private fragment_profilo fragmentProfilo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);
        fragmentProfilo = new fragment_profilo();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , fragmentProfilo).commit();
    }
}
