package com.apnicompany.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AlarmReceiver extends BroadcastReceiver {

   static  Ringtone ringtone;
   static Vibrator vibrator;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    // implement onReceive() method
    public synchronized void onReceive(Context context, Intent intent) {

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

       // for (int i=0;i<=30000;i++) {

          /*  new CountDownTimer(600000, 30000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    AlarmReceiver.stopRintone();
                    AlarmReceiver.incrementVolume();

                }
            }.start();

            new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    AlarmReceiver.startRingtone();

                }
            }.start();*/

       /*     while(true){
                Timer time=new Timer();
                try{
                    time.wait(30000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                stopRintone();
                incrementVolume();

                Timer timer=new Timer();
                try{
                    timer.wait(60000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                startRingtone();

            }*/


            ScheduledExecutorService scheduler =
                    Executors.newSingleThreadScheduledExecutor();

            // stop alarm every 30 sec
            scheduler.scheduleAtFixedRate
                    (new Runnable() {
                        public void run() {
                            if(ringtone.isPlaying()) {
                                AlarmReceiver.stopRintone();
                                AlarmReceiver.incrementVolume();
                               MainActivity.DisableSnoozeButton();
                            }
                      //      new Handler().postDelayed(() -> { AlarmReceiver.startRingtone();},60000);
                        }
                    }, 30, 30, TimeUnit.SECONDS);

    // start alarm every 1 minute
        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                       AlarmReceiver.startRingtone();;
                     MainActivity.EnableSnoozeButton();
                    }
                }, 91, 90, TimeUnit.SECONDS);




        //  }

    }

    public static void stopRintone() {
        if(vibrator!=null && ringtone!=null) {
            // we will use vibrator first
            vibrator.vibrate(1);
            // play ringtone
            ringtone.stop();

        }


    }

    public static void startRingtone() {
        if(vibrator!=null && ringtone!=null) {
            // we will use vibrator first
            vibrator.vibrate(1000);
            // play ringtone
            ringtone.play();

        }


    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void incrementVolume() {
 // play ringtone
        if(ringtone!=null) {
            System.out.println("Older value  -->" + ringtone.getVolume());
            ringtone.setVolume(ringtone.getVolume() + 0.4f);
            System.out.println("New Value -->" + ringtone.getVolume());
        }
    }
}

