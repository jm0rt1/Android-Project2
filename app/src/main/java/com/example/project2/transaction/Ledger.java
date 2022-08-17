package com.example.project2.transaction;

import java.util.ArrayList;

public class Ledger {

    ArrayList<Transaction>  mTransactions = new ArrayList<>();
    public ArrayList<Transaction> getTransactions() {
        return mTransactions;
    }

    public Ledger(){
    }

    public Ledger fromFile(String filename){
        return new Ledger();
    }

    public void addTransaction(Transaction transaction){
        mTransactions.add(transaction);
    }


}
