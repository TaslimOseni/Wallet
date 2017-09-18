package com.dabinu.apps;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabinu.apps.walletapp.GeneralActivity;
import com.dabinu.apps.walletapp.R;
import com.dabinu.apps.walletapp.SingleWallet;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class DebtActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    Intent goToMommy, goToCreditSide, createNewField, stayHereDebt;
    ArrayAdapter<String> arrayAdapter;
    RelativeLayout relativeLayoutDebt;
    TextView nDebitM, nCreditM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(0, 0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(createNewField);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        goToCreditSide = new Intent(getApplicationContext(), CreditActivity.class);
        goToMommy = new Intent(getApplicationContext(), GeneralActivity.class);
        stayHereDebt = new Intent(getApplicationContext(), DebtActivity.class);
        createNewField = new Intent(getApplicationContext(), NewField.class);

        relativeLayoutDebt = (RelativeLayout) findViewById(R.id.motherViewDebit);


        final AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);


        View headerView = navigationView.getHeaderView(0);
        nDebitM = (TextView) headerView.findViewById(R.id.numberOfDebts);
        nCreditM = (TextView) headerView.findViewById(R.id.numberOfCredits);


        File[] listOf = getFilesDir().listFiles();

        final ArrayList<String> allTheBloodyTransactionsGuy = new ArrayList<String>();
        int debtCounter = 0;
        int credCounter = 0;

        for (int i = 0; i < listOf.length; i++) {
            try {
                FileInputStream fis = openFileInput(listOf[i].getName());
                ObjectInputStream ois = new ObjectInputStream(fis);
                SingleWallet object = (SingleWallet) ois.readObject();
                if(object.getReturnStatement().equals("To pay ")){
                    debtCounter++;
                }
                else if(object.getReturnStatement().equals("To collect ")){
                    credCounter++;
                }
                if(object.getReturnStatement().equals("To collect ")){
                    continue;
                }
                else{
                    allTheBloodyTransactionsGuy.add(object.getReturnStatement().concat("#").concat(object.getAmount()).concat(object.getPreposition()).concat(object.getNameOfWallet()));
                }
            }
            catch (Exception e) {
                continue;
            }
        }

        nDebitM.setText(Integer.toString(debtCounter));
        nCreditM.setText(Integer.toString(credCounter));


        if(allTheBloodyTransactionsGuy.isEmpty()){
            TextView nothingSir = new TextView(this);
            nothingSir.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            nothingSir.setText("Nothing to display");
            nothingSir.setTextSize(30);
            nothingSir.setAllCaps(true);
            nothingSir.setPadding(80, 700, 0, 0);
            nothingSir.setAlpha(0.7f);
            relativeLayoutDebt.addView(nothingSir);
        }
        else{
            ListView listView = new ListView(this);
            listView.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            listView.setPadding(50, 30, 50, 30);
            relativeLayoutDebt.addView(listView);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allTheBloodyTransactionsGuy);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                    alBuilder.setMessage("Delete?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String[] tester = {};
                                    String[] ohho = allTheBloodyTransactionsGuy.toArray(tester);
                                    deleteWallet(ohho[position]);

                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                    return false;
                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.debt_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_credit){
            startActivity(goToCreditSide);
        }

        else if (id == R.id.nav_all){
            startActivity(goToMommy);
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


            ArrayList<String> amountArray = new ArrayList<String>();
            ArrayList<String> nameArray = new ArrayList<String>();

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



        File[] listOf = getFilesDir().listFiles();

        for(int i = 0; i < listOf.length; i++) {
            try {
                FileInputStream fis = openFileInput(listOf[i].getName());
                ObjectInputStream ois = new ObjectInputStream(fis);
                SingleWallet theObjectWeWant = (SingleWallet) ois.readObject();

                if(theObjectWeWant.getNameOfWallet().equals(name)){
                    if(theObjectWeWant.getAmount().equals(amount)){
                        if(theObjectWeWant.getIsDebt() == isDebt){
                            listOf[i].delete();
                            Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_LONG).show();
                            startActivity(stayHereDebt);
                        }
                    }
                }
            }
            catch (Exception e){
                continue;
            }
        }

    }

}