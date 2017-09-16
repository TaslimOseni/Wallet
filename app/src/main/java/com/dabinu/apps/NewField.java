package com.dabinu.apps;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import com.dabinu.apps.walletapp.MainActivity;
import com.dabinu.apps.walletapp.R;
import com.dabinu.apps.walletapp.SingleWallet;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class NewField extends AppCompatActivity implements Serializable{

    RadioButton isDebt, isCredit;
    EditText name, amount;
    Button save, cancel;
    ArrayAdapter<CharSequence> mnt, dys;
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


        final Intent backToMomma = new Intent(this, MainActivity.class);;



        isDebt = (RadioButton) findViewById(R.id.isDebt);
        isCredit = (RadioButton) findViewById(R.id.isCredit);
        name = (EditText) findViewById(R.id.name);
        amount = (EditText) findViewById(R.id.amount);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
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