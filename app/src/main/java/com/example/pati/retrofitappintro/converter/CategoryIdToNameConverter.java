package com.example.pati.retrofitappintro.converter;

import android.app.Application;

import com.example.pati.retrofitappintro.repository.CategoryRepository;

import java.util.concurrent.ExecutionException;

public class CategoryIdToNameConverter {

    public static String convertIdToName(Application application, long categoryId) throws ExecutionException, InterruptedException {
        return new CategoryRepository(application).getCategoryNameById(categoryId);
    }
}
