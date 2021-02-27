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

import java.util.UUID;
import java.util.regex.Pattern;

public class RegistryActivity extends AppCompatActivity {

    EditText txt_name, txt_email, txt_pass, txt_pass2;
    String name, email, password, password2;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        txt_name = findViewById(R.id.reg_txt_name);
        txt_email = findViewById(R.id.reg_txt_email);
        txt_pass = findViewById(R.id.reg_txt_pass);
        txt_pass2 = findViewById(R.id.reg_txt_pass2);

        inicializarFirebase();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void registrar(View view) {
        name = String.valueOf(txt_name.getText());
        email = String.valueOf(txt_email.getText());
        password = String.valueOf(txt_pass.getText());
        password2 = String.valueOf(txt_pass2.getText());

        if(!name.equals("") && validarEmail(email) && !password.equals("")) {
            if (password.length()>=6 && password.equals(password2)) {
                try{
                    iniciarRegistro();
                } catch(Exception e) {
                    Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_LONG).show();
                }
            } else {
                if(password.length() < 6) {
                    Toast.makeText(this, "Error: La contraseña debe tener mas de 6 caracteres", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "Error: Confirma tu contraseña", Toast.LENGTH_LONG).show();
                }
            }
        }
        else {
            if(!email.equals("") && !validarEmail(email)) {
                Toast.makeText(this, "Correo no valido", Toast.LENGTH_SHORT).show();
            }
            if(name.equals("") || email.equals("") || password.equals("")) {
                Toast.makeText(this, "Error: Llena todos los campos", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void iniciarRegistro() {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser userAuth = firebaseAuth.getCurrentUser();
                    userAuth.sendEmailVerification();

                    Usuario user = new Usuario();
                    user.setId(userAuth.getUid());
                    user.setNombre(name);
                    user.setCorreo(email);
                    user.setPassword(password);
                    user.setMembresia("Prueba");
                    user.setTipo("Cliente");
                    databaseReference.child("Usuario").child(user.getId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(RegistryActivity.this, "Usuario registrado\nVerifique su correo para poder iniciar sesion", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(RegistryActivity.this, "Error: No se pudo ingresar los datos del usuario", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegistryActivity.this, "Error: No se pudo crear el usuario", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void regresarLogin(View view) {
        finish();
    }
}