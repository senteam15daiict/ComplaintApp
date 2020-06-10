package notification;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.complaintapp.Citizen_home;
import com.example.complaintapp.Corporation_home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String sented  = remoteMessage.getData().get("sented");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null && sented.equals(firebaseUser.getUid())){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                sendOreoNotification(remoteMessage);
            }
            else {
                sendNotification(remoteMessage);
            }
        }
    }

    private void sendOreoNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String user_type = remoteMessage.getData().get("receiver_user_type");
        Intent intent;
        if(user_type.equals("Corporation")){
            intent = new Intent(this, Corporation_home.class);
        }
        else{
            intent = new Intent(this, Citizen_home.class);
        }

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Bundle bundle = new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        android.app.Notification.Builder builder = oreoNotification.getOreoNotification(title,body,pendingIntent,defaultSound,icon);

        int i = 0;
        if(j > 0){
            i = j;
        }

        oreoNotification.getManager().notify(i,builder.build());
    }


    private void sendNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String sented = remoteMessage.getData().get("sented");
        String user_type = remoteMessage.getData().get("receiver_user_type");
        String complaint_id = remoteMessage.getData().get("complaint_id");

        Intent intent;
        Bundle bundle = new Bundle();
        if(user_type.equals("Corporation")){
            /*bundle.putString("Citizen_User_Id",user);
            bundle.putString("Corporation_User_Id",sented);
            bundle.putString("Complaint_Id",complaint_id);
            bundle.putString("User_Type",user_type);*/
            intent = new Intent(this, Corporation_home.class);
        }
        else{
            /*bundle.putString("Citizen_User_Id",sented);
            bundle.putString("Corporation_User_Id",user);
            bundle.putString("Complaint_Id",complaint_id);
            bundle.putString("User_Type",user_type);*/
            intent = new Intent(this, Citizen_home.class);
        }

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        //bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);

        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int i = 0;
        if(j > 0){
            i = j;
        }

        noti.notify(i,builder.build());
    }
}
