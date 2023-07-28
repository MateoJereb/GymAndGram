package com.isaiajereb.gymandgram.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.contract.ActivityResultContracts.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.databinding.ActivityConfigurarPerfilBinding;
import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.model.Genero;
import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.viewmodel.UsuarioViewModel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ConfigurarPerfilActivity extends AppCompatActivity {

    private ActivityConfigurarPerfilBinding binding;
    private Bitmap fotoPerfil;
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
            String filename = getIntent().getStringExtra("fotoPerfil");
            try {
                FileInputStream is = this.openFileInput(filename);
                fotoPerfil = BitmapFactory.decodeStream(is);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        binding.profileImage.setImageBitmap(fotoPerfil);
        //setear listeners de los campos.
        setFieldsListeners();

        binding.guardarPerfilBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDialogoConfirmarGuardado();
            }
        });

        ActivityResultLauncher<PickVisualMediaRequest> pickPhoto =
                registerForActivityResult(new PickVisualMedia(), uri -> {
                    if(uri != null ){
                        try {
                            fotoPerfil = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                            binding.profileImage.setImageBitmap(fotoPerfil);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        binding.cambiarFotoPerfil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                pickPhoto.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
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
                try {
                    String filename = "fotoperfil.png";
                    FileOutputStream stream = openFileOutput(filename, Context.MODE_PRIVATE);
                    fotoPerfil.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    stream.close();
                    fotoPerfil.recycle();


                    Intent cambios = new Intent();
                    cambios.putExtra("fotoPerfil", filename);
                    cambios.putExtra("usuarioNombre", binding.nombreET.getText().toString());
                    cambios.putExtra("usuarioMail", binding.emailET.getText().toString());
                    cambios.putExtra("usuarioGenero", binding.generoSpinner.getSelectedItem().toString());
                    cambios.putExtra("usuarioEdad", Integer.parseInt(binding.edadET.getText().toString()));
                    setResult(Activity.RESULT_OK, cambios);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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