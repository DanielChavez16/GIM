package com.example.gim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    //Declaracion de las vistas que se van a utilizar
    EditText txt_email, txt_pass;
    String email, pass;

    //Declaracion del objeto de autenticacion en Firebase
    FirebaseAuth firebaseAuth;

    //Metodo que se ejecuta al crear la interfaz
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtencion de las vistas de la interfaz
        txt_email = findViewById(R.id.login_txt_email);
        txt_pass = findViewById(R.id.login_txt_pass);

        //Obtencion de la instancia la autenticacion de Firebase
        firebaseAuth = FirebaseAuth.getInstance();
    }

    //Metodo que redirige a la pantalla de registro y limpia los campos de texto / Se ejecuta al presionar el boton "Regístrate"
    public void redirRegistro(View view) {
        Intent registro = new Intent(this, RegistryActivity.class);
        startActivity(registro);
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
                        //Error: El usuario no se encuentra en el registro de usuarios de firebase
                        Toast.makeText(MainActivity.this, "No existe usuario", Toast.LENGTH_LONG).show();
                    } else {
                        //Creacion del objeto "user" que indica si el usuario a validado su correo electronico
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        if (user != null) {
                            if (!user.isEmailVerified()) {
                                //Error: El usuario existe pero no puede iniciar sesion hasta verificar su correo electronico
                                Toast.makeText(MainActivity.this, "El correo electronico no esta verificado", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, "El usuario inicio sesión correctamente", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            //Error: No se pudo iniciar sesion por razones externas
                            Toast.makeText(MainActivity.this, "No se pudo iniciar sesión", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
        else {
            //Errores que indican que los datos obtenidos en los campos de texto no son aceptados
            if(!email.equals("") && !validarEmail(email)) {
                Toast.makeText(this, "Correo no valido", Toast.LENGTH_SHORT).show();
            }
            if(email.equals("") || pass.equals("")) {
                Toast.makeText(this, "Error: Llena todos los campos", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Metodo booleano que valida el correo electronico introducido
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}