package com.example.pati.retrofitappintro.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.pati.retrofitappintro.model.Category;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CategoryRepository {
    private CategoryDao categoryDao;

    public CategoryRepository(Application application){
        CategoryDatabase db = CategoryDatabase.getDatabase(application);
        categoryDao = db.categoryDao();
    }

    public LiveData<List<Category>> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    public String getCategoryNameById(long categoryId) throws ExecutionException, InterruptedException {
        return new getCategoryNameAsync(categoryDao).execute(categoryId).get();
    }

    public void insertCategory(Category category) {
        new CategoryRepository.insertAsyncTask(categoryDao).execute(category);
    }

    private static class insertAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDao mAsyncTaskDao;

        insertAsyncTask(CategoryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Category... params) {
            mAsyncTaskDao.insertCategory(params[0]);
            return null;
        }
    }

    private static class getCategoryNameAsync extends AsyncTask<Long, Void, String> {

        private CategoryDao mAsyncTaskDao;

        getCategoryNameAsync(CategoryDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected String doInBackground(Long... categoryId) {
            return mAsyncTaskDao.getCategoryName(categoryId[0]);
        }
    }
}
