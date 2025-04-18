package com.example.scrollease;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import androidx.annotation.NonNull;
import android.content.Context;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class NotificationFeature {
    public static final String CHANNEL_ID =  "Darren_id";
    public void persistentNotification(@NonNull Context context,View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }

            CharSequence name = context.getString(R.string.app_name);
            String description = context.getString(R.string.app_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager =  context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("ScrollEase")
                .setContentText("Simple scrolling app")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("A Simple Scrolling App that can help you scroll if you are handicapped"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notifyManager = NotificationManagerCompat.from(context);
        notifyManager.notify(1, builder.build());

    }

    public void SRFPermission(@NonNull Context context){
        // this requests user's permission if the app can use the device's mic
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions
                    ((Activity) context,new String[]{Manifest.permission.RECORD_AUDIO},1);
        }
    }
}
