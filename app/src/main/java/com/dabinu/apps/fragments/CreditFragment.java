package com.dabinu.apps.fragments;


import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.dabinu.apps.models.R;
import com.dabinu.apps.models.SingleWallet;
import java.io.File;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class CreditFragment extends android.app.Fragment {


    public CreditFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_credit, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        File[] listOf = getActivity().getFilesDir().listFiles();

        final ArrayList<String> allTheBloodyTransactionsGuy = new ArrayList<>();


        for(File i: listOf) {
            try{
                SingleWallet object = (SingleWallet) (new ObjectInputStream(getActivity().openFileInput(i.getName()))).readObject();

                if(object.getReturnStatement().equals("To collect ")){
                    allTheBloodyTransactionsGuy.add(object.getReturnStatement().concat("#").concat(object.getAmount()).concat(object.getPreposition()).concat(object.getNameOfWallet()));
                }
            }
            catch(Exception e){
                //nothing
            }
        }


        if(allTheBloodyTransactionsGuy.isEmpty()){
            (getActivity().findViewById(R.id.peekaboo)).setVisibility(View.VISIBLE);
            (getActivity().findViewById(R.id.wall)).setAlpha(0.3f);
        }
        else{
            ListView listView = (ListView) getActivity().findViewById(R.id.lizzy);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, allTheBloodyTransactionsGuy);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    (new AlertDialog.Builder(getActivity().getApplicationContext())).setMessage("Delete?")
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


        File[] listOf = getActivity().getFilesDir().listFiles();

        for(File i: listOf) {
            try{
                SingleWallet theObjectWeWant = (SingleWallet) (new ObjectInputStream(getActivity().openFileInput(i.getName()))).readObject();

                if(theObjectWeWant.getNameOfWallet().equals(name)){
                    if(theObjectWeWant.getAmount().equals(amount)){
                        if(theObjectWeWant.getIsDebt() == isDebt){
                            i.delete();
                            Toast.makeText(getActivity().getApplicationContext(), "Deleted!", Toast.LENGTH_LONG).show();
                            FragmentTransaction fragmentransaction = getFragmentManager().beginTransaction();
                            fragmentransaction.replace(R.id.container, new CreditFragment());
                            fragmentransaction.commit();
                        }
                    }
                }
            }
            catch (Exception e){
                //nothing
            }
        }

    }


}