package com.isaiajereb.gymandgram.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.databinding.ActivityConfigurarPerfilBinding;
import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.model.Genero;
import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.viewmodel.UsuarioViewModel;

import java.util.Arrays;

public class ConfigurarPerfilActivity extends AppCompatActivity {

    private ActivityConfigurarPerfilBinding binding;
    private String nombreUsuario;
    private String mailUsuario;
    private Genero generoUsuario;
    private Integer edadUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityConfigurarPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            nombreUsuario = extras.getString("usuarioNombre");
            mailUsuario = extras.getString("usuarioMail");
            generoUsuario = Genero.valueOf(extras.getString("usuarioGenero"));
            edadUsuario = extras.getInt("usuarioEdad");
        }

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

        binding.nombreET.setText(nombreUsuario);
        binding.emailET.setText(mailUsuario);
        for (int i =0; i< generos.length; i++){
            if (generos[i] == generoUsuario.name()){
                binding.generoSpinner.setSelection(i);
                break;
            }
        }
        binding.edadET.setText(edadUsuario.toString());

        //setear listeners de los campos.
        setFieldsListeners();

        binding.guardarPerfilBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDialogoConfirmarGuardado();
            }
        });

    }

    private void abrirDialogoConfirmarGuardado() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle("Guardar Cambios");
        dialogBuilder.setMessage("¿Estás seguro que quieres guardar los cambios?");
        dialogBuilder.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                Intent cambios = new Intent();
                cambios.putExtra("usuarioNombre", binding.nombreET.getText().toString());
                cambios.putExtra("usuarioMail", binding.emailET.getText().toString());
                cambios.putExtra("usuarioGenero", binding.generoSpinner.getSelectedItem().toString());
                cambios.putExtra("usuarioEdad", Integer.parseInt(binding.edadET.getText().toString()));
                setResult(Activity.RESULT_OK, cambios);
                finish();
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void setFieldsListeners() {
        binding.nombreET.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            if(editable.toString().isEmpty()){
                binding.guardarPerfilBoton.setEnabled(false);
                binding.nombreET.setBackgroundColor(getColor(R.color.red_pastel));
                Log.e("gym&gram", "error empty name");
            }else{
                binding.nombreET.setBackgroundColor(Color.TRANSPARENT);
                binding.guardarPerfilBoton.setEnabled(true);

            }
            }
        });
        binding.emailET.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().isEmpty()){
                    binding.guardarPerfilBoton.setEnabled(false);
                    binding.emailET.setBackgroundColor(getColor(R.color.red_pastel));
                    Log.e("gym&gram", "error empty mail");
                }else{
                    binding.emailET.setBackgroundColor(Color.TRANSPARENT);
                    binding.guardarPerfilBoton.setEnabled(true);

                }
            }
        });
        binding.edadET.addTextChangedListener(new TextWatcher(){

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable.toString().isEmpty()){
                        binding.guardarPerfilBoton.setEnabled(false);
                        binding.edadET.setBackgroundColor(getColor(R.color.red_pastel));
                        Log.e("gym&gram", "error empty mail");
                    }else{
                        binding.edadET.setBackgroundColor(Color.TRANSPARENT);
                        binding.guardarPerfilBoton.setEnabled(true);

                    }
                }
            });
        }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}