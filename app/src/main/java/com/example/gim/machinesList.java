package com.example.gim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class machinesList extends AppCompatActivity {

    //Metodo que se ejecuta al crear la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machines_list);
    }

    //Metodo del botón que regresa al menu empleado
    public void returnMenu(View view) {
        finish();
    }

    //Metodo que crea la interfaz para añadir una maquina
    public void addMachine(View view) {
        Toast.makeText(this, "Añadir maquina", Toast.LENGTH_SHORT).show();
    }
}