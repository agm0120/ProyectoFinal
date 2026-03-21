package com.example.proyectoeatq;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SubirPlato extends Fragment {

    private ImageButton imgButtonPlato;
    private Button buttonSubir;
    private String rutaFotoActual; // Guardará la ruta del archivo físico
    private ActivityResultLauncher<Uri> cameraLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Registramos el lanzador que ahora espera un URI (la ubicación donde se guardará)
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result) {
                        // Si 'result' es true, la foto se guardó con éxito en alta resolución
                        imgButtonPlato.setImageURI(Uri.parse(rutaFotoActual));
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plato, container, false);
        buttonSubir = view.findViewById(R.id.btn_subirPlato);
        imgButtonPlato = view.findViewById(R.id.imb_subirPlato);

        imgButtonPlato.setOnClickListener(v -> abrirCamara());

        buttonSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirSiguienteFragment();
            }
        });
        return view;
    }

    private void abrirCamara() {
        File imagenArchivo = null;
        try {
            imagenArchivo = crearArchivoImagen();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (imagenArchivo != null) {
            Uri fotoUri = FileProvider.getUriForFile(getContext(),
                    "com.example.proyectoeatq.fileprovider",
                    imagenArchivo);

            cameraLauncher.launch(fotoUri);
        }
    }

    private File crearArchivoImagen() throws IOException {
        // Creamos un nombre único basado en la fecha
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String nombreImagen = "JPEG_" + timeStamp + "_";
        File directorio = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imagen = File.createTempFile(nombreImagen, ".jpg", directorio);
        rutaFotoActual = imagen.getAbsolutePath(); // Guardamos la ruta para mostrarla luego
        return imagen;
    }

    private void abrirSiguienteFragment() {
        // Reemplaza 'OtroFragment' por el nombre real de tu clase destino
        Fragment nuevoFragment = new ResultadoPlato();

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, nuevoFragment) // R.id.container es el ID del FrameLayout en tu Activity principal
                .addToBackStack(null) // Permite volver atrás
                .commit();
    }
}