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
import android.widget.TextView;
import android.widget.Toast;

// Para poder abrir la galeria del telefono
import androidx.activity.result.PickVisualMediaRequest;

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
    private Button buttonSubir, buttonVolverSubir;
    private String rutaFotoActual; // Guardará la ruta del archivo físico
    private ActivityResultLauncher<Uri> cameraLauncher; // Lanzador de la camara
    private ActivityResultLauncher<PickVisualMediaRequest> galleryLauncher; // Lanzador de la galeria
    private GenerativeModelFutures model;
    private String textoIA;
    private TextView textoOrientativo;

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
                        // Hacer visible boton "Volver a Subir"
                        buttonVolverSubir.setVisibility(View.VISIBLE);
                        textoOrientativo.setText(R.string.plato_de_hoy);
                    }
                });

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        // Guardamos la URI de la galería para que el botón "Subir" pueda leerla
                        rutaFotoActual = uri.toString();
                        imgButtonPlato.setImageURI(uri);
                        // Hacer visible boton "Volver a Subir"
                        buttonVolverSubir.setVisibility(View.VISIBLE);
                        textoOrientativo.setText(R.string.plato_de_hoy);
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
        buttonVolverSubir = view.findViewById(R.id.btn_resubirPlato);
        imgButtonPlato = view.findViewById(R.id.imb_subirPlato);
        textoOrientativo = view.findViewById(R.id.tv_pregunta_plato);

        // Ocultar boton "Volver a Subir"
        buttonVolverSubir.setVisibility(View.GONE);

        imgButtonPlato.setOnClickListener(v -> mostrarOpcionesImagen());

        buttonSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procesarImagenConIA();
            }
        });

        // Funcion del boton "Volver a subir"
        buttonVolverSubir.setOnClickListener(v -> mostrarOpcionesImagen());
        return view;
    }

    private void mostrarOpcionesImagen() {
        // Puedes usar un AlertDialog sencillo para preguntar
        String[] opciones = {"Cámara", "Galería"};
        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Seleccionar imagen")
                .setItems(opciones, (dialog, which) -> {
                    if (which == 0) abrirCamara();
                    else abrirGaleria();
                })
                .show();
    }

    private void abrirGaleria() {
        galleryLauncher.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
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
            Toast.makeText(getContext(), "Primero selecciona una foto", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Bitmap bitmap;
            // Si la ruta empieza por "content://", viene de la galería
            if (rutaFotoActual.startsWith("content://")) {
                Uri uri = Uri.parse(rutaFotoActual);
                bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri));
            } else {
                // Si no, es una ruta de archivo (Cámara)
                bitmap = BitmapFactory.decodeFile(rutaFotoActual);
            }

            String promptIA = "Actúa como un nutricionista experto. Analiza la imagen según el Método del Plato " +
                    "(50% vegetales, 25% proteína, 25% carbohidratos). " +
                    "Responde ÚNICAMENTE en formato JSON con los siguientes campos: " +
                    "es_comida (boolean), " +
                    "analisis_detallado (string muy breve y sencillo), " +
                    "porcentajes (objeto con: vegetales, proteinas, carbohidratos como enteros), " +
                    "puntuacion (int de 1 a 10), " +
                    "sugerencia (string para mejorar el plato). " +
                    "No añadas formato markdown ni texto extra.";

            Content content = new Content.Builder()
                    .addText(promptIA)
                    .addImage(bitmap)
                    .build();

            ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    textoIA = result.getText();
                    getActivity().runOnUiThread(() -> abrirSiguienteFragment(textoIA));
                }

                @Override
                public void onFailure(Throwable t) {
                    getActivity().runOnUiThread(() -> {
                        Log.e("GEMINI_ERROR", t.getMessage());
                        Toast.makeText(getContext(), "Error: Error de conexión con la API", Toast.LENGTH_LONG).show();
                    });
                }
            }, ContextCompat.getMainExecutor(getContext()));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
        }
    }
}