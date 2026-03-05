package com.example.proyectoeatq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.proyectoeatq.R;

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
