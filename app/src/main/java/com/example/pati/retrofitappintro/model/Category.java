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
public class Category {

    @PrimaryKey(autoGenerate = true)
    private long categoryId;
    private String name;

    public Category(String name) {
        this.name = name;
    }

    @Ignore
    public Category(long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
