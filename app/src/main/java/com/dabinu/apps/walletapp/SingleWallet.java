package com.dabinu.apps.walletapp;

import java.io.Serializable;

public class SingleWallet implements Serializable{

    String name, amount, returnStatement;
    boolean isDebt;

    public SingleWallet(String name, String amount, boolean isThisADebt){
        this.name = name;
        this.amount = amount;
        if(isThisADebt){
            returnStatement = "To pay ";
            isDebt = true;
        }
        else{
            returnStatement = "To collect ";
            isDebt = false;
        }
    }

    public String getNameOfWallet(){
        return name;
    }

    public String getAmount(){
        return amount;
    }

    public String getReturnStatement(){
        return returnStatement;
    }

    public String getPreposition(){
        if(returnStatement.equals("To pay ")){
            return " to ";
        }
        else{
            return " from ";
        }
    }

    public boolean getIsDebt(){
        return isDebt;
    }
}