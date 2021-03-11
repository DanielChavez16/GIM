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
    //Declaracion del objeto de autenticacion de usuario
    FirebaseAuth firebaseAuth;

    //Metodo que se ejecuta al crear la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();  //Ejecuta el metodo que carga el idioma
        setContentView(R.layout.activity_client_profile);

        //Obtencion de las vistas necesarias de la interfaz
        txt_name = findViewById(R.id.cprofile_txt_username);
        txt_email = findViewById(R.id.cprofile_txt_email);
        txt_sub = findViewById(R.id.cprofile_txt_sub);
        txt_eMenu = findViewById(R.id.cprofile_txt_eMenu);
        img_user = findViewById(R.id.cprofile_img_user);

        databaseReference = FirebaseDatabase.getInstance().getReference();  //Carga la referencia de la base de datos
        firebaseAuth = FirebaseAuth.getInstance();  //Carga la instancia de la base de datos
    }

    //Metodo que llena los campos de la interfaz con la informacion de usuario
    public void imprimirDatos() {
        FirebaseUser usuario = firebaseAuth.getCurrentUser();  //Obtiene la instancia del usuario conectado

        String id = usuario.getUid();  //Obtiene el ID del usuario conectado

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {  //Obtiene los datos del nodo hijo "Usuario"
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.child(id).getValue(Usuario.class);  //Obtencion de los datos del nodo hijo "ID"

                txt_name.setText(usuario.getNombre());  //Inserta el nombre de usuario obtenido
                txt_email.setText(usuario.getCorreo());  //Inserta el correo electronico obtenido
                String text = txt_sub.getText().toString() + ": " + usuario.getMembresia();  //Crea una cadena con el tipo de membresia
                txt_sub.setText(text);  //Inserta la cadena creada en la linea anterior
                Picasso.with(getApplicationContext()).load(usuario.getImagen()).into(img_user);  //Obtiene e inserta en imagen la URL obtenida

                String tipo = usuario.getTipo();  //Obtiene el tipo de usuario
                if(tipo.equals("Empleado")) {  //Realiza la verificacion si el tipo de usuario es "Empleado"
                    txt_eMenu.setVisibility(View.VISIBLE);  //Vuelve visible la referencia para acceder al menu de empleado
                    txt_eMenu.setClickable(true);  //Vuelve interactuable la referencia para accede al menu de empleado
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
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
        Toast.makeText(this, "Cambiar contraseña del usuario", Toast.LENGTH_SHORT).show();
    }

    //Metodo que redirige al menu de empleado
    public void cambiarMenu(View view) {
        finishAffinity();  //Termina las actividades debajo de la tarea actual
        clientMenu.cM.finish();  //Termina la actividad del menu de cliente
        Intent eMenu = new Intent(this, employeeMenu.class);
        startActivity(eMenu.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));  //Ejecuta el intent del menu de empleado
    }

    //Metodo que cierra la sesión del usuario y redirige a la pantalla de inicio de sesión
    public void salir(View view) {
        finishAffinity();  //Termina laas actividades debajo de la tarea actual
        clientMenu.cM.finish();  //Termina la actvidad del menu de cliente
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));  //Ejecuta el intent de la pantalla de incio de sesión
    }

    //Metodo que se ejecuta al presionar el botón de cambiar lenguaje
    public void cambiarLen(View view) {
        loadLocale();  //Metodo que carga el Locale actual de la aplicación
        String idioma = Locale.getDefault().toString();  //Conversión de la variable Locale a tipo cadena
        if(idioma.equals("es")) {
            setLocale("en");
        }
        else {
            setLocale("es");
        }
        finishAffinity();  //Termina las actividades debajo de la tarea actual
        startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));  //Refresca la actividad actual para reflejar los cambios de idioma
    }

    //Metodo que inserta el idioma elegido como idioma por defecto
    public void setLocale(String idioma) {
        Locale lang = new Locale(idioma);  //Conversion de la variable de cadena a variable de tipo Locale
        Locale.setDefault(lang);  //Define la variable locale "lang" como Locale por defecto

        Configuration config = new Configuration();  //Define la funcion de configuración
        config.setLocale(lang);  //Inserta la variable Locale "lang" en la configuracion de la aplicación
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());  //Actualiza la configuracion con la informacion previamente definida

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();  //Define la funcion de SharedPreferences para editar su contenido
        editor.putString("My_Lang", idioma);  //Añade la variable de cadena "idioma" en "My_Lang" de la funcion de edición de SharedPreferences
        editor.apply();  //Aplica los cambios realizados en SharedPreferences
    }

    //Metodo qeu obtiene el Locale de SharedPreferences para implementara en la actualidad
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String idioma = prefs.getString("My_Lang", "");
        Locale lang = new Locale(idioma);
        Locale.setDefault(lang);
    }

    //Metodo que se ejecuta al iniciar la actividad
    @Override
    protected void onStart() {
        super.onStart();
        imprimirDatos();  //Ejecuta el metodo que se encarga de llenar los datos de usuario en los campos de la interfaz
    }
}