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

    //Definicion de un objeto estatico que representa la actividad actual, servira para referenciar esta actividad en otras clases
    public static Activity eM;

    //Metodo que se ejecuta al crear la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();   //Ejecuta el metodo que carga el idioma de la aplicacion
        setContentView(R.layout.activity_employee_menu);  //Funcion que carga el layout de la interfaz
        eM = this;  //Se declara el contexto de la actividad actual en la variable definida anteriormente
    }

    //Metodo que redirige a la actividad que muestra la lista de rutinas
    public void redirListaRutinas(View view) {
        Intent listaRutinas = new Intent(this, routineList.class);
        startActivity(listaRutinas.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    //Metodo que redirige a la actividad que muestra la lista de clientes
    public void redirListaClientes(View view) {
        Intent listaClientes = new Intent(this, clientList.class);
        startActivity(listaClientes.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    //Metodo que redirige a la actividad que muestra la lista de maquinas
    public void redirListaMaquinas(View view) {
        Intent listaMaquinas = new Intent(this, machinesList.class);
        startActivity(listaMaquinas.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    //Metodo que redirige a la actividad que muestra las estadisticas del gimnasio
    public void redirGimStats(View view) {
        Intent stats = new Intent(this, gimStats.class);
        startActivity(stats.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    //Metodo del botón que redirige a la interfaz que muestra informacion del perfil del empleado
    public void redirVerPerfil(View view) {
        Intent perfil = new Intent(this, employeeProfile.class);
        startActivity(perfil.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    //Metodo que carga la variable Locale de SharedPreferences para implementarla en la actividad
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String lang = prefs.getString("My_Lang", "");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
    }

    //Metodo que se ejecuta al reiniciar la actividad
    @Override
    protected void onRestart() {
        super.onRestart();
        loadLocale(); //Ejecuta el metodo que carga el idioma de la aplicacion
        startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));   //Refresca la actividad para reflejar los cambios de idioma
    }
}