package com.apnicompany.Alarm;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TimePicker alarmTimePicker;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    Intent intent;
    static Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        btn = (Button) findViewById(R.id.toggleButton2);


    }

    // OnToggleClicked() method is implemented the time functionality
    public void OnToggleClicked(View view) {

        long time;
        if (((ToggleButton) view).isChecked()) {
            Toast.makeText(MainActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();

            // calender is called to get current time in hour and minute
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
            calendar.set(Calendar.SECOND,1);

            // set alarm 10 minutes earlier
            calendar.add(Calendar.MINUTE, -10);
            Toast.makeText(MainActivity.this,  calendar.getTime().toString(), Toast.LENGTH_SHORT).show();
            // using intent i have class AlarmReceiver class which inherits
            // BroadcastReceiver

             intent = new Intent(this, AlarmReceiver.class);
            // we call broadcast using pendingIntent
            pendingIntent = PendingIntent.getBroadcast(this, 321, intent, 0);
            time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
            if (System.currentTimeMillis() > time) {
                // setting time as AM and PM
                if (calendar.AM_PM == 0)
                    time = time + (1000 * 60 * 60 * 12);
                else
                    time = time + (1000 * 60 * 60 * 24);
            }
            // Alarm rings continuously until toggle button is turned off
       //     alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);

            // Notification

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "123")
                 //   .setSmallIcon(R.drawable.)
                    .setContentTitle("My notification")
                    .setContentText("Hello World!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .addAction(R.id.toggleButton2, "snooze",
                            pendingIntent);


            // alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent);
        } else {
         //   pendingIntent=PendingIntent.getBroadcast(this,321,intent,PendingIntent.FLAG_CANCEL_CURRENT);
if(pendingIntent!=null && alarmManager!=null) {
    alarmManager.cancel(pendingIntent);
    AlarmReceiver.stopRintone();
    AlarmReceiver.ringtone = null;
    pendingIntent.cancel();

//            alarmManager.cancel(pendingIntent);
//            pendingIntent.cancel();
            Toast.makeText(MainActivity.this, "ALARM OFF", Toast.LENGTH_SHORT).show();

}
            DisableSnoozeButton();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void OnSnoozeClicked(View view) {
        long time;
        alarmManager.cancel(pendingIntent);
        AlarmReceiver.stopRintone();
        pendingIntent.cancel();
            AlarmReceiver.incrementVolume();
            Toast.makeText(MainActivity.this, "Snoozed for 1 minute", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE,1);
            // calender is called to get current time in hour and minute
          /*  calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());*/

            // using intent i have class AlarmReceiver class which inherits
            // BroadcastReceiver
            Intent intent = new Intent(this, AlarmReceiver.class);

            // we call broadcast using pendingIntent
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
            if (System.currentTimeMillis() > time) {
                // setting time as AM and PM
                if (calendar.AM_PM == 0)
                    time = time + (1000 * 60 * 60 * 12);
                else
                    time = time + (1000 * 60 * 60 * 24);
            }
            // Alarm rings continuously until toggle button is turned off
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            // alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent);
        MainActivity.DisableSnoozeButton();
    }


    public static  void EnableSnoozeButton() {
        btn.setVisibility(View.VISIBLE);
    }

    public static void DisableSnoozeButton() {
        btn.setVisibility(View.GONE);
        btn.setVisibility(View.INVISIBLE);
    }
}
