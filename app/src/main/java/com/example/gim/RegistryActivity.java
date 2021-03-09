package com.example.gim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.gim.entidades.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.regex.Pattern;

public class RegistryActivity extends AppCompatActivity {

    //Declaracion de las vistas que se van a utilizar
    EditText txt_name, txt_email, txt_pass, txt_pass2;
    String name, email, password, password2;

    //Declaracion del objeto de referencia de la base de datos
    DatabaseReference databaseReference;
    //Declatacion del objeto de referencia de la autenticacion
    FirebaseAuth firebaseAuth;

    //Metodo que se ejecuta al crear la interfaz
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        //Obtenicion de las vistas de la interfaz
        txt_name = findViewById(R.id.reg_txt_name);
        txt_email = findViewById(R.id.reg_txt_email);
        txt_pass = findViewById(R.id.reg_txt_pass);
        txt_pass2 = findViewById(R.id.reg_txt_pass2);

        //Ejecucion del metodo para inicializar firebase
        inicializarFirebase();
    }

    //Metodo que obtiene la referencia de firebase y obtiene la instancia de autenticacion de firebase
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    //Metodo que hace la validacion de los datos de usuario ingresados / Se ejecuta al presionar el boton "Registrarse"
    public void registrar(View view) {
        //Obtencion de los datos de los campos de texto
        name = String.valueOf(txt_name.getText());
        email = String.valueOf(txt_email.getText());
        password = String.valueOf(txt_pass.getText());
        password2 = String.valueOf(txt_pass2.getText());

        if(!name.equals("") && validarEmail(email) && !password.equals("")) {
            if (password.length()>=6 && password.equals(password2)) {
                try{
                    //Inicia el metodo iniciarRegistro si las validaciones son correctas
                    iniciarRegistro();
                } catch(Exception e) {
                    //Error: No se puede iniciar el registro por razones externas
                    Toast.makeText(this, R.string.reg_msg_1, Toast.LENGTH_LONG).show();
                }
            } else {
                if(password.length() < 6) {
                    //Error: modificar la longitud de la contraseña introducida
                    Toast.makeText(this, R.string.reg_msg_2, Toast.LENGTH_LONG).show();
                }
                else {
                    //Error: Las contraseñas introducidas no coinciden
                    Toast.makeText(this, R.string.reg_msg_3, Toast.LENGTH_LONG).show();
                }
            }
        }
        else {
            //Errores que indican que los datos obtenidos en los campos de texto no son aceptados
            if(!email.equals("") && !validarEmail(email)) {
                Toast.makeText(this, R.string.reg_msg_4, Toast.LENGTH_SHORT).show();
            }
            if(name.equals("") || email.equals("") || password.equals("")) {
                Toast.makeText(this, R.string.reg_msg_5, Toast.LENGTH_LONG).show();
            }
        }
    }

    //Metodo que registra los datos introducidos en la base de datos
    private void iniciarRegistro() {
        //Funcion que registra al usuario en el sistema usando correo y contraseña / Registra los datos del usuario en la base de datos
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //Declaracion del objeto que contiene los datos del usuario recientemente creado en el sistema
                    FirebaseUser userNew = firebaseAuth.getCurrentUser();
                    //Funcion que envia al usuario un correo para verificar su correo electronico
                    userNew.sendEmailVerification();

                    Usuario user = new Usuario();
                    user.setId(userNew.getUid());
                    user.setNombre(name);
                    user.setCorreo(email);
                    user.setMembresia("Prueba");
                    user.setTipo("Cliente");
                    user.setImagen("https://firebasestorage.googleapis.com/v0/b/gim-84f3f.appspot.com/o/Usuarios%2Fimg_user_pic.jpg?alt=media&token=ffa09828-91bf-42e5-84d1-f49457cb113a");
                    //Funcion que inserta los datos del usuario en la base de datos
                    databaseReference.child("Usuario").child(user.getId()).getDatabase();
                    databaseReference.child("Usuario").child(user.getId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(RegistryActivity.this, R.string.reg_msg_6, Toast.LENGTH_LONG).show();
                            }
                            else {
                                //Error: Los datos del usuario no se pudieron insertar en la base de datos
                                Toast.makeText(RegistryActivity.this, R.string.reg_msg_7, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    //Error: No se pudo crear el usuario por razones externas
                    Toast.makeText(RegistryActivity.this, R.string.reg_msg_8, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Metodo booleano que valida el correo electronico introducido
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    //Metodo que cierra la actividad actual y regresa al menu de inicio de sesión
    public void regresarLogin(View view) {
        finish();
    }
}