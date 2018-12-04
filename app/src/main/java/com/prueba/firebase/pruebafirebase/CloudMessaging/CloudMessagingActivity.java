package com.prueba.firebase.pruebafirebase.CloudMessaging;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.prueba.firebase.pruebafirebase.MainActivity;
import com.prueba.firebase.pruebafirebase.R;

public class CloudMessagingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_messaging);

        Intent intent = new Intent(this, MainActivity.class);

        // se instanciara como una nueva en caso de que estuviera corriendo

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // request code identifica al intent codigo privado mantener o ressplazar
        // dependiendo si es el mismo request code 0 es para que no se acumule
        // en la main activity
        // ONE SHOT solo se realizara una vez
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Hola!")
                .setContentText("Bienvenido")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        String channelID = getString(R.string.normal_channel_id);
        String channelName = getString(R.string.normal_channel_name);



        if(notificationBuilder != null){
            notificationManager.notify("",0,notificationBuilder.build());
        }




    }
}
