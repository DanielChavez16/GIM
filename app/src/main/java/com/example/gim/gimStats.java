package com.example.gim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class gimStats extends AppCompatActivity {

    //Metodo que se ejecuta al crear la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gim_stats);
    }

    //Metodo del botón que regresa al menu de empleado
    public void returnMenu(View view) {
        finish();
    }
}