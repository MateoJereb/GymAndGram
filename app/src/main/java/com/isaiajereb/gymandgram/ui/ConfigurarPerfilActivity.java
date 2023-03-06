package com.isaiajereb.gymandgram.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.databinding.ActivityConfigurarPerfilBinding;

public class ConfigurarPerfilActivity extends AppCompatActivity {

    private ActivityConfigurarPerfilBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityConfigurarPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Setear la Toolbar
        Toolbar toolbar = binding.materialToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setear el spinner de Géneros
        String[] generos = getResources().getStringArray(R.array.generos);
        ArrayAdapter<String> adapterGenero = new ArrayAdapter<>(binding.getRoot().getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                generos);
        binding.generoSpinner.setAdapter(adapterGenero);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}