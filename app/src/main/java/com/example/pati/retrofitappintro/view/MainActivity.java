package com.example.pati.retrofitappintro.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.pati.retrofitappintro.R;
import com.example.pati.retrofitappintro.model.Transaction;
import com.example.pati.retrofitappintro.util.TimeHelper;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private TextView budget;
    private EditText loginEdit, balanceEdit;
    private Button askButton, expenseButton, incomeButton, historyButton;
    private TransactionViewModel transactionViewModel;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askButton = (Button) findViewById(R.id.ask_button);
        historyButton = (Button) findViewById(R.id.history_button);
        budget = (TextView) findViewById(R.id.budget);
        expenseButton = (Button) findViewById(R.id.expense_button);
        incomeButton = (Button) findViewById(R.id.income_button);

        try {
            transactionViewModel = new TransactionViewModel(this.getApplication());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dialog= new Dialog(this, transactionViewModel,"dgs");
        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.showDialog();
            }

        });
        expenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.showDialog();
            }

        });
        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.showDialog();
            }

        });

        Double sum;
        if (transactionViewModel.getTransactionSum() != null) {
            sum = transactionViewModel.getTransactionSum();
        } else {
            sum = 0D;
        }
        budget.setText(sum.toString());
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(intent);
        });
    }
    public void setSum(){
        Double sum;
        if (transactionViewModel.getTransactionSum() != null) {
            sum = transactionViewModel.getTransactionSum();
        } else {
            sum = 0D;
        }
        budget.setText(sum.toString());
    }



}
