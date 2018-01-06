package com.example.danilo.testingmap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class jobsAcceptance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_acceptance);

        //redirect driver to main driver menu
        //Intent intentDriverMain = new Intent(getApplicationContext(),DriverMain.class);
        //startActivity(intentDriverMain);
        /*
        PendingIntent pItent = PendingIntent.getActivity(this,0, intentSignUP, 0);
        Notification noti = new Notification.Builder(this)
                .setTicker("Ticker Title")   //ticker title
                .setContentTitle("Food Delivering")
                .setContentText("Customer order ")
                .setSmallIcon(R.mipmap.yellowtaxi)
                .setContentIntent(pItent).getNotification();
        noti.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, noti);
        */
    }
}
