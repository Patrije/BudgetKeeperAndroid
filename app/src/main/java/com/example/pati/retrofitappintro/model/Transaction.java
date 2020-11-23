package com.example.pati.retrofitappintro.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import lombok.Data;

/**
 * Created by Pati on 11.11.2018.
 */

@Data
@Entity
@TypeConverters(DateConverter.class)
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    private long transactionId;
    private double value;
    private String dateOfTransaction;
    private long categoryId;

    public Transaction( double value, String dateOfTransaction, long categoryId) {
        this.value = value;
        this.dateOfTransaction = dateOfTransaction;
        this.categoryId = categoryId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(String dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
