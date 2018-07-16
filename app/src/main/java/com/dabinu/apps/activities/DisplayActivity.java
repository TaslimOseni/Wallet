package com.dabinu.apps.activities;

import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.dabinu.apps.fragments.AllFragment;
import com.dabinu.apps.fragments.CreditFragment;
import com.dabinu.apps.fragments.DebtFragment;
import com.dabinu.apps.models.R;
import com.dabinu.apps.models.SingleWallet;
import com.dabinu.apps.toolClasses.TheAlarmReceiverGuy;
import java.io.File;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;



public class DisplayActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(0, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, TheAlarmReceiverGuy.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(DisplayActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        alarmManager.cancel(pendingIntent);

        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 14);
        alarmStartTime.set(Calendar.MINUTE, 46);
        alarmStartTime.set(Calendar.SECOND, 0);
        if(now.after(alarmStartTime)){
            alarmStartTime.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        (findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NewField.class));
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Context context = this;

        View headerView = navigationView.getHeaderView(0);


        File[] listOf = getFilesDir().listFiles();

        final ArrayList<String> allTheBloodyTransactionsGuy = new ArrayList<>();

        for(File i: listOf){
            try{
                SingleWallet walletObject = (SingleWallet) (new ObjectInputStream(openFileInput(i.getName()))).readObject();
                allTheBloodyTransactionsGuy.add(walletObject.getReturnStatement().concat("#").concat(walletObject.getAmount()).concat(walletObject.getPreposition()).concat(walletObject.getNameOfWallet()));
            }
            catch(Exception e){
                //Nothing
            }
        }


        ((TextView) headerView.findViewById(R.id.numberOfDebts)).setText(String.format("%s", Integer.toString(giveDebt(getApplicationContext()))));
        ((TextView) headerView.findViewById(R.id.numberOfCredits)).setText(String.format("%s", Integer.toString(giveCred(getApplicationContext()))));

        (headerView.findViewById(R.id.numberOfCredits)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentransaction = getFragmentManager().beginTransaction();
                fragmentransaction.replace(R.id.container, new CreditFragment());
                fragmentransaction.commit();
            }
        });


        (headerView.findViewById(R.id.numberOfDebts)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentransaction = getFragmentManager().beginTransaction();
                fragmentransaction.replace(R.id.container, new DebtFragment());
                fragmentransaction.commit();
            }
        });


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else{
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory( Intent.CATEGORY_HOME );
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if (id == R.id.nav_credit){
            FragmentTransaction fragmentransaction = getFragmentManager().beginTransaction();
            fragmentransaction.replace(R.id.container, new CreditFragment());
            fragmentransaction.commit();
        }

        else if (id == R.id.nav_debit){
            FragmentTransaction fragmentransaction = getFragmentManager().beginTransaction();
            fragmentransaction.replace(R.id.container, new DebtFragment());
            fragmentransaction.commit();
        }

        else if (id == R.id.nav_all){
            FragmentTransaction fragmentransaction = getFragmentManager().beginTransaction();
            fragmentransaction.replace(R.id.container, new AllFragment());
            fragmentransaction.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public int giveCred(Context context){
        int credCounter = 0;

        File[] listOf = context.getApplicationContext().getFilesDir().listFiles();
        for(File i: listOf){
            try{
                SingleWallet walletObject = (SingleWallet) (new ObjectInputStream(context.openFileInput(i.getName()))).readObject();

                if(walletObject.getReturnStatement().equals("To collect ")){
                    credCounter++;
                }

            }
            catch(Exception e){
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return credCounter;
    }


    public int giveDebt(Context context){
        int debtCounter = 0;
        File[] listOf = context.getApplicationContext().getFilesDir().listFiles();

        for(File i: listOf){
            try{
                SingleWallet walletObject = (SingleWallet) (new ObjectInputStream(context.openFileInput(i.getName()))).readObject();

                if(walletObject.getReturnStatement().equals("To pay ")){
                    debtCounter++;
                }
            }
            catch(Exception e){
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return debtCounter;
    }


}