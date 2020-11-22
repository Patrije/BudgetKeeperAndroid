package com.example.pati.retrofitappintro.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.example.pati.retrofitappintro.R;
import com.example.pati.retrofitappintro.converter.TransactionDsToTransactionDtoConverter;
import com.example.pati.retrofitappintro.model.Transaction;
import com.example.pati.retrofitappintro.model.TransactionDto;
import com.example.pati.retrofitappintro.service.TransactionService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.SneakyThrows;

public class HistoryActivity extends AppCompatActivity {

    private TransactionViewModel transactionViewModel;
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private TransactionDsToTransactionDtoConverter converter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        customAdapter = new CustomAdapter(getApplicationContext(), new ArrayList<TransactionDto>());
        converter = new TransactionDsToTransactionDtoConverter(getApplication());
        recyclerView.setAdapter(customAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        Button buttonDelete = (Button) findViewById(R.id.deleteButton);
        buttonDelete.setOnClickListener(v -> {
            transactionViewModel.deleteTransactions();
            customAdapter.clear();
            Intent intentService = new Intent(getApplicationContext(), TransactionService.class);
            startService(intentService);
        });
        try {
            transactionViewModel.getAllTransactions().observe(this, new Observer<List<Transaction>>() {
                @SneakyThrows
                @Override
                public void onChanged(@Nullable List<Transaction> transactions) {
                   Collections.reverse(transactions);

                    customAdapter.setData(converter.convertDsToDtos(transactions));
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
