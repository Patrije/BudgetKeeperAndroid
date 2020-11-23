package com.example.pati.retrofitappintro.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.pati.retrofitappintro.model.Category;
import com.example.pati.retrofitappintro.model.Transaction;
import com.example.pati.retrofitappintro.repository.CategoryRepository;
import com.example.pati.retrofitappintro.repository.TransactionRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pati on 12.11.2018.
 */

public class TransactionViewModel extends AndroidViewModel {

    private static final String LOGIN_KEY="BudgetKeeperKey";
    private TransactionRepository transactionRepository;
    private CategoryRepository categoryRepository;
    private Double transactionSum;
    private LiveData<List<Transaction>> transactionList;
    private LiveData<List<Transaction>> transactionListGroupedByDays;
    private SharedPreferences sharedPreferences;
    private LiveData<List<Category>> categoriesList;

    public TransactionViewModel(Application application) throws ExecutionException, InterruptedException {
        super(application);

        transactionRepository = new TransactionRepository(application);
        categoryRepository = new CategoryRepository(application);
        transactionSum = transactionRepository.getTransactionSum();
        transactionList = transactionRepository.getAllTransactions();
        transactionListGroupedByDays = transactionRepository.getAllTransactionsGroupedByDays();
        categoriesList = categoryRepository.getAllCategories();
        sharedPreferences = application.getSharedPreferences("login_pref",Context.MODE_PRIVATE);
    }

    public void setUpViewModel() throws ExecutionException, InterruptedException {

    }

    public void saveToSharedPref(String login){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_KEY,login);
        editor.commit();
    }

    public String getFromSharedPref(){
return sharedPreferences.getString(LOGIN_KEY,"");
    }

    public Double getTransactionSum() {
        return transactionSum;
    }

    public void insert(Transaction transaction) {
        transactionRepository.insertTransaction(transaction);
    }


    public LiveData<List<Transaction>> getAllTransactions() throws ExecutionException, InterruptedException {
        if (transactionList == null) {
            transactionList = transactionRepository.getAllTransactions();
        }
        return transactionList;
    }

    public LiveData<List<Transaction>> getAllTransactionsGroupedByDays() throws ExecutionException, InterruptedException {
        if (transactionListGroupedByDays == null) {
            transactionListGroupedByDays = transactionRepository.getAllTransactionsGroupedByDays();
        }
        return transactionListGroupedByDays;
    }

    public LiveData<List<Category>> getAllCategories() {
        if(categoriesList == null) {
            categoriesList = categoryRepository.getAllCategories();
        }
        return categoriesList;
    }



    public void deleteTransactions() {
        transactionRepository.deleteTransactions();
    }

}
