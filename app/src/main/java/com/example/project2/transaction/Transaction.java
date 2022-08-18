package com.example.project2.transaction;

import com.example.project2.R;

public class Transaction {



    String mPayee;
    String mCategory;
    float mAmount;
    String mType;

    public Transaction(String payee, String category, String type, float amount){
        mPayee = payee;
        mCategory = category;
        mType = type;
        mAmount = amount;
    }
    public String getPayee() {
        return mPayee;
    }

    public String getCategory() {
        return mCategory;
    }

    public float getAmount() {
        return mAmount;
    }

    public String getType() {
        return mType;
    }
}
