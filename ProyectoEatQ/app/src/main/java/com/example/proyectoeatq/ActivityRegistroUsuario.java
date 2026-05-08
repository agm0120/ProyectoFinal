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
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;


public class ActivityRegistroUsuario extends AppCompatActivity {

    private EditText correo, contraseña, nombre, fecha, edad, nacion;

    private Button registroUsuario;

    private String textoCorreo, textoContra, textoNombre, textoFecha, textoEdad, textoNacion;

    private FirebaseAuth auth;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        correo = findViewById(R.id.et_email);
        contraseña =findViewById(R.id.et_passwordRegistro);
        nombre = findViewById(R.id.et_nombreUsuario);
        fecha = findViewById(R.id.et_fechaNacimiento);
        edad = findViewById(R.id.et_edad);
        nacion = findViewById(R.id.et_nacionalidad);
        registroUsuario = findViewById(R.id.btn_crearUsuario);

        auth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        registroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textoCorreo = correo.getText().toString();
                textoContra = contraseña.getText().toString();
                textoNombre = nombre.getText().toString();
                textoFecha = fecha.getText().toString();
                textoEdad = edad.getText().toString();
                textoNacion = nacion.getText().toString();



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
        auth.createUserWithEmailAndPassword(textoCorreo, textoContra)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 1. Obtenemos el UID del usuario recién creado
                            String uid = auth.getCurrentUser().getUid();

                            // 2. Llamamos a la base de datos pasando el UID
                            registerdb(uid, textoCorreo, textoContra, textoNombre, textoFecha, textoEdad, textoNacion);

                            // Se mueve el Intent al éxito de la escritura en DB para asegurar que todo se guardó
                        } else {
                            Toast.makeText(ActivityRegistroUsuario.this, "Fallo en el registro de Auth",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void registerdb(String uid, String textoCorreo, String textoContra, String textoNombre,
                            String textoFecha, String textoEdad, String textoNacion){

        Map<String, Object> userMap = new HashMap<>();

        userMap.put("correo", textoCorreo);
        userMap.put("contraseña", textoContra);
        userMap.put("nombreUsuario", textoNombre);
        userMap.put("fechaNacimiento", textoFecha);
        userMap.put("edad", textoEdad);
        userMap.put("nacionalidad", textoNacion);

        // Usamos el UID como nombre del documento
        db.collection("Usuario").document(uid).set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ActivityRegistroUsuario.this, "Usuario registrado con éxito",
                            Toast.LENGTH_SHORT).show();
                    // Redirigir al Login solo DESPUÉS de que la base de datos confirme la escritura
                    startActivity(new Intent(ActivityRegistroUsuario.this, ActivityMainLogin.class));
                    finish(); // Cerramos la actividad de registro para que no puedan volver atrás
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ActivityRegistroUsuario.this, "Error al guardar datos: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    //Metodo para comprobar que los campos (por ahora correo y contraseña) no estan vacios o son nulos
    private boolean checkEmpty(String textoCorreo, String textoContra) {
        return !textoCorreo.isEmpty() && textoCorreo != null && !textoContra.isEmpty() && textoContra != null;
    }
}