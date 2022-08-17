package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.project2.transaction.Ledger;
import com.example.project2.transaction.Transaction;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    TableLayout mTransactionTable;
    Ledger mLedger = new Ledger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTransactionTable = (TableLayout)findViewById(R.id.transaction_table);


    }

    public void showAddTransactionDialog(View v) {
        FragmentManager fm = getSupportFragmentManager();
        TransactionEntryFragment alertDialog = TransactionEntryFragment.newInstance("Some title", "2");
        alertDialog.show(fm, "fragment_alert");
    }

    public void addTransaction(Transaction transaction){
        mLedger.addTransaction(transaction);
        refreshTable();
    }

    private void refreshTable() {
        mTransactionTable.removeAllViews();
        mTransactionTable.setStretchAllColumns(true);
        mTransactionTable.bringToFront();
        for(int i = 0; i < mLedger.getTransactions().size(); i++){

            Transaction t =  mLedger.getTransactions().get(i);
            TableRow tr =  new TableRow(this);

            TextView typeCell = new TextView(this);
            typeCell.setText(t.getType());

            TextView payeeCell= new TextView(this);
            payeeCell.setText(t.getPayee());

            TextView categoryCell = new TextView(this);
            categoryCell.setText(t.getCategory());

            TextView amountCell = new TextView(this);
            amountCell.setText(String.valueOf(t.getAmount()));

            tr.addView(typeCell);
            tr.addView(payeeCell);
            tr.addView(categoryCell);
            tr.addView(amountCell);

            mTransactionTable.addView(tr);
        }
    }

}