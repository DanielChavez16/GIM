package com.example.gim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class clientList extends AppCompatActivity {

    //Metodo que se ejecuta al crear la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
    }

    //Metodo del botón que se regresa al menu de empleado
    public void returnMenu(View view) {
        finish();
    }
}