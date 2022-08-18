package com.example.project2.transaction;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ledger {
    public final String TAG = "Ledger";
    String mFilename;
    Context mContext;


    private ArrayList<Transaction>  mTransactions = new ArrayList<>();
    public ArrayList<Transaction> getTransactions() {
        return mTransactions;
    }
    private void setTransactions(ArrayList<Transaction> transactions) {
        this.mTransactions = transactions;
    }

    public Ledger(Context context, String filename){
        mFilename = filename;
        mContext = context;

    }

    public void clear(){
        mTransactions = new ArrayList<>();
        try{
            save();
        } catch (Exception e){
            Log.e(TAG,e.toString() );
        }
    }

    public static Ledger loadFromFile(Context context, String filename) throws IOException {
        File file = new File(context.getFilesDir(), filename);
        if (!file.exists()){
            throw new IOException(file +" does not exist");
        }
        FileInputStream inputStream = new FileInputStream(file);
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        String jsonString = new String(buffer, "UTF-8");

        //Log.i("data", jsonFileString);
        Gson gson = new Gson();
        Type listUserType = new TypeToken<ArrayList<Transaction>>() {
        }.getType();
        ArrayList<Transaction> transactions = gson.fromJson(jsonString, listUserType);
        Ledger ledger = new Ledger(context,filename);
        ledger.setTransactions(transactions);
        return ledger;

    }



    public void save() throws IOException {

        File file = new File(mContext.getFilesDir(), mFilename);
        if (!file.exists()){
            file.createNewFile();
        }

        String fileContents = new Gson().toJson(mTransactions);


        try (FileOutputStream fos = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());
        }
    }


    public void addTransaction(Transaction transaction){
        mTransactions.add(transaction);
    }

    public float getBalance(){
        float balance = 0;
        for (int i = 0; i<mTransactions.size();i++){
            balance += mTransactions.get(i).mAmount;
        }
        return balance;
    }

}
