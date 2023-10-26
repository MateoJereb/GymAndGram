package com.isaiajereb.gymandgram.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
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

    private List<Rutina> listaRutinas;

    private Integer campoDeOrdenamiento;
    private static Integer ORDENAR_POR_NOMBRE = 0;
    private static Integer ORDENAR_POR_FECHA_CREACION = 1;
    private static Integer ORDENAR_POR_FECHA_MODIFICACION = 2;

    private SharedPreferences sharedPreferences;

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

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        campoDeOrdenamiento = sharedPreferences.getInt("campoDeOrdenamiento",ORDENAR_POR_FECHA_CREACION);

        //Cambiar la lista del adapter cuando se postee un valor en el live data
        viewModel.getRutinas().observe(requireActivity(), new Observer<List<Rutina>>() {
            @Override
            public void onChanged(List<Rutina> rutinas) {
                listaRutinas = rutinas;
                ordenarRutinas();
            }
        });

        binding.filtrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFiltrar();
            }
        });

        adapter.setOnItemClickListener(new RutinasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Rutina rutina) {
                onEditarRutina(rutina);
            }
        });

        adapter.setOnItemLongClickListener(new RutinasAdapter.OnItemLongClickListener() {
            @Override
            public void onEditar(Rutina rutina) {
                onEditarRutina(rutina);
            }

            @Override
            public void onEliminar(Rutina rutina) {
                dialogEliminarRutina(rutina);
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

    private void onEditarRutina(Rutina rutina){
        //Se crea una nueva rutina para evitar cambiar la que esta en memoria
        Rutina copiaRutina = new Rutina(
                rutina.getId(),
                rutina.getNombre(),
                rutina.getActual(),
                rutina.getFechaCreacion(),
                rutina.getFechaUltimaModificacion(),
                rutina.getId_usuario()
        );

        Bundle bundle = new Bundle();
        bundle.putParcelable("rutina",copiaRutina);
        navController.navigate(R.id.action_listaRutinasFragment_to_editarRutinaFragment,bundle);
    }

    private void ordenarRutinas(){
        if(campoDeOrdenamiento == ORDENAR_POR_NOMBRE){
            Collections.sort(listaRutinas, Comparator.comparing(Rutina::getActual).reversed().thenComparing(Rutina::getNombre));
        }
        else{
            if (campoDeOrdenamiento == ORDENAR_POR_FECHA_MODIFICACION){
                Collections.sort(listaRutinas, Comparator.comparing(Rutina::getActual).reversed().thenComparing(Rutina::getFechaUltimaModificacion));
            }
            else{ //Ordenar por fecha creacion
                Collections.sort(listaRutinas, Comparator.comparing(Rutina::getActual).reversed().thenComparing(Rutina::getFechaCreacion));
            }
        }

        adapter.setDataRutinas(listaRutinas);
        rvRutinas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void dialogEliminarRutina(Rutina rutina){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage("¿Desea eliminar la rutina '"+rutina.getNombre()+"'?")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.eliminarRutina(rutina);
                    }
                })
                .setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void dialogFiltrar(){
        SpannableString textoNombre = new SpannableString("Nombre"),
                        textoFechaCreacion = new SpannableString("Fecha de creación"),
                        textoFechaModificacion = new SpannableString("Fecha de modificación");

        Typeface typeface = ResourcesCompat.getFont(requireContext(),R.font.verdana);
        textoNombre.setSpan(new TypefaceSpan(typeface),0,textoNombre.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textoFechaCreacion.setSpan(new TypefaceSpan(typeface),0,textoFechaCreacion.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textoFechaModificacion.setSpan(new TypefaceSpan(typeface),0,textoFechaModificacion.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textoNombre.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.mid)),0,textoNombre.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textoFechaCreacion.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.mid)),0,textoFechaCreacion.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textoFechaModificacion.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.mid)),0,textoFechaModificacion.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString[] opciones = {textoNombre,textoFechaCreacion,textoFechaModificacion};

        View titleView = getLayoutInflater().inflate(R.layout.dialog_filtrar_rutinas,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(requireActivity(),R.style.BackgroundColor));
        builder.setCustomTitle(titleView)
                .setSingleChoiceItems(opciones, campoDeOrdenamiento, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selecccionado) {
                        campoDeOrdenamiento = selecccionado;
                        ordenarRutinas();

                        SharedPreferences.Editor spEditor = sharedPreferences.edit();
                        spEditor.putInt("campoDeOrdenamiento",campoDeOrdenamiento);
                        spEditor.apply();

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Volver",null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}