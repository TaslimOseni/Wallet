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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabinu.apps.walletapp.MainActivity;
import com.dabinu.apps.walletapp.R;
import com.dabinu.apps.walletapp.SingleWallet;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class Credits extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    Intent goToMommy, goToDebitSide, createNewField;
    ArrayAdapter<String> arrayAdapter;
    RelativeLayout relativeLayoutCredit;
    TextView nDebitM, nCreditM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
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


        goToDebitSide = new Intent(getApplicationContext(), DebtList.class);
        goToMommy = new Intent(getApplicationContext(), MainActivity.class);
        createNewField = new Intent(getApplicationContext(), NewField.class);


        relativeLayoutCredit = (RelativeLayout) findViewById(R.id.motherViewCredit);

        View headerView = navigationView.getHeaderView(0);
        nDebitM = (TextView) headerView.findViewById(R.id.numberOfDebts);
        nCreditM = (TextView) headerView.findViewById(R.id.numberOfCredits);


        File[] listOf = getFilesDir().listFiles();

        ArrayList<String> allTheBloodyTransactionsGuy = new ArrayList<String>();
        int debtCounter = 0;
        int credCounter = 0;


        for (int i = 0; i < listOf.length; i++) {
            try {
                FileInputStream fis = openFileInput(listOf[i].getName());
                ObjectInputStream ois = new ObjectInputStream(fis);
                SingleWallet object = (SingleWallet) ois.readObject();
                if(object.getIsDebt().equals("To pay ")){
                    debtCounter++;
                }
                else if(object.getIsDebt().equals("To collect ")){
                    credCounter++;
                }
                if(object.getIsDebt().equals("To pay ")){
                    continue;
                }
                else{
                    allTheBloodyTransactionsGuy.add(object.getIsDebt().concat("#").concat(object.getAmount()).concat(object.getToOrFrom()).concat(object.getNameOfWallet()));
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
            relativeLayoutCredit.addView(nothingSir);
        }
        else{
            ListView listView = new ListView(this);
            listView.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            listView.setPadding(50, 30, 50, 30);
            relativeLayoutCredit.addView(listView);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allTheBloodyTransactionsGuy);
            listView.setAdapter(arrayAdapter);
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
        getMenuInflater().inflate(R.menu.credit_list, menu);
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
        if (id == R.id.nav_all){
            startActivity(goToMommy);
        }

          else if (id == R.id.nav_debit) {
            startActivity(goToDebitSide);
        }

//          else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}