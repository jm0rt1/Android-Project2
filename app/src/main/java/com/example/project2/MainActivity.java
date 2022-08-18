package com.example.project2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
        createNotificationChannel();
    }
    private void createNotificationChannel()
    {
        //only create the notification channel on API 26+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("alert", "name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            refreshTable();
        }
    }
    private void refreshBalance(){
        mBalanceTextView.setText("$" + String.valueOf(mLedger.getBalance()));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
//            LinearLayout.LayoutParams params = new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(1,0,1,0);
//
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
            final int finali = i;
            tr.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    //show transaction in standalone activity, with attachment
                    Log.i(TAG, "Row " + String.valueOf(finali));
                    Toast toast = Toast.makeText(getApplicationContext(),"Row " + String.valueOf(finali),Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }


    public void showAddTransactionError(){
        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"alert");

        builder.setContentTitle("Error Adding Transaction");
        builder.setContentText("Please ensure all data is filled in");
        builder.setSilent(true);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        manager.notify(1, builder.build());


        AlertDialog.Builder builder1 = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder1.setTitle("Could not add transaction");
        builder1.setMessage("Be sure all sections were filled in");
        builder1.show();
    }

}