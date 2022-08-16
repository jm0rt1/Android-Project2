package com.example.project2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    AlertDialog.Builder mTransactionEntryDialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void showDialog(View v) {
        FragmentManager fm = getSupportFragmentManager();
        TransactionEntryFragment alertDialog = TransactionEntryFragment.newInstance("Some title", "2");
        alertDialog.show(fm, "fragment_alert");
    }

}