package com.example.tarea4;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity {

    private TextView tvResumenSeleccion, tvResumenUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        tvResumenSeleccion = findViewById(R.id.tvResumenSeleccion);
        tvResumenUsuario = findViewById(R.id.tvResumenUsuario);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String selecciones = extras.getString("selecciones", "Sin selección");
            String usuario = extras.getString("usuario", "N/A");
            String materia = extras.getString("materia", "N/A");

            tvResumenSeleccion.setText(selecciones);
            tvResumenUsuario.setText("Usuario: " + usuario + "\nMateria: " + materia);
        }
    }
}