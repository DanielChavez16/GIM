package com.example.gim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

public class employeeProfile extends AppCompatActivity {

    //Declaracion de vistas que se van a utilizar
    TextView txt_name, txt_email;
    ImageView img_user;

    //Declaracion del objeto de la base de datos
    DatabaseReference databaseReference;

    //Declaracion del objeto de autenticacion de usuario
    FirebaseAuth firebaseAuth;

    //Metodo que se ejecuta al crear la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();   //Ejecuta el metodo que carga el idioma
        setContentView(R.layout.activity_employee_profile);

        //Obtencion de las vistas necesarias de la interfaz
        txt_name = findViewById(R.id.eprofile_txt_username);
        txt_email = findViewById(R.id.eprofile_txt_email);
        img_user = findViewById(R.id.eprofile_img_user);

        databaseReference = FirebaseDatabase.getInstance().getReference();  //Carga la referencia de la base de datos
        firebaseAuth = FirebaseAuth.getInstance();  //Carga la instancia de la base de datos
    }

    //Metodo que llena los campos de la interfaz con la informacion del usuario
    public void imprimirDatos() {
        FirebaseUser usuario = firebaseAuth.getCurrentUser();   //Obtiene la instancia del usuario conectado

        String id = usuario.getUid();   //Obtiene el ID del usuario conectado

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() { //Obtencion de los datos del nodo hijo "Usuario"
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.child(id).getValue(Usuario.class);   //Obtencion de los datos del nodo hijo "ID"

                txt_name.setText(usuario.getNombre());  //Inserta el nombre de usuario obtenido
                txt_email.setText(usuario.getCorreo()); //Inserta el correo electronico obtenido
                Picasso.with(getApplicationContext()).load(usuario.getImagen()).into(img_user); //Obtiene e inserta en imagen la URL obtenida
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    //Metodo del botón que regresa al menu de empleado
    public void returnMenu(View view) {
        finish();
    }

    //Metodo que cambia la foto de perfil del usuario
    public void cambiarFoto(View view) {
        Toast.makeText(this, "Cambiar imagen de perfil del usuario", Toast.LENGTH_SHORT).show();
    }

    //Metodo que cambia el correo electronico del usuario
    public void cambiarCorreo(View view) {
        Toast.makeText(this, "Cambiar correo electronico del usuario", Toast.LENGTH_SHORT).show();
    }

    //Metodo que cambia la contraseña del usuario
    public void cambiarPassword(View view) {
        Toast.makeText(this, "Cambiar la contraseña del usuario", Toast.LENGTH_SHORT).show();
    }

    //Metodo que redirige al menu de cliente
    public void cambiarMenu(View view) {
        finishAffinity();   //Termina las actividades debajo de la tarea actual
        employeeMenu.eM.finish();   //Termina la actividad del menu de empleado
        Intent cMenu = new Intent(this, clientMenu.class);
        startActivity(cMenu.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));    //Ejecuta el intent del menu de usuario
    }

    //Metodo que cierra la sesión del usuario y redirige a la pantalla de inicio de sesión
    public void salir(View view) {
        finishAffinity();   //Termina las actividades debajo de la tarea actual
        employeeMenu.eM.finish();   //Termina la actividad del menu de empleado
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));  //Ejecuta el intent de la pantalla de inicio de sesión
    }

    //Metodo que se ejecuta al presionar el botón de cambiar lenguaje de la aplicación
    public void cambiarLen(View view) {
        loadLocale();   //Metodo que carga el Locale actual de la aplicacion
        String idioma = Locale.getDefault().toString(); //Convierte la variable Locale a tipo String
        if(idioma.equals("es")) {
            setLocale("en");
        }
        else {
            setLocale("es");
        }
        finishAffinity();   //Termina las atividades debajo de la tarea actual
        startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));   //Refresca la actividad actual para reflejar los cambios de idioma
    }

    //Metodo que inserta el idioma elegido como idioma por defecto
    public void setLocale(String idioma) {
        Locale lang = new Locale(idioma);   //Conversion de la variable de cadena "idioma" a tipo Locale
        Locale.setDefault(lang); //Define la variable Locale "lang" como Locale por defecto

        Configuration config = new Configuration(); //Define la funcion de configuración
        config.setLocale(lang); //Inserta la variable Locale "lang" en la configuracion de la aplicación
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());   //Actualiza la configuracion con la informacion previamente definida

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();  //Define la funcion de SharedPreferences para editar su contenido
        editor.putString("My_Lang", idioma);  //Añade la variable de cadena "idioma" en "My_Lang" de la funcion de edicion de SharedPreferences
        editor.apply();  //Aplica los cambios realizados en SharedPreferences
    }

    //Metodo que obtiene el Locale de SharedPreferences para implementarla en la actividad
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String lang = prefs.getString("My_Lang", "");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
    }

    //Metodo que se ejecuta cada vez que se inicia la Actividad
    @Override
    protected void onStart() {
        super.onStart();
        imprimirDatos();  //Ejecuta el metodo que se encarga de llenar los datos de usuario en los campos de la interfaz
    }
}