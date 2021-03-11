package com.example.gim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gim.entidades.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class clientProfile extends AppCompatActivity {

    //Declaracion de vistas que se van a utilizar
    TextView txt_name, txt_email, txt_sub, txt_eMenu;
    ImageView img_user;

    //Declaracion del objeto de la base de datos
    DatabaseReference databaseReference;
    //Declaracion de la autenticacion de usuario
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_client_profile);

        txt_name = findViewById(R.id.cprofile_txt_username);
        txt_email = findViewById(R.id.cprofile_txt_email);
        txt_sub = findViewById(R.id.cprofile_txt_sub);
        txt_eMenu = findViewById(R.id.cprofile_txt_eMenu);
        img_user = findViewById(R.id.cprofile_img_user);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void imprimirDatos() {
        FirebaseUser usuario = firebaseAuth.getCurrentUser();

        String id = usuario.getUid();

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.child(id).getValue(Usuario.class);

                txt_name.setText(usuario.getNombre());
                txt_email.setText(usuario.getCorreo());
                String text = txt_sub.getText().toString() + ": " + usuario.getMembresia();
                txt_sub.setText(text);
                Picasso.with(getApplicationContext()).load(usuario.getImagen()).into(img_user);

                String tipo = usuario.getTipo();
                if(tipo.equals("Empleado")) {
                    txt_eMenu.setVisibility(View.VISIBLE);
                    txt_eMenu.setClickable(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void returnMenu(View view) {
        finish();
    }

    public void cambiarFoto(View view) {
        Toast.makeText(this, "Cambiar imagen de perfil del usuario", Toast.LENGTH_SHORT).show();
    }

    public void cambiarCorreo(View view) {
        Toast.makeText(this, "Cambiar correo electronico del usuario", Toast.LENGTH_SHORT).show();
    }

    public void cambiarPassword(View view) {
        Toast.makeText(this, "Cambiar contraseña del usuario", Toast.LENGTH_SHORT).show();
    }

    public void cambiarMenu(View view) {
        finish();
        clientMenu.cM.finish();
        Intent eMenu = new Intent(this, employeeMenu.class);
        startActivity(eMenu.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

    public void salir(View view) {
        finishAffinity();
        clientMenu.cM.finish();
        startActivity(MainActivity.main.getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    public void cambiarLen(View view) {
        loadLocale();
        String idioma = Locale.getDefault().toString();
        if(idioma.equals("es")) {
            setLocale("en");
        }
        else {
            setLocale("es");
        }
        finishAffinity();
        startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void setLocale(String idioma) {
        Locale lang = new Locale(idioma);
        Locale.setDefault(lang);

        Configuration config = new Configuration();
        config.setLocale(lang);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", idioma);
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String idioma = prefs.getString("My_Lang", "");
        Locale lang = new Locale(idioma);
        Locale.setDefault(lang);
    }

    @Override
    protected void onStart() {
        super.onStart();
        imprimirDatos();
    }
}