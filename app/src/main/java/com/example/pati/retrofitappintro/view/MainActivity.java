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
import android.widget.TextView;
import android.widget.Toast;

import com.example.pati.retrofitappintro.R;
import com.example.pati.retrofitappintro.model.Category;
import com.example.pati.retrofitappintro.model.Transaction;
import com.example.pati.retrofitappintro.repository.CategoryRepository;
import com.example.pati.retrofitappintro.repository.TransactionRepository;
import com.example.pati.retrofitappintro.util.ChartBuilder;
import com.example.pati.retrofitappintro.util.TimeHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.common.api.CommonStatusCodes;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private static final int RC_OCR_CAPTURE = 9003;

    private TextView budget;
    private Button scanButton, expenseButton, incomeButton, historyButton;
    private TransactionViewModel transactionViewModel;
    private TransactionRepository transactionRepository;
    private CategoryRepository categoryRepository;
    private Dialog dialog1, dialog2, dialog3;
    private LineChart lineChart;
    private BarChart barChart;
    private ArrayList<BarEntry> entries, negEntries;
    private List<Category> categoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanButton = (Button) findViewById(R.id.scan_button);
        historyButton = (Button) findViewById(R.id.history_button);
        budget = (TextView) findViewById(R.id.budget);
        expenseButton = findViewById(R.id.expense_button);
        incomeButton =findViewById(R.id.income_button);
        barChart =  findViewById(R.id.bar_chart);
        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionRepository = new TransactionRepository(getApplication());
        categoryRepository = new CategoryRepository(getApplication());
//        categoryRepository.insertCategory(new Category("Food"));
//        categoryRepository.insertCategory(new Category("Play"));
//        categoryRepository.insertCategory(new Category("Home"));
//        categoryRepository.insertCategory(new Category("Other"));
        dialog1 = new Dialog(this, transactionViewModel, "income");
        dialog2 = new Dialog(this, transactionViewModel, "expenses");
        dialog3 = new Dialog(this, transactionViewModel, "request");
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);

                startActivityForResult(intent, RC_OCR_CAPTURE);

            }

        });
        expenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.showDialog(categoriesList, null);
            }

        });

        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.showDialog(categoriesList, null);
            }

        });

        entries = new ArrayList<>();
        negEntries= new ArrayList<>();
        Calendar calendar = TimeHelper.getNow();
        try {
            transactionViewModel.getAllTransactions().observe(this, new Observer<List<Transaction>>() {
                @Override
                public void onChanged(@Nullable List<Transaction> transactions) {
                    try {
                        DecimalFormat df = new DecimalFormat("#.00");
                        df.setRoundingMode(RoundingMode.HALF_UP);
                        if(transactions.size() == 0){
                            return;
                        }
                        budget.setText(df.format(transactionRepository.getTransactionSum())+"  PLN");
                    }
                    catch (ExecutionException e) {
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

        try {
            transactionViewModel.getAllTransactionsGroupedByDays().observe(this, new Observer<List<Transaction>>() {
                @Override
                public void onChanged(@Nullable List<Transaction> transactionsGrouped) {
                    try {
                        entries.removeAll(entries);
                        negEntries.removeAll(negEntries);
                        for (int i = 0; i < transactionsGrouped.size(); i++) {
                            if(transactionsGrouped.get(i).getValue()>=0) {

                                entries.add(new BarEntry((float) i, (float) transactionsGrouped.get(i).getValue()));
                            } else {
                                negEntries.add(new BarEntry((float) i, Math.abs((float) transactionsGrouped.get(i).getValue())));
                            }
                        }
                        ChartBuilder.buildBArChart(barChart, negEntries, entries);

                    } catch (NegativeArraySizeException e) {
                        Log.i("Status", e.getMessage());
                    }  catch (NullPointerException e) {
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

        transactionViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                categoriesList = new ArrayList<>();
                categoriesList.addAll(categories);
            }
        });
    }

    public void updateEntries(ArrayList<BarEntry> newEntries) {
        this.entries = newEntries;
        lineChart.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    Log.i("value", text);
//                    transactionViewModel.insert(new Transaction(Double.parseDouble(text), TimeHelper.getActualDate(),1));
                    dialog1.showDialog(categoriesList, Double.parseDouble(text));

//                    statusMessage.setText(R.string.ocr_success);
//                    textValue.setText(text);
//                    Log.d(TAG, "Text read: " + text);
                } else {
//                    statusMessage.setText(R.string.ocr_failure);
//                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
//                statusMessage.setText(String.format(getString(R.string.ocr_error),
//                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
