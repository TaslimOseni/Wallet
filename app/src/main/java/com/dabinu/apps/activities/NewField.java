package com.dabinu.apps.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import com.dabinu.apps.wallets.GeneralActivity;
import com.dabinu.apps.models.R;
import com.dabinu.apps.models.SingleWallet;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class NewField extends AppCompatActivity implements Serializable{


    RadioButton isDebt, isCredit;
    EditText name, amount;
    FloatingActionButton save;
    ImageButton cancel;
    boolean areWeOwing;
    LinearLayout newn;


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Discard?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewField.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_field);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        final Intent backToMomma = new Intent(this, GeneralActivity.class);



        isDebt = (RadioButton) findViewById(R.id.isDebt);
        isCredit = (RadioButton) findViewById(R.id.isCredit);
        name = (EditText) findViewById(R.id.name);
        amount = (EditText) findViewById(R.id.amount);
        save = (FloatingActionButton) findViewById(R.id.save);
        cancel = (ImageButton) findViewById(R.id.cancel);
        newn = (LinearLayout) findViewById(R.id.dreamer);


        isDebt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isDebt.isChecked()){
                    if(isCredit.isChecked()){
                        isCredit.setChecked(false);
                    }
                }
            }
        });

        isCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isCredit.isChecked()){
                    if(isDebt.isChecked()){
                        isDebt.setChecked(false);
                    }
                }
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDebt.isChecked() || isCredit.isChecked()){
                    if(!name.getText().toString().trim().equals("")){
                        if(!amount.getText().toString().trim().equals("")){
                            if(isDebt.isChecked()){
                                areWeOwing = true;
                            }
                            else if(isCredit.isChecked()){
                                areWeOwing = false;
                            }
                            try{
                                FileOutputStream fos = openFileOutput(Long.toString(System.currentTimeMillis()), Context.MODE_APPEND);
                                ObjectOutputStream oos = new ObjectOutputStream(fos);
                                oos.writeObject(new SingleWallet(name.getText().toString().trim(),
                                        amount.getText().toString().trim(),
                                        areWeOwing));
                                oos.close();
                                fos.close();
                                Toast.makeText(getApplicationContext(), "Successful!!!", Toast.LENGTH_LONG).show();
                                startActivity(backToMomma);
                            }
                            catch (Exception e){
                                Toast.makeText(getApplicationContext(), "Failed, try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Amount field empty!!!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Name field empty!!!", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Choose between credit and debit!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}