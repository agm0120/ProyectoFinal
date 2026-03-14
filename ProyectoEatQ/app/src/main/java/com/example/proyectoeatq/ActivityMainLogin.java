package com.example.proyectoeatq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/* Usuario creado como ejemplo (para hacer comprobacion):
Correo: ejemplo@ejemplo.com
Contraseña: 123456
 */

public class ActivityMainLogin extends AppCompatActivity {

    private EditText usuario, password;
    private Button btn_login, btn_registrarse;
    private FirebaseAuth auth;
    private String textoUsuario, textoPassword;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        usuario = findViewById(R.id.et_usuario);
        password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_registrarse = findViewById(R.id.btn_registrarse);
        auth = FirebaseAuth.getInstance();


        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                textoUsuario = usuario.getText().toString();
                textoPassword = password.getText().toString();


                if(checkEmpty(textoUsuario, textoPassword)){
                    login(textoUsuario, textoPassword);
                }
            }
        });


        btn_registrarse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityMainLogin.this, ActivityRegistroUsuario.class));
            }
        });


    }

    private void login(String textoUsuario, String textoPassword) {
        auth.signInWithEmailAndPassword(textoUsuario, textoPassword)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        startActivity(new Intent(ActivityMainLogin.this, ActivityMain.class));
                    }else{
                        Toast.makeText(ActivityMainLogin.this,
                                "Error en la autenticacion",
                                Toast.LENGTH_LONG).show();
                    }
                });

    }

    private boolean checkEmpty(String usuario, String password) {
        return !usuario.isEmpty() && !password.isEmpty();
    }



}
