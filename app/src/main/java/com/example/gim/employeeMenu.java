package com.example.gim;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

public class employeeMenu extends AppCompatActivity {

    public static Activity eM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_employee_menu);
        eM = this;
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

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String lang = prefs.getString("My_Lang", "");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
    }
}