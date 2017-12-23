package com.dabinu.apps.fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dabinu.apps.models.R;
import com.dabinu.apps.models.SingleWallet;
import com.dabinu.apps.activities.NewField;
import com.dabinu.apps.toolClasses.TheAlarmReceiverGuy;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import static com.dabinu.apps.models.R.id.nav_view;



public class GeneralActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Serializable{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("General");
        setSupportActionBar(toolbar);

        overridePendingTransition(0, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, TheAlarmReceiverGuy.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(GeneralActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(nav_view);
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


        if(allTheBloodyTransactionsGuy.isEmpty()){
            (findViewById(R.id.peekaboo)).setVisibility(View.VISIBLE);
            (findViewById(R.id.wall)).setAlpha(0.3f);
        }
        else{
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allTheBloodyTransactionsGuy);
            ListView listView = (ListView) findViewById(R.id.lizzy);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    new AlertDialog.Builder(context).setMessage("Delete?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String[] tester = {};
                                    String[] ohho = allTheBloodyTransactionsGuy.toArray(tester);
                                    deleteWallet(ohho[position]);

                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });


        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if (id == R.id.nav_credit){
            startActivity(new Intent(getApplicationContext(), CreditActivity.class));
        }

        else if (id == R.id.nav_debit){
            startActivity(new Intent(getApplicationContext(), DebtActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void deleteWallet(String displayMessage){
        String name = "";
        String amount = "";
        boolean isDebt = false;
        int startFromHereNextTime;

        char[] brakata = displayMessage.toCharArray();

        if(brakata[3] == 'c'){
            isDebt = false;
        }
        else if(brakata[3] == 'p'){
            isDebt = true;
        }


        if(isDebt){
            ArrayList<String> amountArray = new ArrayList<>();
            ArrayList<String> nameArray = new ArrayList<>();

            for(int i = 8; i < brakata.length; i++){
                amountArray.add(Character.toString(brakata[i]));
                if(brakata[i + 1] == ' '){
                    startFromHereNextTime = i + 5;
                    for(int j = startFromHereNextTime; j < brakata.length; j++){
                        nameArray.add(Character.toString(brakata[j]));
                    }
                    break;
                }
            }
            for(String a: amountArray){
                amount += (a);
            }
            for(String a: nameArray){
                name += (a);
            }

        }

        else{
            ArrayList<String> amountArray = new ArrayList<>();
            ArrayList<String> nameArray = new ArrayList<>();
            for(int i = 12; i < brakata.length; i++){
                amountArray.add(Character.toString(brakata[i]));
                if(brakata[i + 1] == ' '){
                    startFromHereNextTime = i + 7;
                    for(int j = startFromHereNextTime; j < brakata.length; j++){
                        nameArray.add(Character.toString(brakata[j]));
                    }
                    break;
                }
            }
            for(String a: amountArray){
                amount += (a);
            }
            for(String a: nameArray){
                name += (a);
            }
        }

        File[] listOf = getFilesDir().listFiles();

        for(File i: listOf) {
            try {
                FileInputStream fis = openFileInput(i.getName());
                ObjectInputStream ois = new ObjectInputStream(fis);
                SingleWallet theObjectWeWant = (SingleWallet) ois.readObject();

                if(theObjectWeWant.getNameOfWallet().equals(name)){
                    if(theObjectWeWant.getAmount().equals(amount)){
                        if(theObjectWeWant.getIsDebt() == isDebt){
                            i.delete();
                            Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(this, GeneralActivity.class));
                        }
                    }
                }
            }
            catch (Exception e){
            }
        }

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