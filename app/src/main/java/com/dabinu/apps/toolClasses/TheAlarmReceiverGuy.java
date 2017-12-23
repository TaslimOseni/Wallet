package com.dabinu.apps.toolClasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class TheAlarmReceiverGuy extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent){
        Intent service1 = new Intent(context, TheNotificationService.class);
        service1.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        context.startService(service1);
//        Toast.makeText(context, "sdfsdsd", Toast.LENGTH_LONG).show();
    }
}
