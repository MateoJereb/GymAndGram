package com.isaiajereb.gymandgram.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.databinding.FragmentEditarRutinaBinding;
import com.isaiajereb.gymandgram.model.Dia;
import com.isaiajereb.gymandgram.model.DiaSemana;
import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.model.Rutina;
import com.isaiajereb.gymandgram.model.Semana;
import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.recycler_views.EjerciciosAdapter;
import com.isaiajereb.gymandgram.recycler_views.SemanasAdapter;
import com.isaiajereb.gymandgram.repo.RutinasRepository;
import com.isaiajereb.gymandgram.viewmodel.RutinasViewModel;
import com.isaiajereb.gymandgram.viewmodel.RutinasViewModelFactory;
import com.isaiajereb.gymandgram.viewmodel.UsuarioViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EditarRutinaFragment extends Fragment {

    private FragmentEditarRutinaBinding binding;
    private NavController navController;
    private RutinasViewModel viewModel;

    private RecyclerView recyclerView;
    private EjerciciosAdapter recyclerAdapter;
    private Rutina rutina;
    private List<Semana> listaSemanas;
    private List<Dia> listaDias;
    private List<Ejercicio> listaEjercicios;

    private Semana semanaActual;
    private Dia diaActual;

    public EditarRutinaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if(getArguments().get("rutina") != null){
                rutina = getArguments().getParcelable("rutina");
            }
        }
        else{ rutina = new Rutina(); }

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
        recyclerView.setAdapter(recyclerAdapter);
        viewModel.getDatosRutinaCargados().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean valor) {
                if(valor){
                   listaSemanas = viewModel.getSemanas();
                   listaDias = viewModel.getDias();
                   listaEjercicios = viewModel.getEjercicios();

                   Collections.sort(listaSemanas, Comparator.comparing(Semana::getNumero));

                   semanaActual = listaSemanas.get(0);
                   diaActual = listaDias.stream().filter(d -> d.getId_semana().equals(semanaActual.getId()) && d.getNombre().equals(DiaSemana.Lunes)).findFirst().get();

                   actualizarRecyclewView();
                }
            }
        });

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
                dialogNombreRutina();
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

            viewModel.buscarDatosRutina(rutina);
        }
        else{
            listaSemanas = new ArrayList<>();
            listaDias = new ArrayList<>();
            listaEjercicios = new ArrayList<>();

            //TODO inicializar Semana1 y sus dias
            listaSemanas.add(new Semana(null,1,null));
        }
    }

    private void actualizarRecyclewView(){
        List<Ejercicio> ejerciciosAMostrar = listaEjercicios.stream().filter(e -> e.getId_dia().equals(diaActual.getId())).collect(Collectors.toList());
        Collections.sort(listaEjercicios,Comparator.comparing(Ejercicio::getPosicion));

        recyclerAdapter.setListaEjercicios(ejerciciosAMostrar);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }

    private void dialogNombreRutina() {
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

        AppCompatImageView quitarSemana = dialog.findViewById(R.id.quitarSemanaButton);
        AppCompatImageView agregarSemana = dialog.findViewById(R.id.agregarSemanaButton);
        RecyclerView rvSemanas = dialog.findViewById(R.id.semanasRecyclerView);
        AppCompatTextView volver = dialog.findViewById(R.id.volverButton);

        rvSemanas.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        rvSemanas.setLayoutManager(layoutManager);

        SemanasAdapter semanasAdapter = new SemanasAdapter(listaSemanas, requireContext());
        rvSemanas.setAdapter(semanasAdapter);

        if(listaSemanas.size() == 1) quitarSemana.setEnabled(false);
        quitarSemana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaSemanas.remove(listaSemanas.size()-1);
                if(listaSemanas.size() == 1) quitarSemana.setEnabled(false);

                semanasAdapter.setDataSemanas(listaSemanas);
                rvSemanas.setAdapter(semanasAdapter);
                semanasAdapter.notifyDataSetChanged();
                //TODO cambiar en la BD y si se borra la semana que se tenia seleccionada, actualizar la pantalla a la ultima disponible
            }
        });

        agregarSemana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaSemanas.add(new Semana(null,listaSemanas.size()+1,null));
                if(listaSemanas.size() == 2) quitarSemana.setEnabled(true);

                semanasAdapter.setDataSemanas(listaSemanas);
                rvSemanas.setAdapter(semanasAdapter);
                semanasAdapter.notifyDataSetChanged();

                //TODO instanciar dias y copiar los ejercicios de la semana anterior
            }
        });

        semanasAdapter.setOnItemClickListener(new SemanasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Semana semana) {
                binding.seleccionarSemanaButton.setText("Semana "+semana.getNumero().toString());
                //TODO cambiar los ejercicios a los del lunes de la semana actual
                dialog.dismiss();
            }
        });
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
   }
}