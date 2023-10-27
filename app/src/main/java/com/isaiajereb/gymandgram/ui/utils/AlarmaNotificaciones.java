package com.isaiajereb.gymandgram.ui.utils;


import static com.isaiajereb.gymandgram.ui.InicioFragment.MY_CHANNEL_ID;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.isaiajereb.gymandgram.ui.MainActivity;


public class AlarmaNotificaciones extends BroadcastReceiver {

    public final static int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        crearNotificacion(context);
    }

    private void crearNotificacion(Context context) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int flag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) flag = PendingIntent.FLAG_IMMUTABLE;
        else flag = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, flag);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MY_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("Hora de entrenar")
                .setContentText("Tu próximo entrenamiento está por comenzar'")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent((pendingIntent))
                .setAutoCancel(true);

        Notification notification = builder.build();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

}
