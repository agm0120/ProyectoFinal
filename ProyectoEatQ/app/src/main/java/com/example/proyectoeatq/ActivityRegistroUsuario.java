package com.example.proyectoeatq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityRegistroUsuario extends AppCompatActivity {

    private EditText correo;

    private EditText contraseña;

    private Button registroUsuario;

    private String textoCorreo;

    private String textoContra;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        correo = findViewById(R.id.etEmail);
        contraseña =findViewById(R.id.etPassword);
        registroUsuario = findViewById(R.id.btnCreateUser);

        auth = FirebaseAuth.getInstance();

        registroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textoCorreo = correo.getText().toString();
                textoContra = contraseña.getText().toString();

                //Se comprueba que los dos campos son validos. Mas adelante se incluiran el resto de datos de un usuario
                if(checkEmpty(textoCorreo, textoContra)){

                    register(textoCorreo, textoContra);

                }else {
                    Toast.makeText(ActivityRegistroUsuario.this, "Por favor, rellena todos los campos",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    //Metodo para registrar un nuevo usuario
    private void register(String textoCorreo, String textoContra) {
        //Metodo de auth para registrar nuevos usuarios. Se le pasa correo y contraseña(minimo seis caracteres)
        auth.createUserWithEmailAndPassword(textoCorreo, textoContra)
                //Metodo para realizar algo una vez se termine el registro
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        //Condicional que se ejecuta si el registro es correcto. Si lo es se vuelve a la pantalla de login
                        if (task.isSuccessful()) {
                            startActivity(new Intent(ActivityRegistroUsuario.this, ActivityMainLogin.class));
                        //Condicional que se ejecuta si falla el registro. Si falla salta un mensaje con el error
                        } else {
                            Toast.makeText(ActivityRegistroUsuario.this, "Fallo en el registro",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Metodo para comprobar que los campos (por ahora correo y contraseña) no estan vacios o son nulos
    private boolean checkEmpty(String textoCorreo, String textoContra) {
        return !textoCorreo.isEmpty() && textoCorreo != null && !textoContra.isEmpty() && textoContra != null;
    }
}