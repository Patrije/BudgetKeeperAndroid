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
import com.example.pati.retrofitappintro.util.TimeHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.ArrayList;
import java.util.Calendar;
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
    private ArrayList<Entry> entries, negEntries;
    private List<Category> categoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanButton = (Button) findViewById(R.id.scan_button);
        historyButton = (Button) findViewById(R.id.history_button);
        budget = (TextView) findViewById(R.id.budget);
        expenseButton = (Button) findViewById(R.id.expense_button);
        incomeButton = (Button) findViewById(R.id.income_button);
        lineChart = (LineChart) findViewById(R.id.bar_chart);
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
                dialog2.showDialog(categoriesList);
            }

        });

        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.showDialog(categoriesList);
            }

        });

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setBackgroundColor(ColorTemplate.getHoloBlue());
        lineChart.setAlpha(0.8f);
        entries = new ArrayList<>();
        negEntries= new ArrayList<>();
        Calendar calendar = TimeHelper.getNow();
        try {
            transactionViewModel.getAllTransactions().observe(this, new Observer<List<Transaction>>() {
                @Override
                public void onChanged(@Nullable List<Transaction> transactions) {
                    try {
                        budget.setText(categoryRepository.getCategoryNameById(1)+"  PLN");
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
            transactionViewModel.getAllTransactions().observe(this, new Observer<List<Transaction>>() {
                @Override
                public void onChanged(@Nullable List<Transaction> transactions) {
                    try {
                       // budget.setText(transactionRepository.getTransactionSum().toString()+"  PLN");
                        String string = "";
                        entries.removeAll(entries);
                        negEntries.removeAll(negEntries);
                        for (int i = 0; i < transactions.size(); i++) {
                            if(transactions.get(i).getValue()>=0) {
                                entries.add(new Entry((float) i, (float) transactions.get(i).getValue()));
                            } else {
                                negEntries.add(new Entry((float) i, Math.abs((float) transactions.get(i).getValue())));
                            }
                        }

                        LineDataSet set = new LineDataSet(entries, "Incomes");
                        LineDataSet negSet = new LineDataSet(negEntries, "Expenses");
                        negSet.setColor(Color.RED);
                        negSet.setLineWidth(3.0f);
                        negSet.setDrawFilled(true);
                        negSet.setFillColor(Color.RED);
                        negSet.setFillAlpha(210);
                        set.setColor(Color.GREEN);
                        set.setLineWidth(3.0f);
                        set.setDrawFilled(true);
                        set.setFillColor(Color.GREEN);
                        set.setFillAlpha(190);
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set);
                        dataSets.add(negSet);
                        LineData data = new LineData(dataSets);
                        lineChart.setData(data);
                        lineChart.notifyDataSetChanged();
                        lineChart.invalidate();
                    } catch (NegativeArraySizeException e) {
                        Log.i("Status", e.getMessage());
                    }  catch (NullPointerException e) {
                       // budget.setText("00.00");
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

    public void updateEntries(ArrayList<Entry> newEntries) {
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
                    transactionViewModel.insert(new Transaction(Double.parseDouble(text), TimeHelper.getNow().getTimeInMillis(),1));

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
