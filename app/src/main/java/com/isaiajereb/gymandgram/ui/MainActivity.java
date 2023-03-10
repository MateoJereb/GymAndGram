package com.isaiajereb.gymandgram.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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