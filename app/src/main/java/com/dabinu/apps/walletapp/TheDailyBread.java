package com.dabinu.apps.walletapp;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class TheDailyBread extends IntentService{


    public TheDailyBread(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
