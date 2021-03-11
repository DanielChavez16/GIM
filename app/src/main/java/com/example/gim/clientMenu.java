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

    public static Activity cM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_client_menu);
        cM = this;
    }

    public void redirVerPerfil(View view) {
        Intent cProfile = new Intent(this, clientProfile.class);
        startActivity(cProfile.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    public void clientStats(View view) {
        Toast.makeText(this, "Ver las estadisticas del cliente", Toast.LENGTH_SHORT).show();
    }

    public void addRoutine(View view) {
        Toast.makeText(this, "Unirse a una rutina", Toast.LENGTH_SHORT).show();
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String lang = prefs.getString("My_Lang", "");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadLocale();
        startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}