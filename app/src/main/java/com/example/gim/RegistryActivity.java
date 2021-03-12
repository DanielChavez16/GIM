package com.example.gim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        inicializarFirebase();  //Ejecion del metodo para inicializar la base de datos
    }

    //Metodo que obtiene la referencia de firebase y obtiene la instancia de autenticacion de firebase
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);  //Inicializa la base de datos en la aplicacion
        databaseReference = FirebaseDatabase.getInstance().getReference();  //Obtiene la referencia de la base de datos
        firebaseAuth = FirebaseAuth.getInstance();  //Obtiene la instancia de la autenticacion de la base de datos
    }

    //Metodo que hace la validacion de los datos de usuario ingresados / Se ejecuta al presionar el boton "Registrarse"
    public void registrar(View view) {
        //Obtencion de los datos de los campos de texto
        name = String.valueOf(txt_name.getText());
        email = String.valueOf(txt_email.getText());
        password = String.valueOf(txt_pass.getText());
        password2 = String.valueOf(txt_pass2.getText());

        if(!name.equals("") && validarEmail(email) && !password.equals("")) {  //Condicional que valida si los campos de texto no esten vacios o sean validos
            if (password.length()>=6 && password.equals(password2)) {  //Condicional que valida la longitud de la contraseña y si las contraseñas introducisdas coinciden
                try{
                    iniciarRegistro();  //Inicializa el metodo para iniciar registro si las validaciones son correctas
                } catch(Exception e) {
                    Toast.makeText(this, R.string.reg_msg_1, Toast.LENGTH_LONG).show(); //Error: No se puede iniciar el regsitro por razones externas
                }
            } else {
                if(password.length() < 6) {
                    Toast.makeText(this, R.string.reg_msg_2, Toast.LENGTH_LONG).show();  //Error: La longitud de la contraseña no es aceptada
                }
                else {
                    Toast.makeText(this, R.string.reg_msg_3, Toast.LENGTH_LONG).show();  //Las contraseñas introducidas no coinciden
                }
            }
        }
        else {  //Si las condiciones anteriores no son verdaderas, se ejecutan las siguientes lineas que muestran mensajes de error
            if(!email.equals("") && !validarEmail(email)) {  //Condicional que valida si el campo del correo electronico no es valido o esta vacio
                Toast.makeText(this, R.string.reg_msg_4, Toast.LENGTH_SHORT).show();  //Error el correo electronico introducido no es valido
            }
            if(name.equals("") || email.equals("") || password.equals("")) {  //Condicional que valida si los campos indicados estan vacios
                Toast.makeText(this, R.string.reg_msg_5, Toast.LENGTH_LONG).show();  //Error: Los campos de texto estan vacios
            }
        }
    }

    //Metodo que registra los datos introducidos en la base de datos
    private void iniciarRegistro() {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {  //Funcion que registra al usuario en el sistema usando correo y contraseña
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {  //Funcion que se ejecuta al finalizar el registro del usuario en el sistema
                if(task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();  //Creacion de la instancia que contiene al usuario actual

                    Usuario usuario = new Usuario();  //Creacion de la instancia que contiene al constructor de la entidad "Usuario"
                    usuario.setId(user.getUid());  //Carga el ID de usuario del sistema
                    usuario.setNombre(name);
                    usuario.setCorreo(email);
                    usuario.setMembresia("Prueba");
                    usuario.setTipo("Cliente");
                    usuario.setImagen("https://firebasestorage.googleapis.com/v0/b/gim-84f3f.appspot.com/o/Usuarios%2Fimg_user_pic.jpg?alt=media&token=ffa09828-91bf-42e5-84d1-f49457cb113a");  //Carga la imagen de perfil por defecto
                    databaseReference.child("Usuario").child(usuario.getId()).getDatabase();  //Obtiene la base de datos asociada a la refernecia
                    databaseReference.child("Usuario").child(usuario.getId()).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {  //Inserta los datos del constructor "Usuario" en la base de datos
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {  //Funcion que se ejecuta al finalizar de insertar datos en la base de datos
                            if(task.isSuccessful()) {  //Condicional que valida si la tarea se ejecuto correctamente
                                user.sendEmailVerification();  //Funcion que envia al usuario un correo para verificar su correo electronico
                                Toast.makeText(RegistryActivity.this, R.string.reg_msg_6, Toast.LENGTH_LONG).show();
                            }
                            else {  //Si la tarea no se ejecuto con exito
                                Toast.makeText(RegistryActivity.this, R.string.reg_msg_7, Toast.LENGTH_LONG).show();  //Error: Los datos del usuario no se pudieron insertar en la base de datos
                            }
                        }
                    });
                }
                else {  //Si no pudo registrar el usuario en el sistema se ejecutan las siguientes lineas de codigo
                    Toast.makeText(RegistryActivity.this, R.string.reg_msg_8, Toast.LENGTH_LONG).show();  //Error: No se pudo crear el usuario por razones externas
                }
            }
        });
    }

    //Metodo booleano que valida el correo electronico introducido
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;  //Crea la instancia "pattern", que valida la escritura del correo electrnico
        return pattern.matcher(email).matches();  //Regresa el resultado de la validacion
    }

    //Metodo que cierra la actividad actual y regresa al menu de inicio de sesión
    public void regresarLogin(View view) {
        finish();
    }
}