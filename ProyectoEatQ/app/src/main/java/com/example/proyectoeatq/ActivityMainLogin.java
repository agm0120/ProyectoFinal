package com.example.proyectoeatq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.proyectoeatq.R;

//El usuario que he creado de ejemplo es:
//Correo: ejemplo@ejemplo.com
//Contraseña: 123456
// Lo pongo aqui para que se pueda usar para comprobar si funciona el login

public class ActivityMainLogin extends AppCompatActivity {

    private Button bRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        bRegistro = findViewById(R.id.buttonRegistro);

        bRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pasarPantalla = new Intent(ActivityMainLogin.this, ActivityRegistroUsuario.class);

                startActivity(pasarPantalla);

            }
        });

    }
}
