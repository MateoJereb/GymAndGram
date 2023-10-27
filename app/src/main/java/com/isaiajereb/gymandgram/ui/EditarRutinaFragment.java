package com.isaiajereb.gymandgram.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.databinding.FragmentEditarRutinaBinding;
import com.isaiajereb.gymandgram.model.Dia;
import com.isaiajereb.gymandgram.model.DiaSemana;
import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.model.Rutina;
import com.isaiajereb.gymandgram.model.Semana;
import com.isaiajereb.gymandgram.model.UnidadTiempo;
import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.recycler_views.EjerciciosAdapter;
import com.isaiajereb.gymandgram.recycler_views.SemanasAdapter;
import com.isaiajereb.gymandgram.repo.RutinasRepository;
import com.isaiajereb.gymandgram.viewmodel.RutinasViewModel;
import com.isaiajereb.gymandgram.viewmodel.RutinasViewModelFactory;
import com.isaiajereb.gymandgram.viewmodel.UsuarioViewModel;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    private Boolean rutinaGuardada;
    private Boolean mostrandoUltimoDialog = false;


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
        outState.putBoolean("mostrandoUltimoDialog",mostrandoUltimoDialog);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Usuario usuario = new ViewModelProvider(requireActivity()).get(UsuarioViewModel.class).getUsuario();
        viewModel = new ViewModelProvider(requireActivity(), new RutinasViewModelFactory(requireActivity().getApplicationContext(),usuario)).get(RutinasViewModel.class);

        if (getArguments() != null) {
            if(getArguments().get("rutina") != null){
                rutina = getArguments().getParcelable("rutina");
                rutinaGuardada = true;
            }
        }
        else{
            rutina = new Rutina();
            rutina.setNombre("Nombre rutina");
            rutina.setActual(false);
            rutina.setId_usuario(usuario.getId());
            rutinaGuardada = false;
        }

        if(savedInstanceState != null){
            rutina = savedInstanceState.getParcelable("rutina");
            semanaActual = savedInstanceState.getParcelable("semanaActual");
            diaActual = savedInstanceState.getParcelable("diaActual");
            listaSemanas = savedInstanceState.getParcelableArrayList("listaSemanas");
            listaDias = savedInstanceState.getParcelableArrayList("listaDias");
            listaEjercicios = savedInstanceState.getParcelableArrayList("listaEjercicios");
            mostrandoUltimoDialog = savedInstanceState.getBoolean("mostrandoUltimoDialog");

            huboSavedInstances = true;

            if(mostrandoUltimoDialog){
                finalizarGuardadoYVolver();
            }
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
                onEditarEjercicio(item);
            }
        });

        recyclerAdapter.setOnItemLongClickListener(new EjerciciosAdapter.OnItemLongClickListener() {
            @Override
            public void onEditar(Ejercicio item) {
                onEditarEjercicio(item);
            }

            @Override
            public void onEliminar(Ejercicio item) {
                onEliminarEjercicio(item);
            }
        });

        binding.nombreRutinaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNombreRutina();
            }
        });

        binding.compartirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireActivity(), "Proximamente...", Toast.LENGTH_SHORT).show();
            }
        });

        binding.eliminarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogoEliminarRutina();
                    }
                }
        );

        binding.seleccionarSemanaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialogoSeleccionarSemana(); }
        });

        binding.diasTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onSeleccionarTabDia(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        binding.horaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoSeleccionarHora();
            }
        });

        binding.actualSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoSeleccionarActual();
            }
        });

        binding.nuevoEjercicioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNuevoEjercicio();
            }
        });

        binding.guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGuardar();
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
        Collections.sort(ejerciciosAMostrar,Comparator.comparing(Ejercicio::getPosicion));

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
        nuevoNombreET.selectAll();

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
                AlertDialog.Builder builderConfirmarQuitar = new AlertDialog.Builder(requireContext());

                builderConfirmarQuitar.setMessage("¿Desea eliminar la Semana "+listaSemanas.size()+"?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(semanaActual.getNumero() == listaSemanas.size()){
                                    DiaSemana diaSemanaActual = diaActual.getNombre();

                                    semanaActual = listaSemanas.get(listaSemanas.size()-2);
                                    diaActual = listaDias.stream().filter(d -> d.getId_semana().equals(semanaActual.getId()) && d.getNombre().equals(diaSemanaActual)).findFirst().get();
                                    actualizarDia();
                                }

                                Semana ultimaSemana = listaSemanas.get(listaSemanas.size() - 1);
                                List<Dia> diasUltimaSemana = listaDias.stream().filter(d -> d.getId_semana().equals(ultimaSemana.getId())).collect(Collectors.toList());
                                List<UUID> idsDiasUltimaSemana = diasUltimaSemana.stream().map(d -> d.getId()).collect(Collectors.toList());
                                List<Ejercicio> ejerciciosUltimaSemana = listaEjercicios.stream().filter(e -> idsDiasUltimaSemana.contains(e.getId_dia())).collect(Collectors.toList());

                                listaSemanas.remove(ultimaSemana);
                                listaDias.removeAll(diasUltimaSemana);
                                listaEjercicios.removeAll(ejerciciosUltimaSemana);

                                if(listaSemanas.size() == 1) quitarSemana.setEnabled(false);

                                semanasAdapter.setDataSemanas(listaSemanas);
                                rvSemanas.setAdapter(semanasAdapter);
                                semanasAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("NO", null);

                AlertDialog dialogConfirmarQuitar = builderConfirmarQuitar.create();
                dialogConfirmarQuitar.show();
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

                ejsUltimoMartes.stream().forEach(e -> {
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
                            idNuevoMartes
                    );
                    ejsNuevoMartes.add(nuevoEj);
                });

                ejsUltimoMiercoles.stream().forEach(e -> {
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
                            idNuevoMiercoles
                    );
                    ejsNuevoMiercoles.add(nuevoEj);
                });

                ejsUltimoJueves.stream().forEach(e -> {
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
                            idNuevoJueves
                    );
                    ejsNuevoJueves.add(nuevoEj);
                });

                ejsUltimoViernes.stream().forEach(e -> {
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
                            idNuevoViernes
                    );
                    ejsNuevoViernes.add(nuevoEj);
                });

                ejsUltimoSabado.stream().forEach(e -> {
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
                            idNuevoSabado
                    );
                    ejsNuevoSabado.add(nuevoEj);
                });

                ejsUltimoDomingo.stream().forEach(e -> {
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
                            idNuevoDomingo
                    );
                    ejsNuevoDomingo.add(nuevoEj);
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
                DiaSemana diaSemanaActual = diaActual.getNombre();

                semanaActual = semana;
                diaActual = listaDias.stream().filter(d -> d.getId_semana().equals(semanaActual.getId()) && d.getNombre().equals(diaSemanaActual)).findFirst().get();

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

   private void dialogoEliminarRutina(){
       if(!rutinaGuardada){
           AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
           builder.setTitle("Error")
                   .setMessage("La rutina aún no se encuentra guardada, por lo que no se puede eliminar")
                   .setPositiveButton("Aceptar",null);

           AlertDialog dialog = builder.create();
           dialog.show();
       }
       else{
           AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

           builder.setMessage("¿Desea eliminar la rutina '"+rutina.getNombre()+"'?")
                   .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           viewModel.eliminarRutina(rutina);
                           navController.navigateUp();
                       }
                   })
                   .setNegativeButton("NO",null);

           AlertDialog dialog = builder.create();
           dialog.show();
       }
   }

   private void onSeleccionarTabDia(Integer numDia){
        switch (numDia){
            case 0:
                diaActual = listaDias.stream().filter(d -> d.getId_semana().equals(semanaActual.getId()) && d.getNombre().equals(DiaSemana.Lunes)).findFirst().get();
                break;
            case 1:
                diaActual = listaDias.stream().filter(d -> d.getId_semana().equals(semanaActual.getId()) && d.getNombre().equals(DiaSemana.Martes)).findFirst().get();
                break;
            case 2:
                diaActual = listaDias.stream().filter(d -> d.getId_semana().equals(semanaActual.getId()) && d.getNombre().equals(DiaSemana.Miercoles)).findFirst().get();
                break;
            case 3:
                diaActual = listaDias.stream().filter(d -> d.getId_semana().equals(semanaActual.getId()) && d.getNombre().equals(DiaSemana.Jueves)).findFirst().get();
                break;
            case 4:
                diaActual = listaDias.stream().filter(d -> d.getId_semana().equals(semanaActual.getId()) && d.getNombre().equals(DiaSemana.Viernes)).findFirst().get();
                break;
            case 5:
                diaActual = listaDias.stream().filter(d -> d.getId_semana().equals(semanaActual.getId()) && d.getNombre().equals(DiaSemana.Sabado)).findFirst().get();
                break;
            case 6:
                diaActual = listaDias.stream().filter(d -> d.getId_semana().equals(semanaActual.getId()) && d.getNombre().equals(DiaSemana.Domingo)).findFirst().get();
                break;
        }

        actualizarDia();
   }

   private void onEditarEjercicio(Ejercicio ejercicio){
       ConfigurarEjercicioDialogFragment dialog = new ConfigurarEjercicioDialogFragment();
       Bundle bundle = new Bundle();
       bundle.putParcelable("ejercicio",ejercicio);
       dialog.setArguments(bundle);

       dialog.setOnEliminarListener(new ConfigurarEjercicioDialogFragment.EliminarEjercicioListener() {
           @Override
           public void onEliminar() {
               AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
               builder.setMessage("¿Desea eliminar el ejercicio actual?")
                       .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogEliminar, int which) {
                               onEliminarEjercicio(ejercicio);
                               actualizarDia();

                               dialog.dismiss();
                           }
                       })
                       .setNegativeButton("NO",null);

               builder.create().show();
           }
       });

       dialog.setOnCancelarListener(new ConfigurarEjercicioDialogFragment.CancelarListener() {
           @Override
           public void onCancelar() {
               dialog.dismiss();
           }
       });

       dialog.setOnConfirmarListener(new ConfigurarEjercicioDialogFragment.ConfirmarEjercicioListener() {
           @Override
           public void onConfirmar() {
               ejercicio.setNombre(dialog.getEjercicioText());

               if(dialog.getSeriesText().trim().isEmpty()) ejercicio.setSeries(Optional.empty());
               else ejercicio.setSeries(Optional.of(Integer.parseInt(dialog.getSeriesText())));

               if(dialog.getRepeticionesText().trim().isEmpty()) ejercicio.setRepeticiones(Optional.empty());
               else ejercicio.setRepeticiones(Optional.of(Integer.parseInt(dialog.getRepeticionesText())));

               if(dialog.getPesoText().trim().isEmpty()) ejercicio.setPeso(Optional.empty());
               else ejercicio.setPeso(Optional.of(Math.round(Double.parseDouble(dialog.getPesoText()) * 100.0) / 100.0));

               if(dialog.getTiempoText().trim().isEmpty()){
                   ejercicio.setTiempo_unidad(Optional.empty());
                   ejercicio.setTiempo_cantidad(Optional.empty());
               }
               else{
                   ejercicio.setTiempo_cantidad(Optional.of(Math.round(Double.parseDouble(dialog.getTiempoText()) * 100.0) / 100.0));
                   if(dialog.getUnidadTiempoPosition() == 0) ejercicio.setTiempo_unidad(Optional.of(UnidadTiempo.Minuto));
                   else ejercicio.setTiempo_unidad(Optional.of(UnidadTiempo.Segundo));
               }

               ejercicio.setObservaciones(dialog.getObervacionesText());

               int pos = 0;
               while(!listaEjercicios.get(pos).getId().equals(ejercicio.getId())) pos++;

               listaEjercicios.set(pos,ejercicio);
               actualizarDia();

               dialog.dismiss();

           }
       });

       dialog.show(getChildFragmentManager(),"ConfigurarEjercicioDialogFragment");
   }

   private void onEliminarEjercicio(Ejercicio ejercicio){
       listaEjercicios.remove(ejercicio);
       actualizarDia();
   }

   private void onNuevoEjercicio(){
        ConfigurarEjercicioDialogFragment dialog = new ConfigurarEjercicioDialogFragment();

       dialog.setOnEliminarListener(new ConfigurarEjercicioDialogFragment.EliminarEjercicioListener() {
           @Override
           public void onEliminar() {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setMessage("¿Desea descartar el nuevo ejercicio?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogEliminar, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("NO",null);

                builder.create().show();
           }
       });

       dialog.setOnCancelarListener(new ConfigurarEjercicioDialogFragment.CancelarListener() {
           @Override
           public void onCancelar() {
               dialog.dismiss();
           }
       });

       dialog.setOnConfirmarListener(new ConfigurarEjercicioDialogFragment.ConfirmarEjercicioListener() {
           @Override
           public void onConfirmar() {
               Ejercicio ejercicio = new Ejercicio();
               ejercicio.setId(UUID.randomUUID());
               ejercicio.setId_dia(diaActual.getId());

               List<Ejercicio> ejerciciosDiaActual = listaEjercicios.stream().filter(e -> e.getId_dia().equals(diaActual.getId())).collect(Collectors.toList());
               Collections.sort(ejerciciosDiaActual,Comparator.comparing(Ejercicio::getPosicion));

               if(ejerciciosDiaActual.size() == 0) ejercicio.setPosicion(1);
               else ejercicio.setPosicion(ejerciciosDiaActual.get(ejerciciosDiaActual.size()-1).getPosicion() + 1);

               ejercicio.setNombre(dialog.getEjercicioText());

               if(dialog.getSeriesText().trim().isEmpty()) ejercicio.setSeries(Optional.empty());
               else ejercicio.setSeries(Optional.of(Integer.parseInt(dialog.getSeriesText())));

               if(dialog.getRepeticionesText().trim().isEmpty()) ejercicio.setRepeticiones(Optional.empty());
               else ejercicio.setRepeticiones(Optional.of(Integer.parseInt(dialog.getRepeticionesText())));

               if(dialog.getPesoText().trim().isEmpty()) ejercicio.setPeso(Optional.empty());
               else ejercicio.setPeso(Optional.of(Math.round(Double.parseDouble(dialog.getPesoText()) * 100.0) / 100.0));

               if(dialog.getTiempoText().trim().isEmpty()){
                   ejercicio.setTiempo_unidad(Optional.empty());
                   ejercicio.setTiempo_cantidad(Optional.empty());
               }
               else{
                   ejercicio.setTiempo_cantidad(Optional.of(Math.round(Double.parseDouble(dialog.getTiempoText()) * 100.0) / 100.0));
                   if(dialog.getUnidadTiempoPosition() == 0) ejercicio.setTiempo_unidad(Optional.of(UnidadTiempo.Minuto));
                   else ejercicio.setTiempo_unidad(Optional.of(UnidadTiempo.Segundo));
               }

               ejercicio.setObservaciones(dialog.getObervacionesText());

               listaEjercicios.add(ejercicio);
               actualizarDia();

               dialog.dismiss();
           }
       });

        dialog.show(getChildFragmentManager(),"ConfigurarEjercicioDialogFragment");
   }

   private void dialogoSeleccionarHora(){
       SpannableString titulo = new SpannableString("Seleccionar hora de inicio");
       Typeface typeface = ResourcesCompat.getFont(requireContext(),R.font.montserrat_bold);
       titulo.setSpan(new TypefaceSpan(typeface),0,titulo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

       MaterialTimePicker picker = new MaterialTimePicker.Builder()
               .setTimeFormat(TimeFormat.CLOCK_24H)
               .setHour(diaActual.getHora().getHour())
               .setMinute(diaActual.getHora().getMinute())
               .setTitleText(titulo)
               .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
               .build();

       picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Integer hora = picker.getHour(), min = picker.getMinute();

               String horaTexto = "";
               if(hora < 10) horaTexto+="0"+hora+":";
               else horaTexto+=hora+":";

               if(min < 10) horaTexto+="0"+min;
               else horaTexto+=min.toString();

               binding.horaButton.setText(horaTexto);

               diaActual.setHora(LocalTime.of(hora,min));
               //Todas las semanas deben tener el mismo horario para el mismo dia
               listaDias.stream().filter(d -> d.getNombre().equals(diaActual.getNombre())).forEach(d -> d.setHora(LocalTime.of(hora,min)));
           }
       });

       picker.show(requireActivity().getSupportFragmentManager(),"MATERIAL_TIME_PICKER_TAG");
   }

   private void dialogoSeleccionarActual(){
        if(binding.actualSwitch.isChecked()){
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

            builder.setMessage("¿Desea seleccionar la rutina como actual? Esto puede modificar el estado de otra rutina")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rutina.setActual(true);
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            binding.actualSwitch.setChecked(false);
                            rutina.setActual(false);
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            rutina.setActual(false);
        }
   }
   private void onGuardar(){
       viewModel.getRutinaGuardada().observe(requireActivity(), new Observer<Integer>() {
           @Override
           public void onChanged(Integer codigo) {
               if(codigo.equals(RutinasViewModel.RUTINA_GUARDADA)) {
                   viewModel.getRutinaGuardada().removeObservers(requireActivity());
                   viewModel.notificarRutinaGuardadaRecibido();

                   finalizarGuardadoYVolver();
               }
               else{
                   if(codigo.equals(RutinasViewModel.ERROR_AL_GUARDAR_RUTINA)) {
                       viewModel.notificarRutinaGuardadaRecibido();

                       AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                       builder.setTitle("Error!")
                               .setMessage("No se pudo guardar la rutina")
                               .setPositiveButton("Aceptar", null);

                       builder.create().show();
                   }
               }
           }
       });

        if(rutinaGuardada){
            rutina.setFechaUltimaModificacion(LocalDateTime.now());
            viewModel.editarRutinaCompleta(rutina,listaSemanas,listaDias,listaEjercicios);
        }
        else{
            rutina.setFechaCreacion(LocalDateTime.now());
            rutina.setFechaUltimaModificacion(LocalDateTime.now());
            viewModel.guardarRutinaCompleta(rutina,listaSemanas,listaDias,listaEjercicios);
        }
   }

   private void finalizarGuardadoYVolver(){
       AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
       builder.setMessage("Rutina guardada exitosamente!")
               .setCancelable(false)
               .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Bundle bundle = new Bundle();
                       bundle.putParcelable("rutina",rutina);

                       //Se navega hacia atras para quitar el fragmento actual de la pila, pero se vuelve a un mismo fragmento pasando la rutina guardada como una rutina que se esta editando
                       navController.popBackStack();

                       Integer currentId = navController.getCurrentDestination().getId();

                       if(currentId == R.id.inicioFragment) {
                           navController.navigate(R.id.action_inicioFragment_to_editarRutinaFragment, bundle);
                       }
                       else{
                           if(currentId == R.id.listaRutinasFragment){
                               navController.navigate(R.id.action_listaRutinasFragment_to_editarRutinaFragment,bundle);
                           }
                       }
                   }
               });

       builder.create().show();
       mostrandoUltimoDialog = true;
   }


}
