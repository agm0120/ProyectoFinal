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

//Imports para crear un bitmap de la foto tomada por el usuario
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

// Imports para la IA
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

// Necesario para identificar la version de sistema y saber que metodo usar
import androidx.core.content.ContextCompat;

public class SubirPlato extends Fragment {

    private ImageButton imgButtonPlato;
    private Button buttonSubir;
    private String rutaFotoActual; // Guardará la ruta del archivo físico
    private ActivityResultLauncher<Uri> cameraLauncher;
    private GenerativeModelFutures model;
    private String textoIA;

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

        String apiKey = BuildConfig.GEMINI_API_KEY;

        // Se inicializa el modelo de la IA
        GenerativeModel gm = new GenerativeModel("models/gemini-2.5-flash", apiKey);
        model = GenerativeModelFutures.from(gm);
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
                procesarImagenConIA();
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

    private void abrirSiguienteFragment(String infoIA) {
        Fragment nuevoFragment = new ResultadoPlato();

        // Pasamos la respuesta de la IA al siguiente fragmento
        Bundle args = new Bundle();
        args.putString("info_ia", infoIA);
        nuevoFragment.setArguments(args);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, nuevoFragment)
                .addToBackStack(null)
                .commit();
    }

    //Metodo para consultar a la IA pasandole la imagen de la camara
    private void procesarImagenConIA() {
        if (rutaFotoActual == null) {
            Toast.makeText(getContext(), "Primero toma una foto", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Convertir la ruta del archivo a Bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(rutaFotoActual);

        // 2. Crear el prompt para la IA
        Content content = new Content.Builder()
                .addText("Dime si esto es un plato de comida.")
                .addImage(bitmap)
                .build();

        // 3. Llamada asíncrona
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                textoIA = result.getText();

                // IMPORTANTE: Volver al hilo principal para tocar la UI o navegar
                getActivity().runOnUiThread(() -> {
                    Log.d("GEMINI_OK", textoIA);
                    // Aquí puedes pasar el texto al siguiente fragment
                    abrirSiguienteFragment(textoIA);
                });
            }

            @Override
            public void onFailure(Throwable t) {
                getActivity().runOnUiThread(() -> {
                    Log.e("GEMINI_ERROR", t.getMessage());
                    Toast.makeText(getContext(), "Error en la IA: " + t.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        // Linea necesaria para detectar version del sistema y saber que metodo usar automaticamente
        }, androidx.core.content.ContextCompat.getMainExecutor(getContext()));
    }
}