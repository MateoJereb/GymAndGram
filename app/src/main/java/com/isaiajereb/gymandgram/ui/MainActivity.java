package com.isaiajereb.gymandgram.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.isaiajereb.gymandgram.persistencia.room.AppDatabase;
import com.isaiajereb.gymandgram.viewmodel.UsuarioViewModel;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    public BottomNavigationView getNavigationBar(){
        return binding.bottomNavigationView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        /*Instanciar el ViewModel de usuario para traer el usuario a memoria
        * De no existir la BD, la instancia y realiza una consulta para crear un usuario con UUID, pero datos nulos*/
        new ViewModelProvider(this).get(UsuarioViewModel.class);

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
                        Intent intent = new Intent(getApplicationContext(), ConfigurarPerfilActivity.class);
                        startActivity(intent);
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
}