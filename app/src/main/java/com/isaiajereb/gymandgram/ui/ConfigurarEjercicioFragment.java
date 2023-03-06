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
import com.isaiajereb.gymandgram.databinding.FragmentConfigurarEjercicioBinding;

public class ConfigurarEjercicioFragment extends Fragment {

    private FragmentConfigurarEjercicioBinding binding;
    private NavController navController;

    public ConfigurarEjercicioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentConfigurarEjercicioBinding.inflate(inflater,container,false);
        navController = NavHostFragment.findNavController(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llenarSpiner();

    }

    private void llenarSpiner(){
        String[] unidadesTiempo = getResources().getStringArray(R.array.unidad_tiempo);
        ArrayAdapter<String> adapterUnidadTiempo = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, unidadesTiempo);
        binding.unidadTiempoSpinner.setAdapter(adapterUnidadTiempo);
    }
}