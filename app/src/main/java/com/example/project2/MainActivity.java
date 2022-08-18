package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.project2.transaction.Ledger;
import com.example.project2.transaction.Transaction;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "MainActivity";
    TableLayout mTransactionTable;
    Ledger mLedger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTransactionTable = (TableLayout)findViewById(R.id.transaction_table);

        initializeLedger();
        refreshTable();

    }

    private void initializeLedger() {
        boolean ledgerFound = false;
        try{
            mLedger = Ledger.loadFromFile(getApplicationContext(), "file.json");
            ledgerFound = true;

        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
        if (!ledgerFound){
            mLedger = new Ledger(getApplicationContext(),"file.json");
        }
    }

    public void showAddTransactionDialog(View v) {
        FragmentManager fm = getSupportFragmentManager();
        TransactionEntryFragment alertDialog = TransactionEntryFragment.newInstance("Some title", "2");
        alertDialog.show(fm, "fragment_alert");
    }

    public void addTransaction(Transaction transaction){
        mLedger.addTransaction(transaction);
        refreshTable();
        try {
            mLedger.save();
        } catch (IOException e) {
            Log.e(TAG,e.toString());
        }
    }

    private void refreshTable() {
        mTransactionTable.removeAllViews();
        mTransactionTable.setStretchAllColumns(true);
        mTransactionTable.bringToFront();
        TableRow tr =  new TableRow(this);

        TextView typeCell = new TextView(this);
        typeCell.setText(R.string.type);

        TextView payeeCell= new TextView(this);
        payeeCell.setText(R.string.payee);

        TextView categoryCell = new TextView(this);
        categoryCell.setText(R.string.category);

        TextView amountCell = new TextView(this);
        amountCell.setText(R.string.amount);

        tr.addView(typeCell);
        tr.addView(payeeCell);
        tr.addView(categoryCell);
        tr.addView(amountCell);
        mTransactionTable.addView(tr);

        for(int i = 0; i < mLedger.getTransactions().size(); i++){

            Transaction t =  mLedger.getTransactions().get(i);
            tr =  new TableRow(this);

            typeCell = new TextView(this);
            typeCell.setText(t.getType());

            payeeCell= new TextView(this);
            payeeCell.setText(t.getPayee());

            categoryCell = new TextView(this);
            categoryCell.setText(t.getCategory());

            amountCell = new TextView(this);
            amountCell.setText(String.valueOf(t.getAmount()));

            tr.addView(typeCell);
            tr.addView(payeeCell);
            tr.addView(categoryCell);
            tr.addView(amountCell);

            mTransactionTable.addView(tr);
        }
    }

}