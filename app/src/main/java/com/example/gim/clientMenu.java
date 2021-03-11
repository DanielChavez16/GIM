package com.example.gim;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

public class clientMenu extends AppCompatActivity {

    //Definicion del objeto que representa la actividad actual, servira para referenciar esta actividad en otras clases
    public static Activity cM;

    //Metodo que se ejecuta al crear la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();  //Ejecuta el metodo que carga el idioma de la aplicacion
        setContentView(R.layout.activity_client_menu);  //Funcion que carga el layout de la interfaz
        cM = this;  //De declara el contexto de la actividad actual en la variable definida anteriormente
    }

    //Metodo del botón que redirige a la intefaz que muestra la informacion del perfil del usuario
    public void redirVerPerfil(View view) {
        Intent cProfile = new Intent(this, clientProfile.class);
        startActivity(cProfile.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    //Metodo que redirige a la actividad que muestra las estadisticas personales del cliente
    public void clientStats(View view) {
        Toast.makeText(this, "Ver las estadisticas del cliente", Toast.LENGTH_SHORT).show();
    }

    //Metodo que redirige a la pantalla de seleccion de rutinas para que el cliente puede unirse
    public void addRoutine(View view) {
        Toast.makeText(this, "Unirse a una rutina", Toast.LENGTH_SHORT).show();
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
        loadLocale();  //Ejecuta el metodo que carga el idioma de la aplicación
        startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));  //Refresca la actividad para reflejar los cambios de idioma
    }
}