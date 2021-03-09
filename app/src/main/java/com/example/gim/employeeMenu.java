package com.example.gim;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class employeeMenu extends AppCompatActivity {

    public static Activity eM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        eM = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_menu);
    }

    public void redirListaRutinas(View view) {
        Toast.makeText(this, "Lista de rutinas", Toast.LENGTH_SHORT).show();
    }

    public void redirListaClientes(View view) {
        Toast.makeText(this, "Lista de clientes", Toast.LENGTH_SHORT).show();
    }

    public void redirListaMaquinas(View view) {
        Toast.makeText(this, "Lista de maquinas", Toast.LENGTH_SHORT).show();
    }

    public void redirGimStats(View view) {
        Toast.makeText(this, "Estadisticas del gimnasio", Toast.LENGTH_SHORT).show();
    }

    public void redirVerPerfil(View view) {
        Intent perfil = new Intent(getApplicationContext(), employeeProfile.class);
        startActivity(perfil);
    }
}