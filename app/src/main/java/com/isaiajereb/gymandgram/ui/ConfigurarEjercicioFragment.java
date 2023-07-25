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
import android.widget.ArrayAdapter;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.databinding.FragmentConfigurarEjercicioBinding;
import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.model.UnidadTiempo;
import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.viewmodel.RutinasViewModel;
import com.isaiajereb.gymandgram.viewmodel.RutinasViewModelFactory;
import com.isaiajereb.gymandgram.viewmodel.UsuarioViewModel;

import java.util.Optional;
import java.util.UUID;

public class ConfigurarEjercicioFragment extends Fragment {

    private FragmentConfigurarEjercicioBinding binding;
    private NavController navController;
    private RutinasViewModel viewModel;
    private Integer idRutina;
    private Ejercicio ejercicio;

    public ConfigurarEjercicioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idRutina = getArguments().getInt("idRutina");
            if(getArguments().getParcelable("ejercicio")!=null){
                ejercicio = getArguments().getParcelable("ejercicio");
            }else ejercicio = new Ejercicio();
        }

        Usuario usuario = new ViewModelProvider(requireActivity()).get(UsuarioViewModel.class).getUsuario();
        viewModel = new ViewModelProvider(requireActivity(), new RutinasViewModelFactory(requireActivity().getApplicationContext(),usuario)).get(RutinasViewModel.class);
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

        //Setear el icono de la barra de navegacion
        ((MainActivity) requireActivity()).getNavigationBar().setSelectedItemId(R.id.workout_navigation);

        llenarSpiner();
        llenarDatosEjercicio();

    }

    private void llenarSpiner(){
        String[] unidadesTiempo = getResources().getStringArray(R.array.unidad_tiempo);
        ArrayAdapter<String> adapterUnidadTiempo = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, unidadesTiempo);
        binding.unidadTiempoSpinner.setAdapter(adapterUnidadTiempo);
    }

    private void llenarDatosEjercicio() {
            if(ejercicio.getNombre()!=null)binding.ejercicioEditText.setText(ejercicio.getNombre());
            ejercicio.getSeries().ifPresent(s-> binding.seriesEditText.setText(s.toString()));
            ejercicio.getRepeticiones().ifPresent(rep-> binding.repeticionesEditText.setText(rep.toString()));
            ejercicio.getPeso().ifPresent(p->binding.pesoEditText.setText(p.toString()));
            ejercicio.getTiempo_cantidad().ifPresent(tc-> binding.tiempoEditText.setText(tc.toString()));
            ejercicio.getTiempo_unidad().ifPresent(ut->
                    binding.unidadTiempoSpinner.setSelection(((ArrayAdapter)binding.unidadTiempoSpinner.getAdapter()).getPosition(ut.name()))
            );
        if(ejercicio.getObservaciones()!=null)binding.observacionesEditText.setText(ejercicio.getObservaciones());
    }
}