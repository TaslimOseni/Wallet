package com.dabinu.apps.fragments;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dabinu.apps.activities.DisplayActivity;
import com.dabinu.apps.models.R;
import com.dabinu.apps.models.SingleWallet;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class AllFragment extends android.app.Fragment {


    public AllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_all, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Context context = getActivity().getApplicationContext();
        File[] listOf = getActivity().getFilesDir().listFiles();

        final ArrayList<String> allTheBloodyTransactionsGuy = new ArrayList<>();

        for(File i: listOf){
            try{
                SingleWallet walletObject = (SingleWallet) (new ObjectInputStream(getActivity().openFileInput(i.getName()))).readObject();
                allTheBloodyTransactionsGuy.add(walletObject.getReturnStatement().concat("#").concat(walletObject.getAmount()).concat(walletObject.getPreposition()).concat(walletObject.getNameOfWallet()));
            }
            catch(Exception e){
                //Nothing
            }
        }



        if(allTheBloodyTransactionsGuy.isEmpty()){
            (getView().findViewById(R.id.peekaboo)).setVisibility(View.VISIBLE);
            (getView().findViewById(R.id.wall)).setAlpha(0.3f);
        }
        else{
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, allTheBloodyTransactionsGuy);
            ListView listView = (ListView) getView().findViewById(R.id.lizzy);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    new AlertDialog.Builder(context).setMessage("Delete?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String[] tester = {};
                                    String[] ohho = allTheBloodyTransactionsGuy.toArray(tester);
                                    deleteWallet(ohho[i]);

                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                    return false;
                }
            });


        }
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

        File[] listOf = getActivity().getFilesDir().listFiles();

        for(File i: listOf) {
            try {
                FileInputStream fis = getActivity().openFileInput(i.getName());
                ObjectInputStream ois = new ObjectInputStream(fis);
                SingleWallet theObjectWeWant = (SingleWallet) ois.readObject();

                if(theObjectWeWant.getNameOfWallet().equals(name)){
                    if(theObjectWeWant.getAmount().equals(amount)){
                        if(theObjectWeWant.getIsDebt() == isDebt){
                            i.delete();
                            Toast.makeText(getActivity().getApplicationContext(), "Deleted!", Toast.LENGTH_LONG).show();
                            FragmentTransaction fragmentransaction = getFragmentManager().beginTransaction();
                            fragmentransaction.replace(R.id.container, new AllFragment());
                            fragmentransaction.commit();
                        }
                    }
                }
            }
            catch (Exception e){
            }
        }

    }

}