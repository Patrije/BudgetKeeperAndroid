package com.example.pati.retrofitappintro.converter;

import android.app.Application;

import com.example.pati.retrofitappintro.model.Transaction;
import com.example.pati.retrofitappintro.model.TransactionDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TransactionDsToTransactionDtoConverter {

    private Application application;

    public TransactionDsToTransactionDtoConverter(Application application) {
        this.application = application;
    }

    public TransactionDto convertToDto(Transaction transaction) throws ExecutionException, InterruptedException {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionId(transaction.getTransactionId());
        transactionDto.setDateOfTransaction(transaction.getDateOfTransaction());
        transactionDto.setValue(transaction.getValue());
        transactionDto.setCategoryName(CategoryIdToNameConverter.convertIdToName(application, transaction.getCategoryId()));
        return transactionDto;
    }

    public List<TransactionDto> convertDsToDtos(List<Transaction> transactions) throws ExecutionException, InterruptedException {
        List<TransactionDto> transactionDtos = new ArrayList<>();
        for(Transaction transaction: transactions){
            transactionDtos.add(convertToDto(transaction));
        }
        return transactionDtos;
    }
}
