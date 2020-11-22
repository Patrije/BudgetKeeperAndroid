package com.example.pati.retrofitappintro.model;


public class TransactionDto {

    private long transactionId;
    private double value;
    private Long dateOfTransaction;
    private String categoryName;

    public TransactionDto() {
    }

    public TransactionDto(long transactionId, double value, Long dateOfTransaction, String categoryName) {
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

    public Long getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Long dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
