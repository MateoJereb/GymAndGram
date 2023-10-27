package com.isaiajereb.gymandgram.ui;

import static com.isaiajereb.gymandgram.ui.utils.AlarmaNotificaciones.NOTIFICATION_ID;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import com.isaiajereb.gymandgram.model.Dia;
import com.isaiajereb.gymandgram.model.DiaSemana;
import com.isaiajereb.gymandgram.model.Rutina;
import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.ui.utils.AlarmaNotificaciones;
import com.isaiajereb.gymandgram.viewmodel.RutinasViewModel;
import com.isaiajereb.gymandgram.viewmodel.RutinasViewModelFactory;
import com.isaiajereb.gymandgram.viewmodel.UsuarioViewModel;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class InicioFragment extends Fragment {

    private FragmentInicioBinding binding;
    private NavController navController;
    private RutinasViewModel rutinasViewModel;
    public final static String MY_CHANNEL_ID = "canal_notificaciones";

    public InicioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        Usuario usuario = new ViewModelProvider(requireActivity()).get(UsuarioViewModel.class).getUsuario();
        rutinasViewModel = new ViewModelProvider(requireActivity(), new RutinasViewModelFactory(requireActivity().getApplicationContext(), usuario)).get(RutinasViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);
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
                Rutina rutinaActual = rutinasViewModel.getRutinaActual();
                if (rutinaActual != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("rutina", rutinaActual);
                    navController.navigate(R.id.action_inicioFragment_to_editarRutinaFragment, bundle);
                }

            }
        });

        binding.verObjetivosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).getNavigationBar().setSelectedItemId(R.id.social_navigation);
            }
        });

        rutinasViewModel.getDiasRutinaActual().observe(requireActivity(), new Observer<List<Dia>>() {
            @Override
            public void onChanged(List<Dia> dias) {
                if (dias.size() > 0) {
                    actualizarProximoEntrenamiento(dias);
                    createChannel();
                    programarNotificaciones(getContext(), dias);
                } else {
                    binding.proxExtranamientoTextView.setText("Configure su rutina actual");
                }
            }
        });

        elegirFrase();
    }

    private void elegirFrase() {
        Integer[] imagenes = {R.drawable.imagen_1, R.drawable.imagen_2, R.drawable.imagen_3, R.drawable.imagen_4, R.drawable.imagen_5, R.drawable.imagen_6, R.drawable.imagen_7, R.drawable.imagen_8};
        Integer[] frases = {R.string.frase1, R.string.frase2, R.string.frase3, R.string.frase4, R.string.frase5, R.string.frase6, R.string.frase7, R.string.frase8};

        Integer numRandom = new Random().nextInt(8);

        binding.fraseTextView.setText(frases[numRandom]);
        binding.fraseImageView.setImageDrawable(getResources().getDrawable(imagenes[numRandom]));
    }

    private void actualizarProximoEntrenamiento(List<Dia> dias) {
        LocalDateTime fechaProxEntrenamiento = calcularProximoEntrenamiento(dias, LocalDateTime.now());

        String nombreDia = DiaSemana.values()[fechaProxEntrenamiento.getDayOfWeek().getValue() - 1].toString();

        Integer numDia = fechaProxEntrenamiento.getDayOfMonth();

        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        String nombreMes = meses[fechaProxEntrenamiento.getMonthValue() - 1];

        String horaFormat = fechaProxEntrenamiento.format(DateTimeFormatter.ofPattern("HH:mm"));

        String textoProxEntrenamiento = nombreDia + " " + numDia + " de " + nombreMes + ", " + horaFormat + " hs";
        binding.proxExtranamientoTextView.setText(textoProxEntrenamiento);
    }

    public LocalDateTime calcularProximoEntrenamiento(List<Dia> dias, LocalDateTime fechaActual){
        DiaSemana diaActual = DiaSemana.values()[fechaActual.getDayOfWeek().getValue()-1];
        LocalTime horaActual = LocalTime.of(fechaActual.getHour(),fechaActual.getMinute());

        //Buscar el prox entrenamiento (los dias pueden no estar ordenados)
        Dia diaProxEntrenamiento = null;
        for (Dia d : dias) {
            if (d.getNombre().ordinal() == diaActual.ordinal() && !d.getHora().isBefore(horaActual)) {
                diaProxEntrenamiento = d;
            } else {
                if (d.getNombre().ordinal() > diaActual.ordinal()) {
                    if (diaProxEntrenamiento == null) {
                        diaProxEntrenamiento = d;
                    } else {
                        if (d.getNombre().ordinal() < diaProxEntrenamiento.getNombre().ordinal()) {
                            diaProxEntrenamiento = d;
                        }
                    }
                }
            }
        }

        if (diaProxEntrenamiento == null) diaProxEntrenamiento = dias.get(0);


        LocalDateTime fechaProxEntrenamiento = fechaActual;

        if (diaActual.ordinal() == diaProxEntrenamiento.getNombre().ordinal()) { //Mismo dia de la semana
            if (horaActual.isAfter(diaProxEntrenamiento.getHora())) { //Proxima semana
                fechaProxEntrenamiento = fechaProxEntrenamiento.plusDays(7);
            }
        } else {
            DayOfWeek dayOfWeek = DayOfWeek.values()[diaProxEntrenamiento.getNombre().ordinal()];
            fechaProxEntrenamiento = fechaProxEntrenamiento.with(TemporalAdjusters.next(dayOfWeek));
        }

        Integer hour = diaProxEntrenamiento.getHora().getHour(),
                minute = diaProxEntrenamiento.getHora().getMinute();

        fechaProxEntrenamiento = fechaProxEntrenamiento.withHour(hour).withMinute(minute);

        return fechaProxEntrenamiento;
    }

    private void formatearContadoresObjetivos() {
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

    private void programarNotificaciones(Context context, List<Dia> dias) {
        for (Dia dia:dias){
        setearAlarma(context, dia);
        }
    }

    private void setearAlarma(Context context, Dia dia){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmaNotificaciones.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent,
                        PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        switch (dia.getNombre()){
            case Lunes:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            case Martes:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            case Miercoles:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            case Jueves:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            case Viernes:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            case Sabado:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            case Domingo:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        }
        calendar.set(Calendar.HOUR_OF_DAY, dia.getHora().getHour());
        calendar.set(Calendar.MINUTE, dia.getHora().getMinute()-30);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().getTimeInMillis(),
                //1 semana en milisegundos
                1000+60*60*24*7,
                pendingIntent);
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    MY_CHANNEL_ID,
                    "canal_notificaciones",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = this.getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

}