package com.example.pati.retrofitappintro.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.example.pati.retrofitappintro.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Transaction
    @Insert
    void insertCategory(Category category);

    @Transaction
    @Query("Select * from `Category`")
    LiveData<List<Category>> getAllCategories();

    @Transaction
    @Query("select name from    Category where categoryId = :categoryId")
    String getCategoryName(long categoryId);
}
