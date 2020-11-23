package com.example.pati.retrofitappintro.model;


import java.util.Date;

public class TransactionDto {

    private long transactionId;
    private double value;
    private String dateOfTransaction;
    private String categoryName;

    public TransactionDto() {
    }

    public TransactionDto(long transactionId, double value, String dateOfTransaction, String categoryName) {
        this.transactionId = transactionId;
        this.value = value;
        this.dateOfTransaction = dateOfTransaction;
        this.categoryName = categoryName;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
