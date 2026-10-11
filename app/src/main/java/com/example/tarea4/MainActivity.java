package com.example.tarea4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CheckBox cbMusica, cbCarro, cbCalle, cbPersona;
    private Button btnMostrarImagen, btnVerInfo, btnResumen, btnCompartir, btnCambiarFoto;
    private ImageView imgDinamica;

    private String nombreUsuario = "Mario Suero";
    private String nombreMateria = "Introduccion A Aplicaciones Moviles";
    private String nombreInstitucion = "Instituto Tecnológico De Las Americas";
    private Uri uriFotoPerfil = null;

    private static final int REQUEST_GALLERY = 101;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cbMusica = findViewById(R.id.cbMusica);
        cbCarro = findViewById(R.id.cbCarro);
        cbCalle = findViewById(R.id.cbCalle);
        cbPersona = findViewById(R.id.cbPersona);

        btnMostrarImagen = findViewById(R.id.btnMostrarImagen);
        btnVerInfo = findViewById(R.id.btnVerInfo);
        btnResumen = findViewById(R.id.btnResumen);
        btnCompartir = findViewById(R.id.btnCompartir);
        btnCambiarFoto = findViewById(R.id.btnCambiarFoto);
        imgDinamica = findViewById(R.id.imgDinamica);

        btnMostrarImagen.setOnClickListener(v -> actualizarImagenDinamica());

        btnVerInfo.setOnClickListener(v -> mostrarAlertDialogInfo());

        btnResumen.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("selecciones", obtenerTextoSelecciones());
            bundle.putString("usuario", nombreUsuario);
            bundle.putString("materia", nombreMateria);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        btnCompartir.setOnClickListener(v -> {
            String textoACompartir = obtenerTextoSelecciones() + "\nEnviado por: " + nombreUsuario;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, textoACompartir);
            startActivity(Intent.createChooser(shareIntent, "Compartir selección vía:"));
        });

        btnCambiarFoto.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_GALLERY);
        });

        gestureDetector = new GestureDetector(this, new SwipeGestureListener());
    }

    private void actualizarImagenDinamica() {
        boolean m = cbMusica.isChecked();
        boolean c = cbCarro.isChecked();
        boolean ca = cbCalle.isChecked();
        boolean p = cbPersona.isChecked();

        if (m && c && ca && p) {
            imgDinamica.setImageResource(R.drawable.img_todo);
        }
        else if (m && c && ca) {
            imgDinamica.setImageResource(R.drawable.img_musica_carro_calle);
        } else if (m && c && p) {
            imgDinamica.setImageResource(R.drawable.img_musica_carro_persona);
        } else if (m && ca && p) {
            imgDinamica.setImageResource(R.drawable.img_musica_calle_persona);
        } else if (c && ca && p) {
            imgDinamica.setImageResource(R.drawable.img_carro_calle_persona);
        }
        else if (m && c) {
            imgDinamica.setImageResource(R.drawable.img_carro_musica);
        } else if (m && ca) {
            imgDinamica.setImageResource(R.drawable.img_musica_calle);
        } else if (m && p) {
            imgDinamica.setImageResource(R.drawable.img_persona_musica);
        } else if (c && ca) {
            imgDinamica.setImageResource(R.drawable.img_carro_calle);
        } else if (c && p) {
            imgDinamica.setImageResource(R.drawable.img_carro_persona);
        } else if (ca && p) {
            imgDinamica.setImageResource(R.drawable.img_calle_persona);
        }
        else if (m) {
            imgDinamica.setImageResource(R.drawable.img_musica);
        } else if (c) {
            imgDinamica.setImageResource(R.drawable.img_carro);
        } else if (ca) {
            imgDinamica.setImageResource(R.drawable.img_calle);
        } else if (p) {
            imgDinamica.setImageResource(R.drawable.img_persona);
        }
        else {
            imgDinamica.setImageResource(R.drawable.ic_launcher_background);
            Toast.makeText(this, "Selecciona al menos una categoría", Toast.LENGTH_SHORT).show();
        }
    }

    private String obtenerTextoSelecciones() {
        StringBuilder builder = new StringBuilder("Mi selección: ");
        if (cbMusica.isChecked()) builder.append("Música, ");
        if (cbCarro.isChecked()) builder.append("Carro, ");
        if (cbCalle.isChecked()) builder.append("Calle, ");
        if (cbPersona.isChecked()) builder.append("Persona, ");

        String result = builder.toString();
        if (result.endsWith(", ")) {
            result = result.substring(0, result.length() - 2);
        }
        return result.equals("Mi selección: ") ? "Mi selección: Ninguna" : result;
    }

    private void mostrarAlertDialogInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_info, null);

        ImageView imgPerfil = dialogView.findViewById(R.id.imgDialogPerfil);
        TextView tvNombre = dialogView.findViewById(R.id.tvDialogNombre);
        TextView tvMateria = dialogView.findViewById(R.id.tvDialogMateria);
        TextView tvInst = dialogView.findViewById(R.id.tvDialogInstitucion);

        tvNombre.setText("Usuario: " + nombreUsuario);
        tvMateria.setText("Materia: " + nombreMateria);
        tvInst.setText("Institución: " + nombreInstitucion);

        if (uriFotoPerfil != null) {
            imgPerfil.setImageURI(uriFotoPerfil);
        } else {
            imgPerfil.setImageResource(R.drawable.ic_launcher_background);
        }

        builder.setView(dialogView);
        builder.setTitle("Información del Usuario");
        builder.setPositiveButton("Editar", (dialog, which) -> mostrarAlertDialogEdicion());
        builder.setNegativeButton("Cerrar", null);
        builder.create().show();
    }

    private void mostrarAlertDialogEdicion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Información");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 16, 32, 16);

        final EditText etNombre = new EditText(this);
        etNombre.setHint("Nombre");
        etNombre.setText(nombreUsuario);
        layout.addView(etNombre);

        final EditText etMateria = new EditText(this);
        etMateria.setHint("Materia");
        etMateria.setText(nombreMateria);
        layout.addView(etMateria);

        final EditText etInst = new EditText(this);
        etInst.setHint("Institución");
        etInst.setText(nombreInstitucion);
        layout.addView(etInst);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            nombreUsuario = etNombre.getText().toString();
            nombreMateria = etMateria.getText().toString();
            nombreInstitucion = etInst.getText().toString();
            Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null) {
            uriFotoPerfil = data.getData();
            Toast.makeText(this, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1 != null && e2 != null) {
                float diffX = e2.getX() - e1.getX();
                if (diffX < -SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY) {
                    Toast.makeText(MainActivity.this, "Cerrando aplicación...", Toast.LENGTH_SHORT).show();
                    finish();
                    return true;
                }
            }
            return false;
        }
    }
}