package com.example.pati.retrofitappintro.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import lombok.Data;

/**
 * Created by Pati on 11.11.2018.
 */

@Data
@Entity
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    private long transactionId;
    private double value;
    private Long dateOfTransaction;
    private long categoryId;

    public Transaction( double value, Long dateOfTransaction, long categoryId) {
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

    public Long getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Long dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
