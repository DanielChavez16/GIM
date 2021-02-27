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

    EditText txt_email, txt_pass;
    String email, pass;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_email = findViewById(R.id.login_txt_email);
        txt_pass = findViewById(R.id.login_txt_pass);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void redirRegistro(View view) {
        Intent registro = new Intent(this, RegistryActivity.class);
        startActivity(registro);
        txt_email.setText("");
        txt_pass.setText("");
    }

    public void redirMenu(View view) {
        email = txt_email.getText().toString();
        pass = txt_pass.getText().toString();

        if(validarEmail(email) && !pass.equals("")) {
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "No existe usuario", Toast.LENGTH_LONG).show();
                    } else {
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        if (user != null) {
                            if (!user.isEmailVerified()) {
                                Toast.makeText(MainActivity.this, "El correo electronico no esta verificado", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, "El usuario inicio sesión correctamente", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "No se pudo iniciar sesión", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
        else {
            if(!email.equals("") && !validarEmail(email)) {
                Toast.makeText(this, "Correo no valido", Toast.LENGTH_SHORT).show();
            }
            if(email.equals("") || pass.equals("")) {
                Toast.makeText(this, "Error: Llena todos los campos", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}