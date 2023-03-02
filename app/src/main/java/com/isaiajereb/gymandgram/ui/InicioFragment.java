package com.isaiajereb.gymandgram.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.databinding.FragmentInicioBinding;

public class InicioFragment extends Fragment {

    private FragmentInicioBinding binding;
    private NavController navController;

    public InicioFragment() {
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
        binding = FragmentInicioBinding.inflate(inflater,container,false);
        navController = NavHostFragment.findNavController(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        formatearContadoresObjetivos();

        //Binding botones (Ver rutina y Ver objetivos)
        binding.verRutinaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO conseguir rutina actual y pasarla por un Bundle
                navController.navigate(R.id.action_inicioFragment_to_editarRutinaFragment);
            }
        });

        binding.verObjetivosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).getNavigationBar().setSelectedItemId(R.id.social_navigation);
            }
        });
    }

    private void formatearContadoresObjetivos(){
        SpannableString spannableString = new SpannableString("0/0");
        ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.parseColor("#FF000000"));
        spannableString.setSpan(blackSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(blackSpan, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan graySpan = new ForegroundColorSpan(Color.parseColor("#4B4B4B"));
        spannableString.setSpan(graySpan, 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.8f);
        spannableString.setSpan(sizeSpan, 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.contadorKg.setText(spannableString);
        binding.contadorEntrenamientos.setText(spannableString);
        binding.contadorCaminata.setText(spannableString);
    }
}