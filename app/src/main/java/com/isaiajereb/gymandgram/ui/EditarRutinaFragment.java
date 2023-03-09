package com.isaiajereb.gymandgram.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.databinding.FragmentEditarRutinaBinding;
import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.model.Rutina;
import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.recycler_views.EjerciciosAdapter;
import com.isaiajereb.gymandgram.repo.RutinasRepository;
import com.isaiajereb.gymandgram.viewmodel.RutinasViewModel;
import com.isaiajereb.gymandgram.viewmodel.RutinasViewModelFactory;
import com.isaiajereb.gymandgram.viewmodel.UsuarioViewModel;

import java.util.ArrayList;
import java.util.List;

public class EditarRutinaFragment extends Fragment {

    private FragmentEditarRutinaBinding binding;
    private NavController navController;
    private RutinasViewModel viewModel;

    private RecyclerView recyclerView;
    private EjerciciosAdapter recyclerAdapter;
    private List<Ejercicio> listaEjercicios;

    private Rutina rutina;

    public EditarRutinaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if(getArguments().get("rutina") != null) rutina = getArguments().getParcelable("rutina");
        }
        else rutina = new Rutina();

        Usuario usuario = new ViewModelProvider(requireActivity()).get(UsuarioViewModel.class).getUsuario();
        viewModel = new ViewModelProvider(requireActivity(), new RutinasViewModelFactory(requireActivity().getApplicationContext(),usuario)).get(RutinasViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditarRutinaBinding.inflate(inflater,container,false);
        navController = NavHostFragment.findNavController(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Setear el icono de la barra de navegacion
        ((MainActivity) requireActivity()).getNavigationBar().setSelectedItemId(R.id.workout_navigation);

        recyclerView = binding.ejerciciosRecyclerView;
        recyclerAdapter = new EjerciciosAdapter(new ArrayList<Ejercicio>(),requireActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        cargarInfoRutina();

        recyclerAdapter.setOnItemClickListener(new EjerciciosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Ejercicio item) {
                //TODO listener
            }
        });

        recyclerAdapter.setOnItemLongClickListener(new EjerciciosAdapter.OnItemLongClickListener() {
            @Override
            public void onEditar(Ejercicio item) {
                //TODO listener
            }

            @Override
            public void onEliminar(Ejercicio item) {
                //TODO listener
            }
        });

        binding.nombreRutinaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogoNombreRutina();
            }
        });

    }

    private void dialogoNombreRutina() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
//        builder.setTitle("Editar nombre de la rutina").setMessage("Ingrese el nuevo nombre");
//        EditText nuevoNombreET = new EditText(this.getContext());
//        builder.setView(nuevoNombreET);
//
//        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//               binding.nombreRutinaButton.setText(nuevoNombreET.getText());
//            }
//        });
//        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        builder.show();
        Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.dialog_editar_nombre_rutina);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView confirmar = dialog.findViewById(R.id.boton_confirmar);
        TextView cancelar = dialog.findViewById(R.id.boton_cancelar);
        EditText nuevoNombreET = dialog.findViewById(R.id.et_nuevo_nombre);

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nuevoNombreET.getText().toString().isEmpty()){
                binding.nombreRutinaButton.setText(nuevoNombreET.getText());
                //TODO metodo viewModel.editarNombreRUtina(idRutina,nuevoNOmbreET.getText())
                    dialog.dismiss();
                }

            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    private void cargarInfoRutina(){
        if(rutina.getId() != null) {
            binding.nombreRutinaButton.setText(rutina.getNombre());
            binding.actualSwitch.setChecked(rutina.getActual());

            //TODO cambiar por la info buscada en la BD
            listaEjercicios = RutinasRepository._EJERCICIOS;
            recyclerAdapter.setListaEjercicios(listaEjercicios);
            recyclerView.setAdapter(recyclerAdapter);
        }
    }
}