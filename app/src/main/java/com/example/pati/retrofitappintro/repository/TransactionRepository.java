package com.example.pati.retrofitappintro.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.pati.retrofitappintro.model.Transaction;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pati on 11.11.2018.
 */

public class TransactionRepository {
    private TransactionDao transactionDao;
    private Double transactionSum;

    public TransactionRepository(Application application) {
        TransactionDatabase db = TransactionDatabase.getDatabase(application);
        transactionDao = db.transactionDao();

    }

    public Double getTransactionSum() throws ExecutionException, InterruptedException {
        return new getSum(transactionDao).execute().get();
    }

    public List<Transaction> getAllTransactionASC() throws ExecutionException, InterruptedException {
        return new getAllTransactionAsc(transactionDao).execute().get();
    }


    public void insertTransaction(Transaction transaction) {
        new insertAsyncTask(transactionDao).execute(transaction);
    }

    public void deleteTransactions() {
        new deleteAsyncTask(transactionDao).execute();
    }

    public LiveData<List<Transaction>> getAllTransactions() throws ExecutionException, InterruptedException {
        return transactionDao.getAllTransactions();
    }

    private static class insertAsyncTask extends AsyncTask<Transaction, Void, Void> {

        private TransactionDao mAsyncTaskDao;

        insertAsyncTask(TransactionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Transaction... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {

        private TransactionDao mAsyncTaskDao;

        deleteAsyncTask(TransactionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class getSum extends AsyncTask<Void, Void, Double> {

        private TransactionDao mAsyncTaskDao;

        getSum(TransactionDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Double doInBackground(Void... voids) {
            return mAsyncTaskDao.getSumOfTransaction();
        }
    }

    private static class getAllTransactionAsc extends AsyncTask<Void, Void, List<Transaction>> {

        private TransactionDao mAsyncTaskDao;

        getAllTransactionAsc(TransactionDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected List<Transaction> doInBackground(Void... voids) {
            return mAsyncTaskDao.getAllTransactionASC();
        }
    }

//    private static class getAllTransactionsList extends AsyncTask<Void, Void, List<Transaction>> {
//
//        private TransactionDao mAsyncTaskDao;
//
//        getAllTransactionsList(TransactionDao dao) {
//            mAsyncTaskDao = dao;
//        }
//
//
//        @Override
//        protected List<Transaction> doInBackground(Void... voids) {
//            return mAsyncTaskDao.getAllTransactionsWithoutRequests();
//        }
//    }


}
