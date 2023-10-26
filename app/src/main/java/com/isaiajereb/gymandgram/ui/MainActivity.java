package com.isaiajereb.gymandgram.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.databinding.ActivityMainBinding;
import com.isaiajereb.gymandgram.databinding.DrawerHeaderBinding;
import com.isaiajereb.gymandgram.model.Genero;
import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.viewmodel.UsuarioViewModel;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private int CODIGO_EDITAR_USUARIO = 1;
    private ActivityMainBinding binding;
    private DrawerHeaderBinding drawerHeaderBinding;
    private UsuarioViewModel usuarioViewModel;
    private Usuario usuario;
    private TextView nombreUsuarioDrawer;
    private TextView correoUsuarioDrawer;

    private CircleImageView fotoPerfilDrawer;
    public BottomNavigationView getNavigationBar(){
        return binding.bottomNavigationView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Instanciar el ViewModel de usuario para traer el usuario a memoria
         * De no existir la BD, la instancia y realiza una consulta para crear un usuario*/
        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        usuario = usuarioViewModel.getUsuario();
        View view = binding.getRoot();
        setContentView(view);

        //Setear la Toolbar
        Toolbar toolbar = binding.materialToolbar;
        setSupportActionBar(toolbar);

        //Configurar barra de navegacion
        binding.bottomNavigationView.setSelectedItemId(R.id.home_navigation);
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragmentoActual = binding.fragmentConainterView.getFragment();
                NavController navController = NavHostFragment.findNavController(fragmentoActual);
                Integer currentId = navController.getCurrentDestination().getId();

                switch (item.getItemId()){
                    case R.id.home_navigation:
                        while(navController.navigateUp()){}
                        return true;

                    case R.id.workout_navigation:
                        if(currentId == R.id.inicioFragment){
                            navController.navigate(R.id.action_inicioFragment_to_listaRutinasFragment);
                        }

                        return true;

                    case R.id.social_navigation:
                        Toast.makeText(MainActivity.this, "Proximamente...", Toast.LENGTH_SHORT).show();
                        return false;
                }
                return false;
            }
        });

        //Setea el drawer.
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.drawerNavView;

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.app_name, R.string.app_name );
        drawer.addDrawerListener(toogle);
        toogle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragmentoActual = binding.fragmentConainterView.getFragment();
                NavController navController = NavHostFragment.findNavController(fragmentoActual);

                switch (item.getItemId()) {
                    case R.id.drawer_perfil:
                        usuario = usuarioViewModel.getUsuario();
                        try {
                            Bitmap fotoPerfil = usuario.getFotoPerfil();
                            String filename = "fotoperfil.png";
                            FileOutputStream stream = openFileOutput(filename, Context.MODE_PRIVATE);
                            fotoPerfil.compress(Bitmap.CompressFormat.PNG, 100, stream);

                            stream.close();
//                            fotoPerfil.recycle();

                            Intent intent = new Intent(getApplicationContext(), ConfigurarPerfilActivity.class);
                            intent.putExtra("fotoPerfil", filename);
                            intent.putExtra("usuarioNombre", usuario.getNombre());
                            intent.putExtra("usuarioMail", usuario.getMail());
                            intent.putExtra("usuarioGenero", usuario.getGenero().name());
                            intent.putExtra("usuarioEdad", usuario.getEdad());
                            startActivityForResult(intent, CODIGO_EDITAR_USUARIO);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    case R.id.drawer_recordatorios:
                        Toast.makeText(MainActivity.this, "Proximamente...", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_ayuda:
                        Toast.makeText(MainActivity.this, "Proximamente...", Toast.LENGTH_SHORT).show();                        Toast.makeText(MainActivity.this, "Proximamente...", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_ajustes:
                        return true;
                    case R.id.drawer_cerrar_sesion:
                        return true;
                }
                return false;
            }
        });

        nombreUsuarioDrawer = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_nombre_usuario);
        correoUsuarioDrawer = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_correo_usuario);
        fotoPerfilDrawer = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.drawer_profile_image);

        nombreUsuarioDrawer.setText(usuario.getNombre());
        correoUsuarioDrawer.setText(usuario.getMail());
        fotoPerfilDrawer.setImageBitmap(usuario.getFotoPerfil());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment fragmentoActual = binding.fragmentConainterView.getFragment();
        NavController navController = NavHostFragment.findNavController(fragmentoActual);
        Integer currentId = navController.getCurrentDestination().getId();

        if(currentId == R.id.inicioFragment)
            binding.bottomNavigationView.setSelectedItemId(R.id.home_navigation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == CODIGO_EDITAR_USUARIO){
            Log.i("usuario",data.getStringExtra("usuarioNombre")+data.getStringExtra("usuarioMail") + data.getStringExtra("usuarioGenero") + data.getIntExtra("usuarioEdad", 0));
            String filename = data.getStringExtra("fotoPerfil");
            Bitmap nuevaFotoPerfil=null;
            try {
                FileInputStream is = this.openFileInput(filename);
                nuevaFotoPerfil = BitmapFactory.decodeStream(is);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            usuario.setFotoPerfil(nuevaFotoPerfil);
            usuario.setNombre(data.getStringExtra("usuarioNombre"));
            usuario.setMail(data.getStringExtra("usuarioMail"));
            usuario.setGenero(Genero.valueOf(data.getStringExtra("usuarioGenero")));
            usuario.setEdad(data.getIntExtra("usuarioEdad", 0));
            usuarioViewModel.updateUsuario(usuario);

            nombreUsuarioDrawer.setText(usuario.getNombre());
            correoUsuarioDrawer.setText(usuario.getMail());
            fotoPerfilDrawer.setImageBitmap(usuario.getFotoPerfil());
        }

    }

}