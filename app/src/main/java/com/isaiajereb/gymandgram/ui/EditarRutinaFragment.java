package com.isaiajereb.gymandgram.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
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

    private Boolean huboSavedInstances;

    public EditarRutinaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("rutina",rutina);
        outState.putParcelable("semanaActual",semanaActual);
        outState.putParcelable("diaActual",diaActual);
        outState.putParcelableArrayList("listaSemanas",(ArrayList) listaSemanas);
        outState.putParcelableArrayList("listaDias",(ArrayList) listaDias);
        outState.putParcelableArrayList("listaEjercicios",(ArrayList) listaEjercicios);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Usuario usuario = new ViewModelProvider(requireActivity()).get(UsuarioViewModel.class).getUsuario();
        viewModel = new ViewModelProvider(requireActivity(), new RutinasViewModelFactory(requireActivity().getApplicationContext(),usuario)).get(RutinasViewModel.class);

        if (getArguments() != null) {
            if(getArguments().get("rutina") != null){
                rutina = getArguments().getParcelable("rutina");
            }
        }
        else{
            rutina = new Rutina();
            rutina.setId_usuario(usuario.getId());
        }

        if(savedInstanceState != null){
            rutina = savedInstanceState.getParcelable("rutina");
            semanaActual = savedInstanceState.getParcelable("semanaActual");
            diaActual = savedInstanceState.getParcelable("diaActual");
            listaSemanas = savedInstanceState.getParcelableArrayList("listaSemanas");
            listaDias = savedInstanceState.getParcelableArrayList("listaDias");
            listaEjercicios = savedInstanceState.getParcelableArrayList("listaEjercicios");

            huboSavedInstances = true;
        }
        else{
            huboSavedInstances = false;
        }
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

                   actualizarDia();

                   //Metodo auxiliar que publica un false en datosRutinaCargados para evitar actualizar nuevamente las listas al girar la pantalla
                   viewModel.notificarDatosRecibidos();
                }
            }
        });

        //Setea el nombre de la rutina, el switch del actual y busca los datos en la BD (editar) o instancia 1 semana con 7 dias (nueva rutina)
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
            //Editar rutina
            binding.nombreRutinaButton.setText(rutina.getNombre());
            binding.actualSwitch.setChecked(rutina.getActual());

            if(!huboSavedInstances) viewModel.buscarDatosRutina(rutina);
            else actualizarDia();
        }
        else{
            if(!huboSavedInstances) {
                //Crear rutina
                rutina.setId(UUID.randomUUID());

                listaSemanas = new ArrayList<>();
                listaDias = new ArrayList<>();
                listaEjercicios = new ArrayList<>();

                UUID idPrimeraSemana = UUID.randomUUID();
                Semana primeraSemana = new Semana(idPrimeraSemana, 1, rutina.getId());
                listaSemanas.add(primeraSemana);

                Dia lunes = new Dia(UUID.randomUUID(), DiaSemana.Lunes, LocalTime.of(0, 0), idPrimeraSemana);
                Dia martes = new Dia(UUID.randomUUID(), DiaSemana.Martes, LocalTime.of(0, 0), idPrimeraSemana);
                Dia miercoles = new Dia(UUID.randomUUID(), DiaSemana.Miercoles, LocalTime.of(0, 0), idPrimeraSemana);
                Dia jueves = new Dia(UUID.randomUUID(), DiaSemana.Jueves, LocalTime.of(0, 0), idPrimeraSemana);
                Dia viernes = new Dia(UUID.randomUUID(), DiaSemana.Viernes, LocalTime.of(0, 0), idPrimeraSemana);
                Dia sabado = new Dia(UUID.randomUUID(), DiaSemana.Sabado, LocalTime.of(0, 0), idPrimeraSemana);
                Dia domingo = new Dia(UUID.randomUUID(), DiaSemana.Domingo, LocalTime.of(0, 0), idPrimeraSemana);

                listaDias.add(lunes);
                listaDias.add(martes);
                listaDias.add(miercoles);
                listaDias.add(jueves);
                listaDias.add(viernes);
                listaDias.add(sabado);
                listaDias.add(domingo);

                semanaActual = primeraSemana;
                diaActual = lunes;
                actualizarDia();
            }
            else actualizarDia();
        }
    }

    private void actualizarDia(){
        List<Ejercicio> ejerciciosAMostrar = listaEjercicios.stream().filter(e -> e.getId_dia().equals(diaActual.getId())).collect(Collectors.toList());
        Collections.sort(listaEjercicios,Comparator.comparing(Ejercicio::getPosicion));

        recyclerAdapter.setListaEjercicios(ejerciciosAMostrar);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();

        binding.horaButton.setText(diaActual.getHora().format(DateTimeFormatter.ofPattern("HH:mm")));
        binding.seleccionarSemanaButton.setText("Semana "+semanaActual.getNumero());
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
                    rutina.setNombre(nuevoNombreET.getText().toString());

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
                Semana nuevaSemana = new Semana(UUID.randomUUID(),listaSemanas.size()+1,rutina.getId());

                //Copiar dias y ejercicios de la ultima semana en la nueva
                Dia ultimoLunes = listaDias.stream().filter(d -> d.getId_semana().equals(listaSemanas.get(listaSemanas.size()-1).getId()) && d.getNombre().equals(DiaSemana.Lunes)).findFirst().get();
                Dia ultimoMartes = listaDias.stream().filter(d -> d.getId_semana().equals(listaSemanas.get(listaSemanas.size()-1).getId()) && d.getNombre().equals(DiaSemana.Martes)).findFirst().get();
                Dia ultimoMiercoles = listaDias.stream().filter(d -> d.getId_semana().equals(listaSemanas.get(listaSemanas.size()-1).getId()) && d.getNombre().equals(DiaSemana.Miercoles)).findFirst().get();
                Dia ultimoJueves = listaDias.stream().filter(d -> d.getId_semana().equals(listaSemanas.get(listaSemanas.size()-1).getId()) && d.getNombre().equals(DiaSemana.Jueves)).findFirst().get();
                Dia ultimoViernes = listaDias.stream().filter(d -> d.getId_semana().equals(listaSemanas.get(listaSemanas.size()-1).getId()) && d.getNombre().equals(DiaSemana.Viernes)).findFirst().get();
                Dia ultimoSabado = listaDias.stream().filter(d -> d.getId_semana().equals(listaSemanas.get(listaSemanas.size()-1).getId()) && d.getNombre().equals(DiaSemana.Sabado)).findFirst().get();
                Dia ultimoDomingo = listaDias.stream().filter(d -> d.getId_semana().equals(listaSemanas.get(listaSemanas.size()-1).getId()) && d.getNombre().equals(DiaSemana.Domingo)).findFirst().get();

                List<Ejercicio> ejsUltimoLunes = listaEjercicios.stream().filter(e -> e.getId_dia().equals(ultimoLunes.getId())).collect(Collectors.toList());
                List<Ejercicio> ejsUltimoMartes = listaEjercicios.stream().filter(e -> e.getId_dia().equals(ultimoMartes.getId())).collect(Collectors.toList());
                List<Ejercicio> ejsUltimoMiercoles = listaEjercicios.stream().filter(e -> e.getId_dia().equals(ultimoMiercoles.getId())).collect(Collectors.toList());
                List<Ejercicio> ejsUltimoJueves = listaEjercicios.stream().filter(e -> e.getId_dia().equals(ultimoJueves.getId())).collect(Collectors.toList());
                List<Ejercicio> ejsUltimoViernes = listaEjercicios.stream().filter(e -> e.getId_dia().equals(ultimoViernes.getId())).collect(Collectors.toList());
                List<Ejercicio> ejsUltimoSabado = listaEjercicios.stream().filter(e -> e.getId_dia().equals(ultimoSabado.getId())).collect(Collectors.toList());
                List<Ejercicio> ejsUltimoDomingo = listaEjercicios.stream().filter(e -> e.getId_dia().equals(ultimoDomingo.getId())).collect(Collectors.toList());

                UUID idNuevoLunes = UUID.randomUUID();
                UUID idNuevoMartes = UUID.randomUUID();
                UUID idNuevoMiercoles = UUID.randomUUID();
                UUID idNuevoJueves = UUID.randomUUID();
                UUID idNuevoViernes = UUID.randomUUID();
                UUID idNuevoSabado = UUID.randomUUID();
                UUID idNuevoDomingo = UUID.randomUUID();

                Dia nuevoLunes = new Dia(idNuevoLunes, ultimoLunes.getNombre(), ultimoLunes.getHora(), nuevaSemana.getId());
                Dia nuevoMartes = new Dia(idNuevoMartes, ultimoMartes.getNombre(), ultimoMartes.getHora(), nuevaSemana.getId());
                Dia nuevoMiercoles = new Dia(idNuevoMiercoles, ultimoMiercoles.getNombre(), ultimoMiercoles.getHora(), nuevaSemana.getId());
                Dia nuevoJueves = new Dia(idNuevoJueves, ultimoJueves.getNombre(), ultimoJueves.getHora(), nuevaSemana.getId());
                Dia nuevoViernes = new Dia(idNuevoViernes, ultimoViernes.getNombre(), ultimoViernes.getHora(), nuevaSemana.getId());
                Dia nuevoSabado = new Dia(idNuevoSabado, ultimoSabado.getNombre(), ultimoSabado.getHora(), nuevaSemana.getId());
                Dia nuevoDomingo = new Dia(idNuevoDomingo, ultimoDomingo.getNombre(), ultimoDomingo.getHora(), nuevaSemana.getId());

                List<Ejercicio> ejsNuevoLunes = new ArrayList<>();
                List<Ejercicio> ejsNuevoMartes = new ArrayList<>();
                List<Ejercicio> ejsNuevoMiercoles = new ArrayList<>();
                List<Ejercicio> ejsNuevoJueves = new ArrayList<>();
                List<Ejercicio> ejsNuevoViernes = new ArrayList<>();
                List<Ejercicio> ejsNuevoSabado = new ArrayList<>();
                List<Ejercicio> ejsNuevoDomingo = new ArrayList<>();

                ejsUltimoLunes.stream().forEach(e -> {
                    Ejercicio nuevoEj = new Ejercicio(
                            UUID.randomUUID(),
                            e.getNombre(),
                            e.getPosicion(),
                            e.getSeries(),
                            e.getRepeticiones(),
                            e.getPeso(),
                            e.getTiempo_cantidad(),
                            e.getTiempo_unidad(),
                            e.getObservaciones(),
                            idNuevoLunes
                    );
                    ejsNuevoLunes.add(nuevoEj);
                });


                listaSemanas.add(nuevaSemana);

                listaDias.add(nuevoLunes);
                listaDias.add(nuevoMartes);
                listaDias.add(nuevoMiercoles);
                listaDias.add(nuevoJueves);
                listaDias.add(nuevoViernes);
                listaDias.add(nuevoSabado);
                listaDias.add(nuevoDomingo);

                listaEjercicios.addAll(ejsNuevoLunes);
                listaEjercicios.addAll(ejsNuevoMartes);
                listaEjercicios.addAll(ejsNuevoMiercoles);
                listaEjercicios.addAll(ejsNuevoJueves);
                listaEjercicios.addAll(ejsNuevoViernes);
                listaEjercicios.addAll(ejsNuevoSabado);
                listaEjercicios.addAll(ejsNuevoDomingo);

                if(listaSemanas.size() == 2) quitarSemana.setEnabled(true);

                semanasAdapter.setDataSemanas(listaSemanas);
                rvSemanas.setAdapter(semanasAdapter);
                semanasAdapter.notifyDataSetChanged();
            }
        });

        semanasAdapter.setOnItemClickListener(new SemanasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Semana semana) {
                semanaActual = semana;
                diaActual = listaDias.stream().filter(d -> d.getId_semana().equals(semanaActual.getId()) && d.getNombre().equals(DiaSemana.Lunes)).findFirst().get();
                actualizarDia();
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

   private void onGuardar(){
        //TODO guardar todos los cambios en la BD
   }
}
