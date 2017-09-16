package com.dabinu.apps.walletapp;

import java.io.Serializable;

public class SingleWallet implements Serializable{

    String name, amount, month, day;
    String isDebt;

    public SingleWallet(String name, String amount, boolean isThisADebt){
        this.name = name;
        this.amount = amount;
        if(isThisADebt){
            isDebt = "To pay ";
        }
        else{
            isDebt = "To collect ";
        }
    }

    public String getNameOfWallet(){
        return name;
    }

    public String getAmount(){
        return amount;
    }

    public String getIsDebt(){
        return isDebt;
    }

    public String getToOrFrom(){
        if(isDebt.equals("To pay ")){
            return " to ";
        }
        else{
            return " from ";
        }
    }


}
