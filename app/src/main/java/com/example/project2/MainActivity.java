package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
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
    TextView mBalanceTextView;
    Ledger mLedger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTransactionTable = (TableLayout)findViewById(R.id.transaction_table);
        mBalanceTextView = findViewById(R.id.balance_text_view);
        initializeLedger();
        refresh();

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

    public void clear(View v){
        mLedger.clear();
        refresh();
    }

    public void showAddTransactionDialog(View v) {
        FragmentManager fm = getSupportFragmentManager();
        TransactionEntryFragment alertDialog = TransactionEntryFragment.newInstance("Some title", "2");
        alertDialog.show(fm, "fragment_alert");
    }

    public void addTransaction(Transaction transaction){
        mLedger.addTransaction(transaction);
        refresh();
        try {
            mLedger.save();
        } catch (IOException e) {
            Log.e(TAG,e.toString());
        }
    }
    private void refresh(){
        refreshBalance();
        refreshTable();
    }
    private void refreshBalance(){
        mBalanceTextView.setText("$" + String.valueOf(mLedger.getBalance()));
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
        typeCell.setBackgroundColor(getColor(R.color.white));
        payeeCell.setBackgroundColor(getColor(R.color.white));
        categoryCell.setBackgroundColor(getColor(R.color.white));
        amountCell.setBackgroundColor(getColor(R.color.white));
        typeCell.setPadding(2,1,1,1);
        payeeCell.setPadding(1,1,1,1);
        categoryCell.setPadding(1,1,1,1);
        amountCell.setPadding(1,1,2,1);
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

            typeCell.setBackgroundColor(getColor(R.color.white));
            payeeCell.setBackgroundColor(getColor(R.color.white));
            categoryCell.setBackgroundColor(getColor(R.color.white));
            amountCell.setBackgroundColor(getColor(R.color.white));

            int Value = 5;
            typeCell.setPadding(Value, Value, Value, Value);
            payeeCell.setPadding(Value, Value, Value, Value);
            categoryCell.setPadding(Value, Value, Value, Value);
            amountCell.setPadding(Value, Value, Value, Value);
//            LinearLayout.LayoutParams params = new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(1,0,1,0);
//            typeCell.setLayoutParams(params);
//            payeeCell.setLayoutParams(params);
//            categoryCell.setLayoutParams(params);
//            amountCell.setLayoutParams(params);


            tr.addView(typeCell);

            tr.addView(payeeCell);
            tr.addView(categoryCell);
            tr.addView(amountCell);
            TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams();

            Value = 2;
            tableRowParams.setMargins(Value, Value, Value, Value);
            tr.setLayoutParams(tableRowParams);
            mTransactionTable.addView(tr);
        }
    }

}