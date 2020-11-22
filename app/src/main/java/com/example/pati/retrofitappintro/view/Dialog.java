package com.example.pati.retrofitappintro.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pati.retrofitappintro.R;
import com.example.pati.retrofitappintro.model.Category;
import com.example.pati.retrofitappintro.model.Transaction;
import com.example.pati.retrofitappintro.repository.TransactionRepository;
import com.example.pati.retrofitappintro.service.TransactionService;
import com.example.pati.retrofitappintro.util.TimeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pati on 12.11.2018.
 */

public class Dialog {

    private final Context context;
    private final TransactionViewModel transactionViewModel;
    private final String operationType;
    private TransactionRepository transactionRepository;
    private Spinner spinner;
    private String category;
    private EditText userInput;
    private long categoryId;


    public Dialog(Context context, TransactionViewModel transactionViewModel, String operationType) {
        this.context = context;
        this.transactionViewModel = transactionViewModel;
        this.operationType = operationType;
    }

    public void showDialog(List<Category> categories) {
        final android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);
        userInput = dialog.findViewById(R.id.value);
        Button okButton = dialog.findViewById(R.id.confirmButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        List<Category> spiennerArray = new ArrayList<>();
//        spiennerArray.addAll(categories);
        spinner = dialog.findViewById(R.id.category);
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(context, android.R.layout.simple_spinner_item , spiennerArray){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
                TextView label = (TextView) super.getView(position, convertView, parent);
                label.setTextColor(Color.BLACK);
                // Then you can get the current item using the values array (Users array) and the current position
                // You can NOW reference each method you has created in your bean object (User class)
                Category item = getItem(position);
                label.setText(item.getName());

                // And finally return your dynamic (or custom) view for each spinner item
                return label;
            }

            // And here is when the "chooser" is popped up
            // Normally is the same view, but you can customize it if you want
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                TextView label = (TextView) super.getDropDownView(position, convertView, parent);
                label.setTextColor(Color.BLACK);
                Category item = getItem(position);
                label.setText(item.getName());

                return label;
            }
        };
        adapter.addAll(categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(categorySelectedListener);
        okButton.setOnClickListener(v -> onSubmitted(dialog, ParseDouble(userInput.getText().toString()), category));
        cancelButton.setOnClickListener(view -> onDismissed(dialog));
        dialog.show();
    }

   private double ParseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch(Exception e) {
                return -1;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        }
        else return 0;
    }
    private void onSubmitted(final DialogInterface dialog, Double value, final String category) {
        int isAsk= 0;
        if(value==0){
            Toast.makeText(context.getApplicationContext(),"Insert value", Toast.LENGTH_SHORT).show();
            return ;
        }
        Intent intentService = new Intent(context.getApplicationContext(), TransactionService.class);
        context.startService(intentService);
            if (operationType.equals("expenses")) {
                value = -value;
            }
                transactionViewModel.insert(new Transaction(value, TimeHelper.getNow().getTimeInMillis(), categoryId));
        dialog.dismiss();
    }
    private void onDismissed(final DialogInterface dialog) {
        dialog.dismiss();
    }
    AdapterView.OnItemSelectedListener categorySelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> spinner, View container, int position, long id) {
            categoryId = ((Category) spinner.getItemAtPosition(position)).getCategoryId();
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    };
}
