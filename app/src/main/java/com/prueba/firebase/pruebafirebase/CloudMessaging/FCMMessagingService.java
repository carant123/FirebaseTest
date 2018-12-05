package com.prueba.firebase.pruebafirebase.CloudMessaging;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.prueba.firebase.pruebafirebase.MainActivity;
import com.prueba.firebase.pruebafirebase.R;

public class FCMMessagingService extends FirebaseMessagingService {

    private static final String DESCUENTIO = "descuento";

    public FCMMessagingService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData().size() > 0 && remoteMessage.getNotification() != null){
            sendNotification(remoteMessage);
        }

    }

    private void sendNotification(RemoteMessage remoteMessage) {
        float desc = Float.valueOf(remoteMessage.getData().get(DESCUENTIO));

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(DESCUENTIO,desc);
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
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notificationBuilder.setColor(desc > .4?
                    ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary) :
                    ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        }


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            String channelID = desc <.10? getString(R.string.normal_channel_id) :
                    getString(R.string.bajo_channel_id);
            String channelName = desc <.10? getString(R.string.normal_channel_name) :
                    getString(R.string.bajo_channel_name);
            NotificationChannel channel = new NotificationChannel(channelID,channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100,200,200,50});
            if(notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }

            notificationBuilder.setChannelId(channelID);

        }


        if(notificationBuilder != null){
            notificationManager.notify("",0,notificationBuilder.build());
        }

    }
}
