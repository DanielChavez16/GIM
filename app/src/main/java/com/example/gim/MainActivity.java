package com.example.gim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gim.entidades.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    //Declaracion de las vistas que se van a utilizar
    EditText txt_email, txt_pass;
    String email, pass;

    //Declaracion del objeto de la base de datos
    DatabaseReference databaseReference;

    //Declaracion del objeto de autenticacion en Firebase
    FirebaseAuth firebaseAuth;

    //Metodo que se ejecuta al crear la interfaz
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();   //Ejecuta el metodo que carga el idioma
        setContentView(R.layout.activity_main);

        //Obtencion de las vistas de la interfaz
        txt_email = findViewById(R.id.login_txt_email);
        txt_pass = findViewById(R.id.login_txt_pass);

        databaseReference = FirebaseDatabase.getInstance().getReference(); //Obtencion de la referencia de la base de datos
        firebaseAuth = FirebaseAuth.getInstance();  //Obtencion de la instancia la autenticacion de Firebase
    }

    //Metodo que redirige a la pantalla de registro y limpia los campos de texto / Se ejecuta al presionar el boton "Regístrate"
    public void redirRegistro(View view) {
        Intent reg = new Intent(this, RegistryActivity.class);
        startActivity(reg.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        txt_email.setText("");
        txt_pass.setText("");
    }

    //Metodo que realiza las validaciones necesarias y redirige al menu principal del cliente/empleado / Se ejecuta al presionar el botion "Iniciar Sesión"
    public void redirMenu(View view) {
        //Obtencion de los datos escritos en los campos de texto
        email = txt_email.getText().toString();
        pass = txt_pass.getText().toString();

        //Verificacion si los datos escritos son aceptables
        if(validarEmail(email) && !pass.equals("")) {
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, R.string.main_msg_1, Toast.LENGTH_LONG).show();  //Error: El usuario no se encuentra en el registro de usuarios de firebase
                    } else {
                        FirebaseUser user = firebaseAuth.getCurrentUser();  //Creacion del objeto "user" que indica si el usuario a validado su correo electronico

                        if (user != null) {
                            String ID = user.getUid();  //Obtiene el identificador de usuario

                            if (!user.isEmailVerified()) {  //Condiciona si el correo del usuario no esta verificado
                                Toast.makeText(MainActivity.this, R.string.main_msg_2, Toast.LENGTH_LONG).show();  //Error: El usuario existe pero no puede iniciar sesion hasta verificar su correo electronico
                            } else {  //Si el correo del usuario esta validado ejecuta el siguiente codigo
                                databaseReference.child("Usuario").addListenerForSingleValueEvent(new ValueEventListener() {  //Carga la referencia de la base de datos en el nodo "Usuario"
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Usuario usuario = snapshot.child(ID).getValue(Usuario.class);  //Carga los datos obtenidos del nodo ID dado y los envia al constructor de la entidad "Usuario"

                                        String tipoUsuario = usuario.getTipo();  //Escribe el tipo de usuario del nodo obtenido en una variable de cadena

                                        if(tipoUsuario.equals("Empleado")) {  //Condiciona si la variable "tipoUsuario" obtenida contiene el valor de "Empleado"
                                            Intent eMenu = new Intent(getApplicationContext(), employeeMenu.class);
                                            startActivity(eMenu.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));  //Ejecuta el intent del menu de empleado definido en la linea anterior
                                            finish();  //Termina la actividad actual
                                        }
                                        else {  //Si la variable "tipoUsuario" contiene un valor diferente a "Empleado" se ejecuta el siguiente codigo
                                            Intent cMenu = new Intent(getApplicationContext(), clientMenu.class);
                                            startActivity(cMenu.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));  //Ejecuta el intent del menu de cliente definido en la linea anterior
                                            finish();  //Termina la actividad actual
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {}
                                });
                            }
                        } else {
                            Toast.makeText(MainActivity.this, R.string.main_msg_3, Toast.LENGTH_LONG).show();  //Error: No se pudo iniciar sesion por razones externas
                        }
                    }
                }
            });
        }
        else {
            //Errores que indican que los datos obtenidos en los campos de texto no son aceptados
            if(!email.equals("") && !validarEmail(email)) {
                Toast.makeText(this, R.string.main_msg_4, Toast.LENGTH_SHORT).show();
            }
            if(email.equals("") || pass.equals("")) {
                Toast.makeText(this, R.string.main_msg_5, Toast.LENGTH_LONG).show();
            }
        }
    }

    //Metodo que obtiene el lenguaje actual y ejecuta el metodo para cambiarlo
    public void cambiarLen(View view) {
        loadLocale();
        String idioma = Locale.getDefault().toString();
        if(idioma.equals("es")) {
            setLocale("en");
        } else {
            setLocale("es");
        }
        finish();
        startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    //Metodo que establece el lenguaje de la aplicacion y lo guarda en la memoria
    public void setLocale(String locale) {
        Locale lang = new Locale(locale);
        Locale.setDefault(lang);

        Configuration config = new Configuration();
        config.setLocale(lang);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", locale);
        editor.apply();
    }

    //Metodo que carga el idioma de la memoria
    public void  loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String lang = prefs.getString("My_Lang", "");
        setLocale(lang);
    }

    //Metodo booleano que valida el correo electronico introducido
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}