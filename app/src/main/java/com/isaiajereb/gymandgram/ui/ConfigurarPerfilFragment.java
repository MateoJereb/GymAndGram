package com.isaiajereb.gymandgram.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.databinding.FragmentConfigurarPerfilBinding;


public class ConfigurarPerfilFragment extends Fragment {

    private FragmentConfigurarPerfilBinding binding;
    private NavController navController;

    public ConfigurarPerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConfigurarPerfilBinding.inflate(inflater, container, false);
        navController = NavHostFragment.findNavController(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llenarSpinner();
    }

    private void llenarSpinner() {
        String[] generos = getResources().getStringArray(R.array.generos);
        ArrayAdapter<String> adapterGenero = new ArrayAdapter<>(binding.getRoot().getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                generos);
        binding.generoSpinner.setAdapter(adapterGenero);
    }
}