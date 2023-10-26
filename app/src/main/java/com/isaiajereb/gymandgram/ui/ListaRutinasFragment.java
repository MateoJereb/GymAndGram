package com.isaiajereb.gymandgram.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.databinding.FragmentListaRutinasBinding;
import com.isaiajereb.gymandgram.model.Rutina;
import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.recycler_views.RutinasAdapter;
import com.isaiajereb.gymandgram.repo.RutinasRepository;
import com.isaiajereb.gymandgram.viewmodel.RutinasViewModel;
import com.isaiajereb.gymandgram.viewmodel.RutinasViewModelFactory;
import com.isaiajereb.gymandgram.viewmodel.UsuarioViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ListaRutinasFragment extends Fragment {

    private RutinasViewModel viewModel;
    private FragmentListaRutinasBinding binding;
    private NavController navController;

    private RecyclerView rvRutinas;
    private RutinasAdapter adapter;

    private Usuario usuario;

    public ListaRutinasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        usuario = new ViewModelProvider(requireActivity()).get(UsuarioViewModel.class).getUsuario();
        viewModel = new ViewModelProvider(requireActivity(), new RutinasViewModelFactory(requireActivity().getApplicationContext(),usuario)).get(RutinasViewModel.class);
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
        //Setear el icono de la barra de navegacion
        ((MainActivity) requireActivity()).getNavigationBar().setSelectedItemId(R.id.workout_navigation);

        rvRutinas = binding.rutinasRecyclerView;
        rvRutinas.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        rvRutinas.setLayoutManager(layoutManager);

        adapter = new RutinasAdapter(new ArrayList<>(),requireActivity());
        rvRutinas.setAdapter(adapter);

        //Cambiar la lista del adapter cuando se postee un valor en el live data
        viewModel.getRutinas().observe(requireActivity(), new Observer<List<Rutina>>() {
            @Override
            public void onChanged(List<Rutina> rutinas) {
                Collections.sort(rutinas, Comparator.comparing(Rutina::getFechaCreacion));

                adapter.setDataRutinas(rutinas);
                rvRutinas.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        adapter.setOnItemClickListener(new RutinasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Rutina rutina) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("rutina",rutina);
                navController.navigate(R.id.action_listaRutinasFragment_to_editarRutinaFragment,bundle);
            }
        });

        adapter.setOnItemLongClickListener(new RutinasAdapter.OnItemLongClickListener() {
            @Override
            public void onEditar(Rutina rutina) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("rutina",rutina);
                navController.navigate(R.id.action_listaRutinasFragment_to_editarRutinaFragment,bundle);
            }

            @Override
            public void onEliminar(Rutina rutina) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setMessage("¿Desea eliminar '"+rutina.getNombre()+"'?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO listener eliminar
                            }
                        })
                        .setNegativeButton("Cancelar", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        binding.nuevaRuttinaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onNuevaRutina(); }
        });
    }

    private void onNuevaRutina(){
        navController.navigate(R.id.action_listaRutinasFragment_to_editarRutinaFragment);
    }
}