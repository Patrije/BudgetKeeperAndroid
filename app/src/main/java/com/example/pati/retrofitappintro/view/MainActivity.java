package com.example.pati.retrofitappintro.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pati.retrofitappintro.R;
import com.example.pati.retrofitappintro.model.Transaction;
import com.example.pati.retrofitappintro.repository.TransactionDao;
import com.example.pati.retrofitappintro.repository.TransactionDatabase;
import com.example.pati.retrofitappintro.repository.TransactionRepository;
import com.example.pati.retrofitappintro.util.TimeHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private TextView budget;
    private EditText loginEdit, balanceEdit;
    private Button askButton, expenseButton, incomeButton, historyButton;
    private TransactionViewModel transactionViewModel;
    private TransactionRepository transactionRepository;
    private Dialog dialog1, dialog2, dialog3;
    private LineChart lineChart;
    private ArrayList<Entry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askButton = (Button) findViewById(R.id.ask_button);
        historyButton = (Button) findViewById(R.id.history_button);
        budget = (TextView) findViewById(R.id.budget);
        expenseButton = (Button) findViewById(R.id.expense_button);
        incomeButton = (Button) findViewById(R.id.income_button);
        lineChart = (LineChart) findViewById(R.id.bar_chart);

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionRepository = new TransactionRepository(getApplication());
        dialog1 = new Dialog(this, transactionViewModel, "income");
        dialog2 = new Dialog(this, transactionViewModel, "expanses");
        dialog3 = new Dialog(this, transactionViewModel, "request");

        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog3.showDialog();
            }

        });
        expenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.showDialog();
            }

        });

        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.showDialog();
            }

        });

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setBackgroundColor(ColorTemplate.getHoloBlue());
        lineChart.setAlpha(0.8f);
        entries = new ArrayList<>();
        TextView title = (TextView) findViewById(R.id.title);
        Calendar calendar = TimeHelper.getNow();
//        LineDataSet set = new LineDataSet(entries,"datas");
//        set.setColor(Color.BLACK);
//        ArrayList<ILineDataSet>dataSets= new ArrayList<>();
//        dataSets.add(set);
//        LineData data= new LineData(dataSets);
//        lineChart.setData(data);
//     Collections.sort(entries, new EntryXComparator());
        try {
            transactionViewModel.getAllTransactions().observe(this, new Observer<List<Transaction>>() {
                @Override
                public void onChanged(@Nullable List<Transaction> transactions) {
                    ArrayList<Entry> entries2 = new ArrayList<>();

                    try {
                        budget.setText(transactionRepository.getTransactionSum().toString());
                        String string = "";
                        for (int i = 0; i < transactions.size(); i++) {
//    string=string+transactions.get(i).getValue()+"";
                            entries.add(new Entry((float) i, (float) transactionRepository.getAllTransactionASC().get(i).getValue()));
                        }
//updateEntries(entries2);
                        LineDataSet set = new LineDataSet(entries, "datas");
//                        set.setColor(Color.BLACK);
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set);
                        LineData data = new LineData(dataSets);
                        lineChart.setData(data);
                        lineChart.notifyDataSetChanged();
//title.setText(string);
//                        set.notifyDataSetChanged();
//                        lineChart.notifyDataSetChanged();
//                        Collections.sort(entries, new EntryXComparator());
                    } catch (NegativeArraySizeException e) {
                        Log.i("Statuss", e.getMessage());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        budget.setText("00.00");
                    }
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(intent);
        });
    }

    public void updateEntries(ArrayList<Entry> newEntries) {
        this.entries = newEntries;
        lineChart.notifyDataSetChanged();
    }
}