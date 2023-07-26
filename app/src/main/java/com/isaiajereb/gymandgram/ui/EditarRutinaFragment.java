package com.isaiajereb.gymandgram.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.isaiajereb.gymandgram.model.UnidadTiempo;
import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.recycler_views.EjerciciosAdapter;
import com.isaiajereb.gymandgram.repo.RutinasRepository;
import com.isaiajereb.gymandgram.viewmodel.RutinasViewModel;
import com.isaiajereb.gymandgram.viewmodel.RutinasViewModelFactory;
import com.isaiajereb.gymandgram.viewmodel.UsuarioViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
                Bundle bundle = new Bundle();
                //TODO agregar implementacion real de la rutina.
                bundle.putInt("idRutina", 1);
                bundle.putParcelable("ejercicio", item);
                navController.navigate(R.id.action_editarRutinaFragment_to_configurarEjercicioFragment, bundle);}
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

        binding.seleccionarSemanaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialogoSeleccionarSemana(); }
        });

        binding.nuevoEjercicioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                //TODO agregar implementacion real de la rutina.
                bundle.putInt("idRutina", 1);
                navController.navigate(R.id.action_editarRutinaFragment_to_configurarEjercicioFragment, bundle);
            }
        });

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
    private void dialogoNombreRutina() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_editar_nombre_rutina);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        TextView confirmar = dialog.findViewById(R.id.boton_confirmar);
        TextView cancelar = dialog.findViewById(R.id.boton_cancelar);
        EditText nuevoNombreET = dialog.findViewById(R.id.et_nuevo_nombre);

        nuevoNombreET.setText(binding.nombreRutinaButton.getText().toString());

        //Abrir teclado
        nuevoNombreET.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nuevoNombreET.getText().toString().isEmpty()){
                    //Cerrar teclado
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);

                    binding.nombreRutinaButton.setText(nuevoNombreET.getText().toString());

                    //TODO metodo viewModel.editarNombreRutina(idRutina,nuevoNombreET.getText())
                    dialog.dismiss();
                }

            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cerrar teclado
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(nuevoNombreET.getWindowToken(),0);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

   private void dialogoSeleccionarSemana(){
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_seleccionar_semana);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);



        dialog.show();
   }
}