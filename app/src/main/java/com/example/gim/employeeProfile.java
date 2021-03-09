package com.example.gim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class employeeProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);
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
        Toast.makeText(this, "Cambiar la contraseña del usuario", Toast.LENGTH_SHORT).show();
    }

    public void salir(View view) {
        finish();
        employeeMenu.eM.finish();
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main);
    }
}