package com.example.project2.transaction;

import java.util.ArrayList;

public class Ledger {
    ArrayList<Transaction>  mTransactions = new ArrayList<>();
    public Ledger(){

    }

    public Ledger fromFile(String filename){

    }

    public addTransaction(Transaction transaction){
        mTransactions.add(transaction);
    }
}
