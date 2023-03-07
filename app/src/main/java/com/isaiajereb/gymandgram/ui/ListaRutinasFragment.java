package com.isaiajereb.gymandgram.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.databinding.FragmentListaRutinasBinding;
import com.isaiajereb.gymandgram.model.Rutina;
import com.isaiajereb.gymandgram.recycler_views.RutinasAdapter;

import java.util.ArrayList;
import java.util.UUID;

public class ListaRutinasFragment extends Fragment {

    private FragmentListaRutinasBinding binding;
    private NavController navController;
    private RecyclerView rvRutinas;
    private RutinasAdapter adapter;

    public ListaRutinasFragment() {
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
        // Inflate the layout for this fragment
        binding = FragmentListaRutinasBinding.inflate(inflater, container, false);
        navController = NavHostFragment.findNavController(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).getNavigationBar().setSelectedItemId(R.id.workout_navigation);

        rvRutinas = binding.rutinasRecyclerView;
        rvRutinas.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        rvRutinas.setLayoutManager(layoutManager);

        ArrayList<Rutina> rutinas = new ArrayList<Rutina>();
        rutinas.add(new Rutina(UUID.randomUUID(),"Rutina1",true, UUID.randomUUID()));
        rutinas.add(new Rutina(UUID.randomUUID(),"Rutina2",false, UUID.randomUUID()));

        adapter = new RutinasAdapter(rutinas,requireActivity());
        rvRutinas.setAdapter(adapter);

        adapter.setOnItemClickListener(new RutinasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Rutina rutina) {
                //TODO listener
            }
        });
    }
}