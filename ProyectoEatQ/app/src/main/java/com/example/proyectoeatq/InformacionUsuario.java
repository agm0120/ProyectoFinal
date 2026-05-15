package com.example.proyectoeatq;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InformacionUsuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformacionUsuario extends Fragment {

    private EditText et_nombre, et_correo, et_contra, et_fecha, et_edad, et_nacion;
    private Button bt_guardar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public InformacionUsuario() {
        // Required empty public constructor
    }

    public static InformacionUsuario newInstance(String param1, String param2) {
        InformacionUsuario fragment = new InformacionUsuario();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_usuario, container, false);
        // Inicializar vistas
        et_nombre = view.findViewById(R.id.etNombre);
        et_correo = view.findViewById(R.id.et_email);
        et_contra = view.findViewById(R.id.et_passwordRegistro);
        et_fecha = view.findViewById(R.id.etFecha);
        et_edad = view.findViewById(R.id.etEdad);
        et_nacion = view.findViewById(R.id.etPais);
        bt_guardar = view.findViewById(R.id.btn_guardarEdicion);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser usuario = mAuth.getCurrentUser();

        cargarDatosUsuario();

        bt_guardar.setOnClickListener(v -> actualizarDatosUsuario());

        return view;
    }

    private void cargarDatosUsuario() {

        FirebaseUser usuario = mAuth.getCurrentUser();

        if (usuario == null) {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = usuario.getUid();
        android.util.Log.d("DEBUG_INFO", "Buscando documento con ID: " + uid);

        db.collection("Usuario")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        String nombre = String.valueOf(documentSnapshot.get("nombreUsuario"));
                        String correo = String.valueOf(documentSnapshot.get("correo"));
                        String fecha = String.valueOf(documentSnapshot.get("fechaNacimiento"));
                        String edad = String.valueOf(documentSnapshot.get("edad"));
                        String nacionalidad = String.valueOf(documentSnapshot.get("nacionalidad"));
                        String contrasena = String.valueOf(documentSnapshot.get("contraseña"));

                        et_nombre.setText(!nombre.equals("null") ? nombre : "");
                        et_correo.setText(!correo.equals("null") ? correo : "");
                        et_contra.setText(!contrasena.equals("null") ? contrasena : "");
                        et_fecha.setText(!fecha.equals("null") ? fecha : "");
                        et_edad.setText(!edad.equals("null") ? edad : "");
                        et_nacion.setText(!nacionalidad.equals("null") ? nacionalidad : "");

                    } else {
                        Toast.makeText(getContext(), "No existen datos del usuario", Toast.LENGTH_SHORT).show();
                    }

                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(),
                                "Error al cargar datos",
                                Toast.LENGTH_SHORT).show());
    }

        private void actualizarDatosUsuario() {
            FirebaseUser usuario = mAuth.getCurrentUser();
            if (usuario == null) return;

            String uid = usuario.getUid();

            // 1. Obtenemos los valores actuales de los EditText
            String nuevoNombre = et_nombre.getText().toString().trim();
            String nuevoCorreo = et_correo.getText().toString().trim();
            String nuevaContra = et_contra.getText().toString().trim();
            String nuevaFecha = et_fecha.getText().toString().trim();
            String nuevaEdad = et_edad.getText().toString().trim();
            String nuevoPais = et_nacion.getText().toString().trim();

            // 2. Validación básica (opcional pero recomendada)
            if (TextUtils.isEmpty(nuevoNombre) || TextUtils.isEmpty(nuevoCorreo)) {
                Toast.makeText(getContext(), "Nombre y correo son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // 3. Creamos un Map con los nombres de los campos de la base datos
            java.util.Map<String, Object> datosActualizados = new java.util.HashMap<>();
            datosActualizados.put("nombreUsuario", nuevoNombre);
            datosActualizados.put("correo", nuevoCorreo);
            datosActualizados.put("contraseña", nuevaContra);
            datosActualizados.put("fechaNacimiento", nuevaFecha);
            datosActualizados.put("edad", nuevaEdad);
            datosActualizados.put("nacionalidad", nuevoPais);

            // 4. Enviamos la actualización a Firestore
            db.collection("Usuario").document(uid)
                    .update(datosActualizados)
                    .addOnSuccessListener(aVoid -> {

                        // 5. Si Firestore se actualizó bien, procedemos con Auth
                        if (!TextUtils.isEmpty(nuevaContra)) {
                            usuario.updatePassword(nuevaContra)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Perfil y contraseña de acceso actualizados", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Esto suele fallar si la sesión es antigua o la contraseña es muy corta (< 6 caracteres)
                                            android.util.Log.e("AUTH_ERROR", "Error: " + task.getException().getMessage());
                                            Toast.makeText(getContext(), "Perfil guardado, pero error al cambiar contraseña de acceso", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                        }

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error al guardar en base de datos", Toast.LENGTH_SHORT).show();
                    });
        }
}