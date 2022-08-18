package com.example.project2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.project2.transaction.Transaction;

import java.lang.reflect.Array;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TransactionEntryFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "TransactionEntry";
    private Spinner mCategorySpinner;
    private Spinner mTypeSpinner;
    private Button mButton;
    private ImageView mImageView;
    ActivityResultLauncher<Intent> launcher;


    int SELECT_PICTURE = 200;
    private EditText mPayeeEditText;
    private EditText mAmountEditText;
//    private EditText mPayeeEditText;

    MainActivity mMainActivity;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static TransactionEntryFragment newInstance(String param1,String param2) {
        TransactionEntryFragment fragment = new TransactionEntryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TransactionEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mMainActivity = (MainActivity)getActivity();


        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        mImageView.setImageBitmap(photo);
    }

    // this function is triggered when
    // the Select Image Button is clicked
    public void imageChooser(View v) {


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        launcher.launch(takePictureIntent);

    }

    private void initActivityResultLauncher(View v) {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK)
                        {
                            Intent data = result.getData();
                            ImageView image = (ImageView) v.findViewById(R.id.imageView);
                            Bitmap b = (Bitmap) data.getExtras().get("data");
                            image.setImageBitmap(b);
                        }
                    }
                });
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage("Add New Transaction");

        // Edited: Overriding onCreateView is not necessary in your case
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_transaction_entry, null);
        builder.setView(view);

        try{
            mCategorySpinner = view.findViewById(R.id.category_spinner);
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.categories, android.R.layout.simple_spinner_dropdown_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mCategorySpinner.setAdapter(spinnerAdapter);

            mTypeSpinner = view.findViewById(R.id.type_spinner);
            ArrayAdapter<CharSequence> spinnerAdapter2 = ArrayAdapter.createFromResource(getContext(), R.array.types, android.R.layout.simple_spinner_dropdown_item);
            spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mTypeSpinner.setAdapter(spinnerAdapter2);


            mAmountEditText = view.findViewById(R.id.amount_edit_text);
            mPayeeEditText = view.findViewById(R.id.payee_edit_text);

            mButton = view.findViewById(R.id.button);
            mButton.setOnClickListener(this::imageChooser);
            mImageView = view.findViewById(R.id.imageView);
            initActivityResultLauncher(view);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            throw e;
        }

        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try{
                    Transaction transaction = new Transaction(mPayeeEditText.getText().toString(),
                            mCategorySpinner.getSelectedItem().toString(),
                            mTypeSpinner.getSelectedItem().toString(),
                            Float.parseFloat(mAmountEditText.getText().toString()));
                    mMainActivity.addTransaction(transaction);
                } catch (Exception e){
                    Log.e(TAG, e.toString());
                    dialog.dismiss();
                    mMainActivity.showAddTransactionError();

                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return builder.create();
    }


}