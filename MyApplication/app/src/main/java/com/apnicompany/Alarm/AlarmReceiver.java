package com.apnicompany.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class AlarmReceiver extends BroadcastReceiver {

   static  Ringtone ringtone;
   static Vibrator vibrator;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    // implement onReceive() method
    public void onReceive(Context context, Intent intent) {

        // we will use vibrator first
         vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show();
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        // setting default ringtone
        if(ringtone==null) {
            ringtone = RingtoneManager.getRingtone(context, alarmUri);
            // play ringtone
            System.out.println("Older value  -->" + ringtone.getVolume());
            ringtone.setVolume(0.3f);
            System.out.println("New Value -->" + ringtone.getVolume());
        }

        ringtone.play();
        MainActivity.EnableSnoozeButton();



    }

    public static void stopRintone(Context context, Intent intent) {
        if(vibrator!=null && ringtone!=null) {
            // we will use vibrator first
            vibrator.vibrate(1);
            // play ringtone
            ringtone.stop();

        }


    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void incrementVolume(Context context, Intent intent) {
 // play ringtone
        if(ringtone!=null) {
            System.out.println("Older value  -->" + ringtone.getVolume());
            ringtone.setVolume(ringtone.getVolume() + 0.4f);
            System.out.println("New Value -->" + ringtone.getVolume());
        }
    }
}

