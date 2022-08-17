package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.project2.transaction.Transaction;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    TableLayout mTransactionTable;
    ArrayList<Transaction> Transaction

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
        mTransactionTable.setStretchAllColumns(true);
        mTransactionTable.bringToFront();
        for(int i = 0; i < 10; i++){
            TableRow tr =  new TableRow(this);
            TextView payeeCell = new TextView(this);
            payeeCell.setText("1");
            TextView c2 = new TextView(this);
            c2.setText(String.valueOf(2));
            TextView c3 = new TextView(this);
            c3.setText(String.valueOf(100));
            tr.addView(payeeCell);
            tr.addView(c2);
            tr.addView(c3);
            mTransactionTable.addView(tr);
        }
    }

}