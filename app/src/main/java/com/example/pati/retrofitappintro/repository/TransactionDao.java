package com.example.pati.retrofitappintro.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.pati.retrofitappintro.model.Transaction;

import java.util.List;

/**
 * Created by Pati on 11.11.2018.
 */
@Dao
public interface TransactionDao {

    @Insert
    void insert(Transaction transaction);

    @Query("delete  from `Transaction` ")
    void deleteAll();

    @Query("select sum(value) from `Transaction`")
    Double getSumOfTransaction();

    @Query("select transactionId,value,category,dateOfTransaction from `Transaction` order by dateOfTransaction desc")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("select transactionId, value, category, dateOfTransaction from `Transaction` order by dateOfTransaction asc")
    List<Transaction> getAllTransactionASC();
}