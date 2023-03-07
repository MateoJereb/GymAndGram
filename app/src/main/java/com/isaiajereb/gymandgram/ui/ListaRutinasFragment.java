package com.isaiajereb.gymandgram.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.databinding.FragmentListaRutinasBinding;
import com.isaiajereb.gymandgram.viewmodel.RutinasViewModel;

public class ListaRutinasFragment extends Fragment {

    private FragmentListaRutinasBinding binding;
    private NavController navController;
    private RutinasViewModel viewModel;

    public ListaRutinasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        viewModel = new ViewModelProvider(requireActivity()).get(RutinasViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListaRutinasBinding.inflate(inflater,container,false);
        navController = NavHostFragment.findNavController(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Setear el icono de la barra de navegacion
        ((MainActivity) requireActivity()).getNavigationBar().setSelectedItemId(R.id.workout_navigation);
    }
}