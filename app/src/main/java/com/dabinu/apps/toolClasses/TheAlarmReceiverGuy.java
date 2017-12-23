package com.dabinu.apps.toolClasses;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.dabinu.apps.fragments.GeneralActivity;
import com.dabinu.apps.models.R;


public class TheAlarmReceiverGuy extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent){

        int debt = new GeneralActivity().giveDebt(context);
        int credit = new GeneralActivity().giveCred(context);
        String debtsShouldS = "debts";
        String creditsShouldS = "credits";

        if(debt == 1){
            debtsShouldS = "debt";
        }
        if(credit == 1){
            creditsShouldS = "credit";
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.theperfectlogo).setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, GeneralActivity.class), 0)).setAutoCancel(true).setContentTitle(String.format("You have %d %s and %d %s", debt, debtsShouldS, credit, creditsShouldS)).setContentText("Touch to see details");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

        ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(1400);
    }
}
